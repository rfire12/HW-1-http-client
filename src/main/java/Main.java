import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class Main {

    public static void main(String [] args) throws IOException {
        String url = "http://itachi.avathartech.com:4567/opcion2.html";
        Document htmlDocument = Jsoup.connect(url).timeout(6000).get();

        System.out.println(String.format("La pagina contiene %d lineas", getNumberOfLines(htmlDocument.toString())));
        System.out.println(String.format("La pagina contiene %d parrafos", getNumberOfParagraphs(htmlDocument)));
        System.out.println(String.format("La pagina contiene %d imagenes dentro de los parrafos", getImagesInParagraphs(htmlDocument)));
        System.out.println(String.format("La pagina contiene %d formularios con el metodo POST", getForms(htmlDocument, "post")));
        System.out.println(String.format("La pagina contiene %d formularios con el metodo GET", getForms(htmlDocument, "get")));
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
        Elements forms = document.select("form"); // Getting the forms from the document
        int formsFound = 0;
        for(Element form: forms) {
            if(form.attr("method").equalsIgnoreCase(method))
                formsFound++;
        }
        return formsFound;
    }






}
