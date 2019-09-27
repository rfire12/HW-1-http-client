import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

public class Main {

    public static void main(String [] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("URL: ");
        String url = scanner.nextLine();
        Document htmlDocument = Jsoup.connect(url).timeout(6000).get();

        System.out.println(String.format("La pagina contiene %d lineas", getNumberOfLines(Jsoup.connect(url).execute().body())));
        System.out.println(String.format("La pagina contiene %d parrafos", getNumberOfParagraphs(htmlDocument)));
        System.out.println(String.format("La pagina contiene %d imagenes dentro de los parrafos", getImagesInParagraphs(htmlDocument)));
        System.out.println(String.format("La pagina contiene %d formularios con el metodo POST", getForms(htmlDocument, "post")));
        System.out.println(String.format("La pagina contiene %d formularios con el metodo GET", getForms(htmlDocument, "get")));
        System.out.println();
        printFormsInputsType(htmlDocument);
        makeRequest(htmlDocument, "http://itachi.avathartech.com:4567/opcion2.html");
    }

    private static int getNumberOfLines(String document){
        return document.split("\n").length;
    }

    private static int getNumberOfParagraphs(Document document){
        return document.select("p").size();
    }

    private static int getImagesInParagraphs(Document document){
        return document.select("p").select("img").size();
    }

    private static int getForms(Document document, String method){
        Elements forms = document.select("form"); // Getting forms from the document
        int formsFound = 0;
        for(Element form: forms) {
            if(form.attr("method").equalsIgnoreCase(method))
                formsFound++;
        }
        return formsFound;
    }

    private static void printFormsInputsType(Document document){
        Elements forms = document.select("form");
        int formIndex = 1, inputIndex = 1;
        for(Element form : forms){
            System.out.println(String.format("Formulario #%d: Method: %s", formIndex, form.attr("method")));
            for(Element input: form.select("input")){
                System.out.println(String.format("Input type #%d: %s \n",inputIndex, input.attr("type")));
                inputIndex++;
            }
            formIndex++;
            inputIndex = 1; // Restart the input index each time we change the form
        }
    }

    private static void makeRequest(Document document, String url){
        Elements forms = document.select("form");

        for(Element form : forms) {
            if(form.attr("method").equalsIgnoreCase("post")){
                url = parseURL(form, url);
                System.out.println(url);
                try{
                    Document response = Jsoup.connect(url).data("asignatura", "practica1").header("matricula", "20160792").post();
                    System.out.println(response);
                } catch (Exception e){
                    e.printStackTrace();
                }

            }

        }
    }

    private static String parseURL(Element form, String url) {
        try{
            URL newUrl = new URL(url);
            if(form.attr("action").contains("http")) //If the form action contains a full URL
                url = form.attr("action"); // Use the form URL
            else
                url = newUrl.getProtocol() + "://" + newUrl.getHost() + ":" + newUrl.getPort() + form.attr("action"); // Append the form action to the given URL by the user
            return url;
        } catch (Exception e){
            System.out.println("Error parsing url");
        }
        return url;
    }
}
