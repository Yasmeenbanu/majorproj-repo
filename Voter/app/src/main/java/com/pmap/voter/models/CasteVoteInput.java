package com.pmap.voter.models;

public class CasteVoteInput {
    public String el_id;
    public String  voter_id  ;
    public String candidate_id;

    public CasteVoteInput(String el_id, String voter_id, String candidate_id) {
        this.el_id = el_id;
        this.voter_id = voter_id;
        this.candidate_id = candidate_id;
    }

}
