package com.telino.limsdataextractor.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class LIMSReponseBean {

    public static class Link {
        @JsonProperty("$ref")
        public String url;
    }

    private Link next;
    private List<LIMSAnalyseBean> analyses =  new ArrayList<>();

    public boolean hasNext() {
        return analyses.isEmpty() == false && getNextUrl() != null;
    }

    @JsonIgnore
    public String getNextUrl() {
        return next != null
                ? next.url
                : null;
    }

    public Link getNext() {
        return next;
    }

    public void setNext(Link next) {
        this.next = next;
    }

    @JsonProperty("items")
    public List<LIMSAnalyseBean> getAnalyses() {
        return analyses;
    }

    public void setAnalyses(List<LIMSAnalyseBean> analyses) {
        this.analyses = analyses;
    }
}