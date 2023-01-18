package com.dfrolod.tindergeo.dto;


import com.fasterxml.jackson.annotation.JsonProperty;

public class MatchProfileResult {
    private int status;
    private MatchProfile results;



    public int getStatus(){
        return status;
    }
    public MatchProfile getResults(){
        return results;
    }

    public class MatchProfile {
        @JsonProperty("distance_mi")
        private int distanceMi;
        private String name;

        public int getDistanceMi() {
            return distanceMi;
        }

        public String getName(){
            return name;
        }
    }
}
