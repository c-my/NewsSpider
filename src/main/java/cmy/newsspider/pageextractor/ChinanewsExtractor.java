package cmy.newsspider.pageextractor;

import cmy.newsspider.record.NewsRecord;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class ChinanewsExtractor extends PageExtractor {

    public ChinanewsExtractor(String url) {
        super(url);
    }


    @Override
    public String getTitle() {
        if (pageDoc == null) return "";
        var titleElement = pageDoc.select("div.content>h1");
        if (titleElement.isEmpty()) {
            return "";
        }
        return titleElement.text().strip();
    }

    @Override
    public String getSource() {
        if (pageDoc == null) return "";
        var sourceElement = pageDoc.select("div.content>div.left-time>div.left-t");
        if (sourceElement.isEmpty()) {
            return "";
        }
        var eachText = sourceElement.get(0).children().eachText();
        var leftText = sourceElement.get(0).textNodes().get(0).getWholeText();

        //去除最后一个互动标签
        var source = String.join("", eachText.subList(0, eachText.size() - 1));
        var prefix = "";
        var splitList = leftText.split("来源：");
        if (splitList.length > 1) {
            prefix = splitList[1];
        }
        return prefix + source.strip();
    }

    @Override
    public String getDate() {
        if (pageDoc == null) return "";
        var dateElement = pageDoc.select("div.content>div.left-time>div.left-t");
        if (dateElement.isEmpty()) {
            return "";
        }
        var leftText = dateElement.get(0).textNodes().get(0).getWholeText();

        return leftText.split("来源")[0].strip();
    }

    @Override
    public String getContent() {
        if (pageDoc == null) return "";
        var contentElement = pageDoc.select("div.content>div.left_zw");
        if (contentElement.isEmpty()) {
            return "";
        }
        return contentElement.text();
    }

}
