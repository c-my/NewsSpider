package cmy.newsspider.pageextractor;

import cmy.newsspider.record.NewsRecord;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class TecentExtractor extends PageExtractor {

    public TecentExtractor(String url) {
        super(url);
    }


    @Override
    public String getTitle() {
        if (pageDoc == null) return "";
        var titleElement = pageDoc.select("div.LEFT h1");
        return titleElement.text();
    }

    @Override
    public String getSource() {
        return "";
    }

    @Override
    public String getDate() {
        return "";
    }

    @Override
    public String getContent() {
        if (pageDoc == null) return "";
        var contentElement = pageDoc.select("div.content-article");
        return contentElement.text();
    }
}
