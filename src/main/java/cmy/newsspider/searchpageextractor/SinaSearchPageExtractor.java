package cmy.newsspider.searchpageextractor;

import cmy.newsspider.pageextractor.SinaExtractor;
import cmy.newsspider.record.NewsRecord;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Pattern;

public class SinaSearchPageExtractor implements SearchPageExtractor {
    final static String searchBaseUrl = "https://search.sina.com.cn/news?q=%s&c=news&sort=time";

    static String getSearchUrl(String keyword) {
        return String.format(searchBaseUrl, keyword);
    }

    private Document getSearchPage(String keyword) throws IOException {
        var searchUrl = getSearchUrl(keyword);
        return getSearchPageByUrl(searchUrl);
    }

    private Document getSearchPageByUrl(String url) throws IOException {
        var pageDoc = Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.61 Safari/537.36")
                .timeout(3000)
                .get();
        return pageDoc;
    }

    private ArrayList<String> getItemUrls(Document doc) {
        var selector = "div.box-result h2>a";
        var itemLinks = doc.select(selector);
        var resultLinks = new ArrayList<String>();
        for (var link :
                itemLinks) {
            resultLinks.add(link.attr("href"));
        }
        return resultLinks;
    }

    private String getNextPageUrl(Document doc) {
        var selector = "div.pagebox a[title='下一页']";
        var nextPageTag = doc.select(selector).first();
        if (nextPageTag == null) {
            return null;
        }
        var nextPageLinkText = nextPageTag.attr("href");
        var r = Pattern.compile("javascript:linkPost\\('(.*)','(.*)'\\)");
        var matcher = r.matcher(nextPageLinkText);
        if (!matcher.find()) {
            return null;
        }
        return "https://search.sina.com.cn" + matcher.group(1) + matcher.group(2);
    }

    private ArrayList<String> getAllItemLink(String keyword, int maxPage) {
        var urlList = new ArrayList<String>();
        Document searchPage;
        int currentPage = 1;
        try {
            searchPage = getSearchPage(keyword);
        } catch (IOException exception) {
            return null;
        }
        var itemUrls = getItemUrls(searchPage);
        urlList.addAll(itemUrls);

        var nextPageUrl = getNextPageUrl(searchPage);

        while (nextPageUrl != null && currentPage < maxPage) {
            try {
                searchPage = getSearchPageByUrl(nextPageUrl);
                itemUrls = getItemUrls(searchPage);
                urlList.addAll(itemUrls);
                nextPageUrl = getNextPageUrl(searchPage);
                currentPage++;
            } catch (IOException exception) {
                break;
            }
        }
        return urlList;
    }

    @Override
    public ArrayList<NewsRecord> searchByKeyword(String keyword, int maxPage) {
        var resultRecord = new ArrayList<NewsRecord>();

        var allItemLink = getAllItemLink(keyword, maxPage);

        var executor = Executors.newCachedThreadPool();
        var futureList = new ArrayList<Future<NewsRecord>>();
        for (String link : allItemLink) {
            var future = executor.submit(new SinaExtractor(link));
            futureList.add(future);
        }
        for (var future :
                futureList) {
            try {
                resultRecord.add(future.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        executor.shutdown();
        return resultRecord;
    }
}
