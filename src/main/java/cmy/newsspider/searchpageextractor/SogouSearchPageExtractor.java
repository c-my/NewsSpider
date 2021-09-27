package cmy.newsspider.searchpageextractor;

import cmy.newsspider.pageextractor.PageExtractor;
import cmy.newsspider.pageextractor.SinaExtractor;
import cmy.newsspider.record.NewsRecord;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Pattern;

public class SogouSearchPageExtractor implements SearchPageExtractor {
    private final static String searchBaseUrl = "https://www.sogou.com/sogou?interation=1728053249&query=%s";
    private final static String baseUrl = "https://www.sogou.com";
    private final static String nextPageBaseUrl = "https://www.sogou.com/sogou";

    public SogouSearchPageExtractor() {
    }

    private String getSearchUrl(String keyword) {
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
        var selector = "div.vrwrap h3.vr-title>a";
        var itemLinks = doc.select(selector);
        var resultLinks = new ArrayList<String>();

        var executor = Executors.newCachedThreadPool();
        var futureList = new ArrayList<Future<String>>();
        for (var link :
                itemLinks) {
            var fullUrl = baseUrl + link.attr("href");
            var future = executor.submit(new UrlConverter(fullUrl));
            futureList.add(future);
        }
        for (var future :
                futureList) {
            try {
                resultLinks.add(future.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        executor.shutdown();
        return resultLinks;
    }

    private String getNextPageUrl(Document doc) {
        var selector = "a#sogou_next";
        var nextPageTag = doc.select(selector).first();
        if (nextPageTag == null) {
            return null;
        }
        return nextPageBaseUrl + nextPageTag.attr("href");
    }

    private ArrayList<String> getAllItemLink(String keyword, int maxPage) {
        var urlList = new ArrayList<String>();
        Document searchPage;
        int currentPage = 1;
        try {
            searchPage = getSearchPage(keyword);
        } catch (IOException exception) {
            return urlList;
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
            } catch (IOException e) {
                e.printStackTrace();
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
            var extractor = PageExtractor.getExtractor(link);
            if (extractor == null) continue;
            var future = executor.submit(extractor);
            futureList.add(future);
        }
        for (var future :
                futureList) {
            try {
                var result = future.get();
                if (result != null) {
                    resultRecord.add(result);
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        executor.shutdown();
        return resultRecord;
    }


    static class UrlConverter implements Callable<String> {
        private final String url;

        public UrlConverter(String url) {
            this.url = url;
        }

        private String extractRedirectUrl(String scriptStr) {
            var r = Pattern.compile("window\\.location\\.replace\\(\"(.*)\"\\)");
            var matcher = r.matcher(scriptStr);
            if (!matcher.find()) {
                return null;
            }
            return matcher.group(1);
        }

        @Override
        public String call() throws Exception {
            var pageDoc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.61 Safari/537.36")
                    .timeout(3000)
                    .get();
            var scriptStr = pageDoc.select("script").html();
            return extractRedirectUrl(scriptStr);
        }
    }
}
