package cmy.newsspider;

import cmy.newsspider.searchpageextractor.SearchPageExtractor;
import cmy.newsspider.searchpageextractor.SogouSearchPageExtractor;

public class Main {
    public static void main(String[] args) {


        SearchPageExtractor spe = new SogouSearchPageExtractor();
        var result = spe.searchByKeyword("限电", 20);
        System.out.println(result);
    }
}
