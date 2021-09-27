package cmy.newsspider.pageextractor;

import cmy.newsspider.record.NewsRecord;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class SinaExtractor extends PageExtractor {

    public SinaExtractor(String url) {
        super(url);
    }

    @Override
    public String getTitle() {
        if (pageDoc == null) return "";
        var titleElement = pageDoc.select("h1.main-title");
        return titleElement.text();
    }

    @Override
    public String getSource() {
        if (pageDoc == null) return "";
        var sourceElement = pageDoc.select("div.date-source a.source");
        return sourceElement.text();
    }

    @Override
    public String getDate() {
        if (pageDoc == null) return "";
        var dateElement = pageDoc.select("div.date-source span.date");
        return dateElement.text();
    }

    @Override
    public String getContent() {
        if (pageDoc == null) return "";
        var contentElement = pageDoc.select("#article");
        return contentElement.text();
    }

}
