package cmy.newsspider.pageextractor;

import cmy.newsspider.record.NewsRecord;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class NeteaseExtractor extends PageExtractor {

    public NeteaseExtractor(String url) {
        super(url);
    }

    @Override
    public String getTitle() {
        if (pageDoc == null) return "";
        var titleElement = pageDoc.select("h1.post_title");
        return titleElement.text();
    }

    @Override
    public String getSource() {
        if (pageDoc == null) return "";
        var sourceElement = pageDoc.select("div.post_info");
        try {
            return sourceElement.text().split("来源")[1];
        } catch (Exception e) {
            System.out.println(sourceElement);
            return "";
        }
    }

    @Override
    public String getDate() {
        if (pageDoc == null) return "";
        var sourceElement = pageDoc.select("div.post_info");
        return sourceElement.text().split("来源")[0];
    }

    @Override
    public String getContent() {
        if (pageDoc == null) return "";
        var contentElement = pageDoc.select("div.post_content>div.post_body");
        return contentElement.text();
    }
}
