package com.pmap.voter.models;

public class ElectionListDetails {

        public String el_id;
        public String el_name;


    public ElectionListDetails(String el_id, String el_name) {
        this.el_id = el_id;
        this.el_name = el_name;
    }
    public String getEl_id() {
        return el_id;
    }

    public String getEl_name() {
        return el_name;
    }

    public void setEl_id(String el_id) {
        this.el_id = el_id;
    }

    public void setEl_name(String el_name) {
        this.el_name = el_name;
    }
}


