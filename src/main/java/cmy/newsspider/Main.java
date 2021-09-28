package cmy.newsspider;

import cmy.newsspider.searchpageextractor.SearchPageExtractor;
import cmy.newsspider.searchpageextractor.SogouSearchPageExtractor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) throws IOException {

        SearchPageExtractor spe = new SogouSearchPageExtractor();
        var keyword = "限电";
        var result = spe.searchByKeyword(keyword, 20);
        var mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(Paths.get(String.format("%s-result.json", keyword)).toFile(), result);
    }
}
