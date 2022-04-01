import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.RecursiveTask;

public class LinkExtractor extends RecursiveTask<String> {

    private String url;
    private static String startURL;
    private static CopyOnWriteArraySet<String> links = new CopyOnWriteArraySet<>();

    public LinkExtractor(String url) {
        this.url = url.trim();
    }

    public LinkExtractor(String url, String startURL) {
        this.url = url.trim();
        LinkExtractor.startURL = startURL.trim();
    }

    @Override
    protected String compute() {
        StringBuffer stringBuffer = new StringBuffer(url + "\n");
        Set<LinkExtractor> tasks = new HashSet<>();

        getLinks(tasks);

        for (LinkExtractor linkExtractor : tasks) {
            stringBuffer.append(linkExtractor.join());
        }
        return stringBuffer.toString();
    }

    private void getLinks(Set<LinkExtractor> tasks) {
        Document document;
        Elements elements;
        try {
            Thread.sleep(100);
            document = Jsoup.connect(url).get();
            elements = document.select("a");
            for (Element element : elements) {
                String attr = element.attr("abs:href");
                if(!attr.isEmpty() && attr.startsWith(startURL) && !attr.contains("#") && !LinkExtractor.links.contains(attr)) {
                    LinkExtractor linkExtractor = new LinkExtractor(attr);
                    linkExtractor.fork();
                    tasks.add(linkExtractor);
                    System.out.println(attr);
                    LinkExtractor.links.add(attr);
                }
            }
        }catch (IOException | InterruptedException ignored) {
        }
    }

}
