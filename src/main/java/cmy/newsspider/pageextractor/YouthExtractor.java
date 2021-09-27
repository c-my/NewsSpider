package cmy.newsspider.pageextractor;

import cmy.newsspider.record.NewsRecord;

import java.io.IOException;

public class YouthExtractor extends PageExtractor {


    public YouthExtractor(String url) {
        super(url);
    }

    @Override
    public String getTitle() {
        if (pageDoc == null) return "";
        var titleElement = pageDoc.select("div.page_title>h1");
        return titleElement.text();
    }

    @Override
    public String getSource() {
        if (pageDoc == null) return "";
        var sourceElement = pageDoc.select("#source_baidu");
        return sourceElement.text();
    }

    @Override
    public String getDate() {
        if (pageDoc == null) return "";
        var dateElement = pageDoc.select("#page_right");
        return dateElement.text();
    }

    @Override
    public String getContent() {
        if (pageDoc == null) return "";
        var contentElement = pageDoc.select("div.page_left>div.page_k2");
        var first = contentElement.first();
        if (first == null) return "";
        return first.text();
    }

}
