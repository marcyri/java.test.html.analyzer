import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

class AnalyzerUtils {
    private static Logger LOGGER = LoggerFactory.getLogger(AnalyzerUtils.class);

    private static Optional<Document> getDoc(File htmlFile) {
        try {
            String CHARSET_NAME = "utf8";
            return Optional.of(Jsoup.parse(
                    htmlFile,
                    CHARSET_NAME,
                    htmlFile.getAbsolutePath()));

        } catch (IOException e) {
            LOGGER.error("Error reading [{}] file", htmlFile.getAbsolutePath(), e);
            return Optional.empty();
        }
    }
    static Optional<Element> findElementById(File htmlFile, String targetElementId) {
        Optional<Document> doc = getDoc(htmlFile);
        return doc.map(document -> document.getElementById(targetElementId));
    }

    static Optional<Elements> findElementsByCssQuery(File htmlFile, String cssQuery) {
        Optional<Document> doc = getDoc(htmlFile);
        return doc.map(document -> document.select(cssQuery));
    }
}
