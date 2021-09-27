package cmy.newsspider.pageextractor;

public class HuanqiuExtractor extends PageExtractor {
    public HuanqiuExtractor(String url) {
        super(url);
    }

    @Override
    public String getTitle() {
        if (pageDoc == null) return "";
        var titleElement = pageDoc.select("div.t-container-title>h3");
        return titleElement.text();
    }

    @Override
    public String getSource() {
        if (pageDoc == null) return "";
        var sourceElement = pageDoc.select("div.metadata-info span.source");
        return sourceElement.text();
    }

    @Override
    public String getDate() {
        if (pageDoc == null) return "";
        var dateElement = pageDoc.select("div.metadata-info p.time");
        return dateElement.text();
    }

    @Override
    public String getContent() {
        if (pageDoc == null) return "";
        var contentElement = pageDoc.select("div.b-container div.l-con");
        return contentElement.text();
    }
}
