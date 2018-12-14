import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class AnalyzerUtils {
    private static String CHARSET_NAME = "utf8";

    private static Optional<Document> getDoc(File htmlFile) {
        try {
            return Optional.of(Jsoup.parse(
                    htmlFile,
                    CHARSET_NAME,
                    htmlFile.getAbsolutePath()));

        } catch (IOException e) {
            return Optional.empty();
        }
    }
    public static Optional<Element> findElementById(File htmlFile, String targetElementId) {
        Optional<Document> doc = getDoc(htmlFile);
        return doc.map(document -> document.getElementById(targetElementId));
    }

    public static Optional<Elements> findElementsByCssQuery(File htmlFile, String cssQuery) {
        Optional<Document> doc = getDoc(htmlFile);
        return doc.map(document -> document.select(cssQuery));
    }
}
