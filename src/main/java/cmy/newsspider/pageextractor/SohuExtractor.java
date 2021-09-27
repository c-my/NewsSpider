package cmy.newsspider.pageextractor;

import cmy.newsspider.record.NewsRecord;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class SohuExtractor extends PageExtractor {

    public SohuExtractor(String url) {
        super(url);
    }


    @Override
    public String getTitle() {
        if (pageDoc == null) return "";
        var titleElement = pageDoc.select("div.text-title h1");
        if (titleElement.isEmpty()) {
            titleElement = pageDoc.select("h3.article-title");
        }
        return titleElement.text();
    }

    @Override
    public String getSource() {
        if (pageDoc == null) return "";
        var sourceElement = pageDoc.select("div.article-info span[data-role='original-link']");
        return sourceElement.text();
    }

    @Override
    public String getDate() {
        if (pageDoc == null) return "";
        var dateElement = pageDoc.select("div.article-info span.time");
        if (dateElement.isEmpty()) {
            dateElement = pageDoc.select("p.article-info span.time");

        }
        return dateElement.text();
    }

    @Override
    public String getContent() {
        if (pageDoc == null) return "";
        var contentElement = pageDoc.select("article.article");
        if (contentElement.isEmpty()) {
            contentElement = pageDoc.select("article.article-text");
        }
        return contentElement.text();
    }
}
