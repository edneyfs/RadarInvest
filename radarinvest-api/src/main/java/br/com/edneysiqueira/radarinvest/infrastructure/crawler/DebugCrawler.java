package br.com.edneysiqueira.radarinvest.infrastructure.crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class DebugCrawler {
    public static void main(String[] args) throws IOException {
        String url = "https://investidor10.com.br/acoes/fatos-relevantes-comunicados/?s=ITSA4";
        System.out.println("Crawling: " + url);

        try {
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                    .timeout(10000)
                    .get();

            System.out.println("Title: " + doc.title());

            // Strategy 1: Find the table/grid wrapper
            Elements tables = doc.select("div#news-section"); 
            if (tables.isEmpty()) {
                System.out.println("div#news-section not found. Trying generic classes.");
            }

            // Strategy 2: Find the specific text from the screenshot to locate the element
            Elements elementsWithText = doc.getElementsContainingOwnText("COMUNICADO SOBRE TRANSAÇÕES");
            System.out.println("Found " + elementsWithText.size() + " elements with text 'COMUNICADO SOBRE TRANSAÇÕES'");
            
            for (Element el : elementsWithText) {
                System.out.println("Element Tag: " + el.tagName());
                System.out.println("Element Classes: " + el.className());
                System.out.println("Parent Tag: " + el.parent().tagName());
                System.out.println("Parent HTML: " + el.parent().outerHtml());
                System.out.println("--------------------------------------------------");
            }
            
            // Strategy 3: Dump all links with 'link_comunicado'
            Elements links = doc.select("a[href*='link_comunicado']");
            System.out.println("Found " + links.size() + " links with 'link_comunicado'");
            if (!links.isEmpty()) {
                Element link = links.first();
                System.out.println("First link parent HTML: " + link.parent().outerHtml());
                if (link.parent().parent() != null) {
                     System.out.println("First link grandparent HTML: " + link.parent().parent().outerHtml());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
