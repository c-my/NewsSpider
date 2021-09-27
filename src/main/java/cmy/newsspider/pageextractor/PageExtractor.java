package cmy.newsspider.pageextractor;

import cmy.newsspider.record.NewsRecord;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.regex.Pattern;

public abstract class PageExtractor implements Callable<NewsRecord> {
    // sohu: http://www.sohu.com/a/492356464_121110808
    // netease: https://www.163.com/dy/article/GKTDREEB0548OUH9.html
    // netease: http://dy.163.com/article/GK5SJVEL05529M2G.html
    // tecent: https://new.qq.com/omn/20210927/20210927A05QTU00.html
    // ifeng: https://news.ifeng.com/c/89sSRm9IoyG
    // chinanews: http://www.chinanews.com/gn/2021/09-27/9574765.shtml
    // youth: http://news.youth.cn/jsxw/202109/t20210916_13222690.htm
    // xinhua: http://www.news.cn/mil/2021-09/16/c_1211371426.htm
    // huanqer: https://world.huanqiu.com/article/44j5MGsP4Cv
    // zhonghua: https://news.china.com/domestic/945/20210809/39853554.html


    static public PageExtractor getExtractor(String itemUrl) {
        var r = Pattern.compile("://(.*?)/");
        var matcher = r.matcher(itemUrl);
        if (!matcher.find()) {
            return null;
        }
        return switch (matcher.group(1)) {
            case "www.sohu.com" -> new SohuExtractor(itemUrl);
            case "www.163.com", "dy.163.com", "news.163.com" -> new NeteaseExtractor(itemUrl);
            case "new.qq.com" -> new TecentExtractor(itemUrl);
            case "news.ifeng.com", "i.ifeng.com", "mil.ifeng.com" -> new IFengExtractor(itemUrl);
            case "www.chinanews.com" -> new ChinanewsExtractor(itemUrl);
            case "news.youth.cn" -> new YouthExtractor(itemUrl);
            case "www.news.cn", "xinhuanet.com", "www.xinhuanet.com" -> new XinhuaExtractor(itemUrl);
            case "world.huanqiu.com", "mil.huanqiu.com", "china.huanqiu.com" -> new HuanqiuExtractor(itemUrl);
            case "news.china.com" -> new ZhonghuaExtractor(itemUrl);
            default -> null;
        };
    }

    protected Document pageDoc;

    public PageExtractor(String url) {
        try {
            this.pageDoc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.61 Safari/537.36")
                    .timeout(3000)
                    .get();

        } catch (IOException exception) {
            this.pageDoc = null;
        }
    }

    public NewsRecord getNewsRecord() throws IOException {
        if (this.pageDoc == null) {
            return null;
        }
        var title = getTitle();
        var content = getContent();
        var source = getSource();
        var date = getDate();
        if (title.equals("") && content.equals("") && source.equals("") && date.equals("")) {
            System.out.println(this.pageDoc.location());
            return null;
        }
        return new NewsRecord(title, content, source, date);
    }

    @Override
    public NewsRecord call() throws Exception {
        return getNewsRecord();
    }

    public abstract String getTitle();

    public abstract String getSource();

    public abstract String getDate();

    public abstract String getContent();

}
