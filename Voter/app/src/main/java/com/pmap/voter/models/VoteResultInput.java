package com.pmap.voter.models;

public class VoteResultInput {
    public String voter_el_id;
    public String candidates_id;

    public VoteResultInput(String voter_el_id, String candidates_id, String voter_id) {
        this.voter_el_id = voter_el_id;
        this.candidates_id = candidates_id;
        this.voter_id = voter_id;
    }

    public String voter_id;

}
