package cmy.newsspider.pageextractor;

import cmy.newsspider.record.NewsRecord;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class IFengExtractor extends PageExtractor {

    public IFengExtractor(String url) {
        super(url);
    }


    @Override
    public String getTitle() {
        if (pageDoc == null) return "";
        var titleElement = pageDoc.select("div[class^=artical] h1[class^=topic-]");
        if (titleElement.isEmpty()) {
            titleElement = pageDoc.select("#artical_topic");
        }
        return titleElement.text().strip();
    }

    @Override
    public String getSource() {
        if (pageDoc == null) return "";
        var sourceElement = pageDoc.select("div[class^=artical] div[class^=info-] p[class^=time]>span[class^=publisher]");
        if (sourceElement.isEmpty()) {
            sourceElement = pageDoc.select("span[itemprop=publisher]");
        }
        return sourceElement.text().strip();
    }

    @Override
    public String getDate() {
        if (pageDoc == null) return "";
        var dateElement = pageDoc.select("div[class^=artical] div[class^=info-] p[class^=time]>span");
        if (dateElement.isEmpty()) {
            dateElement = pageDoc.select("span[itemprop=datePublished]");
        }
        return dateElement.get(0).text().strip();
    }

    @Override
    public String getContent() {
        if (pageDoc == null) return "";
        var contentElement = pageDoc.select("div[class^=artical] div[class^=main_content-]");
        if (contentElement.isEmpty()) {
            contentElement = pageDoc.select("div #artical_real");
        }
        return contentElement.text();
    }

}
