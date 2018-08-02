package com.howtographql.sampl.hackernewsgraphqljava.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LinkFilter {
    @JsonProperty("description_contains")
    private String descriptionContains;
    @JsonProperty("url_contains")
    private String urlContains;
}
