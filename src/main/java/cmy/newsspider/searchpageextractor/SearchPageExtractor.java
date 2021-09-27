package cmy.newsspider.searchpageextractor;

import cmy.newsspider.record.NewsRecord;

import java.util.ArrayList;


public interface SearchPageExtractor {
    ArrayList<NewsRecord> searchByKeyword(String keyword, int maxPage);
}
