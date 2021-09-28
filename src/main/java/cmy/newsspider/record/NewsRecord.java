package cmy.newsspider.record;

import com.fasterxml.jackson.annotation.JsonProperty;

public record NewsRecord(
        @JsonProperty("title")
        String title,
        @JsonProperty("content")
        String content,
        @JsonProperty("source")
        String source,
        @JsonProperty("time")
        String time) {
}
