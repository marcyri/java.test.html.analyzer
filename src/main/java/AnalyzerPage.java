import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AnalyzerPage {
    private AnalyzerArgs args;
    private Element originButton;
    private Elements preFindElements;

    public AnalyzerPage(AnalyzerArgs args) {
        this.args = args;
    }

    public void fireAnalyze() {
        originButton = getOriginButton();
        if (originButton == null) {
            System.out.println("Original button is not present :(");
            return;
        }

        // get preFind elements
        preFindElements = getPreFindElements(getCssQueryForSearch());
        if (preFindElements == null || preFindElements.size() < 1) {
            System.out.println("Not found :(");
            return;
        }

        // if element was found
        if (preFindElements.size() == 1) {
            printResult(preFindElements.get(0));
            return;
        }

        // if elements > 1 - check count of equals attribute
        Element findElement = getMaxCountAttrFindElement();
        if (findElement == null) {
            System.out.println("Not found :(");
        } else {
            printResult(findElement);
        }
    }

    private Element getMaxCountAttrFindElement() {
        // fill count of equals attr
        Map<Element, Integer> preFindElementsWithAttrCount = new HashMap<>();
        for (Element el : preFindElements) {
            int countEqualAttr = 0;
            for (Attribute attrFind : el.attributes()) {
                for (Attribute attrOrigi : originButton.attributes()) {
                    if (attrOrigi.equals(attrFind)) {
                        countEqualAttr++;
                    }
                }
            }
            preFindElementsWithAttrCount.put(el, countEqualAttr);
        }

        // filter element by max count attr equals
        Optional<Map.Entry<Element, Integer>> elWithMaxAttrCount = preFindElementsWithAttrCount.entrySet().stream().max(Map.Entry.comparingByValue());
        List<Element> filterElementsByMaxCount = null;
        if (elWithMaxAttrCount.isPresent()) {
            filterElementsByMaxCount = preFindElementsWithAttrCount.entrySet().stream()
                    .filter(e -> e.getValue() == elWithMaxAttrCount.get().getValue())
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
        }

        // if element was found
        if (filterElementsByMaxCount.size() == 1) {
            return filterElementsByMaxCount.get(0);
        }

        // if find elements by max count attr equals > 1 => check the parents
        Map<Element, Integer> equalParentsCountPerElements = new HashMap<>();
        for (Element filterElement : filterElementsByMaxCount) {
            int countEqualParents = 0;
            for (Element filterElementParent : filterElement.parents()) {
                //path.insert(0, filterElementParent.tagName() + " > ");
                for (Element originalElementParent : originButton.parents()) {
                    if (originalElementParent.attributes().equals(filterElementParent.attributes())) {
                        countEqualParents++;
                    }
                }
            }
            equalParentsCountPerElements.put(filterElement, countEqualParents);
        }
        Optional<Map.Entry<Element, Integer>> elWithMaxParentsCount =
                equalParentsCountPerElements.entrySet().stream().max(Map.Entry.comparingByValue());

        List<Element> filterElementsByParentsCount = null;
        if (elWithMaxParentsCount.isPresent()) {
            filterElementsByParentsCount = preFindElementsWithAttrCount.entrySet().stream()
                    .filter(e -> e.getValue() == elWithMaxParentsCount.get().getValue())
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
        }

        // if element was found
        if (filterElementsByParentsCount.size() == 1) {
            return filterElementsByParentsCount.get(0);
        }

        return null;
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
            findCssQuery.append(findsTag)
                    .append("[").append(attr.getKey())
                    .append("='").append(attr.getValue()).append("'],");
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
