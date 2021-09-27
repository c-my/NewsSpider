package cmy.newsspider.pageextractor;

public class XinhuaExtractor extends PageExtractor {
    public XinhuaExtractor(String url) {
        super(url);
    }

    @Override
    public String getTitle() {
        if (pageDoc == null) return "";
        var titleElement = pageDoc.select("div.head-line span.title");
        return titleElement.text();
    }

    @Override
    public String getSource() {
        if (pageDoc == null) return "";
        var sourceElement = pageDoc.select("div.source");
        return sourceElement.text();
    }

    @Override
    public String getDate() {
        if (pageDoc == null) return "";
        var dateElement = pageDoc.select("div.header-time");
        return dateElement.text();
    }

    @Override
    public String getContent() {
        if (pageDoc == null) return "";
        var contentElement = pageDoc.select("#detail");
        return contentElement.text();
    }
}
