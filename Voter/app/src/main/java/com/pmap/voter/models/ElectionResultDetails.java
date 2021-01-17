package com.pmap.voter.models;

import java.security.PublicKey;

public class ElectionResultDetails {
    public String er_id;
    public String er_count;
    public String er_candidates_id;
    public String candidates_name;
    public String  candidates_photo;

    public ElectionResultDetails(String er_id, String er_count, String er_candidates_id, String candidates_name,String candidates_photo) {
        this.er_id = er_id;
        this.er_count = er_count;
        this.er_candidates_id = er_candidates_id;
        this.candidates_name = candidates_name;
        this.candidates_photo = candidates_photo;
    }

    public String getEr_id() {
        return er_id;
    }

    public String getEr_count() {
        return er_count;
    }

    public String getEr_candidates_id() {
        return er_candidates_id;
    }

    public String getCandidates_name() {
        return candidates_name;
    }

    public void setEr_id(String er_id) {
        this.er_id = er_id;
    }

    public void setEr_count(String er_count) {
        this.er_count = er_count;
    }

    public void setEr_candidates_id(String er_candidates_id) {
        this.er_candidates_id = er_candidates_id;
    }

    public void setCandidates_name(String candidates_name) {
        this.candidates_name = candidates_name;
    }
}
