import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class AnalyzerPage {
    private AnalyzerArgs args;
    private Element originButton;
    private Elements preFindElements;

    private static Logger LOGGER = LoggerFactory.getLogger(AnalyzerPage.class);

    AnalyzerPage(AnalyzerArgs args) {
        this.args = args;
    }

    void fireAnalyze() {
        originButton = getOriginButton();
        if (originButton == null) {
            LOGGER.warn("Original button is not present :(");
            return;
        }

        // get preFind elements
        preFindElements = getPreFindElements(getCssQueryForSearch());
        if (preFindElements == null || preFindElements.size() < 1) {
            LOGGER.warn("Not found :(");
            return;
        }

        // if element was found
        if (preFindElements.size() == 1) {
            printResult(preFindElements.get(0));
            return;
        }

        // if elements > 1 - check count of equals attribute
        List<Element> findElement = getMaxCountAttrFindElement();
        if (findElement == null) {
            LOGGER.warn("Not found :(");
        } else {
            printResult(findElement);
        }
    }

    private List<Element> getMaxCountAttrFindElement() {
        // fill count of equals attr
        Map<Element, Integer> preFindElementsWithAttrCount = new HashMap<>();
        for (Element el : preFindElements) {
            int countEqualAttr = 0;
            for (Attribute attrOrigi : originButton.attributes()) {
                boolean isAdd = false;
                for (Iterator<Attribute> iter = el.attributes().iterator(); iter.hasNext() && !isAdd; ) {
                    Attribute attrFind = iter.next();
                    String attrOrigiStr = attrOrigi.toString().toLowerCase();
                    String attrFindStr = attrFind.toString().toLowerCase();
                    if (attrOrigiStr.equals(attrFindStr)) {
                        countEqualAttr++;
                        isAdd = true;
                    } else {
                        if (attrOrigi.getKey().toLowerCase().equals("class") && attrFind.getKey().toLowerCase().equals("class")) {
                            String[] splitedCssClass = attrOrigiStr.split(" ");
                            if (Stream.of(splitedCssClass).allMatch(attrFindStr::contains)) {
                                countEqualAttr++;
                                isAdd = true;
                            }
                        }
                    }
                }
            }
            preFindElementsWithAttrCount.put(el, countEqualAttr);
        }

        // filter element by max count attr equals
        Optional<Map.Entry<Element, Integer>> elWithMaxAttrCount = preFindElementsWithAttrCount.entrySet().stream().filter(e -> e.getValue() > 0).max(Map.Entry.comparingByValue());
        List<Element> filterElementsByMaxCount;
        if (elWithMaxAttrCount.isPresent()) {
            filterElementsByMaxCount = preFindElementsWithAttrCount.entrySet().stream()
                    .filter(e -> Objects.equals(e.getValue(), elWithMaxAttrCount.get().getValue()))
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
        } else {
            LOGGER.info("There is no equals attribute");
            return null;
        }

        // if element was found
        try {
            if (filterElementsByMaxCount.size() == 1) {
                return filterElementsByMaxCount;
            }
        } catch (Exception e) { // if something went wrong
            LOGGER.error("Something went wrong. Doesn't find elements with max equal count attr in stream :/ [{}]", e.getMessage());
            return null;
        }

        // TODO: add css styles analyzer
        LOGGER.info("Elements with max count attr equals > 1. => need add check css attributes (like a color)");

        return filterElementsByMaxCount;
    }

    private void printResult(List<Element> target) {
        for (Element el: target) {
            printResult(el);
        }
    }
    private void printResult(Element target) {
        List<String> ss = getPathResult(target);

        Collections.reverse(ss);
        StringBuilder b = new StringBuilder();
        ss.forEach(b::append);
        b.delete(b.length()-3, b.length());
        System.out.println(b);
    }
    private List<String> getPathResult(Element target) {
        return target.parents().stream()
                .flatMap((p) -> Stream.of(p.tagName() + p.attributes().asList() + " > "))
                .collect(Collectors.toList());
    }

    private Elements getPreFindElements(StringBuilder cssQueryForSearc) {
        Optional<Elements> preFindElements = AnalyzerUtils.findElementsByCssQuery(
                new File(args.getOtherSampleFilePath())
                , cssQueryForSearc.toString());
        if (preFindElements.isPresent() && !preFindElements.equals(Optional.empty())) {
            return preFindElements.get();
        }
        return null;
    }

    private StringBuilder getCssQueryForSearch() {
        StringBuilder findCssQuery = new StringBuilder();
        String findsTag = originButton.tag().getName();
        for (Attribute attr : originButton.attributes()) {
            if (attr.getKey().equals("class")) {
                String[] splitedCssClass = attr.getValue().split(" ");
                for (String cssClass: splitedCssClass) {
                    findCssQuery.append(findsTag)
                            .append("[").append(attr.getKey())
                            .append("*='").append(cssClass).append("'],");
                }
            } else {
                findCssQuery.append(findsTag)
                        .append("[").append(attr.getKey())
                        .append("*='").append(attr.getValue()).append("'],");
            }
        }
        findCssQuery.delete(findCssQuery.length()-1,findCssQuery.length());
        return findCssQuery;
    }

    private Element getOriginButton() {
        Optional<Element> originButton = AnalyzerUtils.findElementById(new File(args.getOriginFilePath()), args.getOriginElementId());
        if (originButton.isPresent() && !originButton.equals(Optional.empty())) {
            return originButton.get();
        }
        return null;
    }
}
