package cmy.newsspider.pageextractor;

public class ZhonghuaExtractor extends PageExtractor {
    public ZhonghuaExtractor(String url) {
        super(url);
    }

    @Override
    public String getTitle() {
        if (pageDoc == null) return "";
        var titleElement = pageDoc.select("h1.article_title");
        return titleElement.text();
    }

    @Override
    public String getSource() {
        if (pageDoc == null) return "";
        var sourceElement = pageDoc.select("span.source");
        return sourceElement.text();
    }

    @Override
    public String getDate() {
        if (pageDoc == null) return "";
        var dateElement = pageDoc.select("span.time");
        return dateElement.text();
    }

    @Override
    public String getContent() {
        if (pageDoc == null) return "";
        var contentElement = pageDoc.select("#detail");
        return contentElement.text();
    }
}
