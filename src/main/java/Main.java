import java.io.*;
import java.util.concurrent.ForkJoinPool;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Enter the link to the website: ");
        String url = br.readLine();
        br.close();

        LinkExtractor linkExtractor = new    LinkExtractor(url,url);
        String siteLinks = new ForkJoinPool().invoke(linkExtractor);
        System.out.println(siteLinks);

        String filePath = "siteMap.txt";

        File file = new File(filePath);
        try (PrintWriter writer = new PrintWriter(file)) {
            writer.write(siteLinks);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("Map created!");
    }
}
