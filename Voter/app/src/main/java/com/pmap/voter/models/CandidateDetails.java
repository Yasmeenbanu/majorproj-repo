package com.pmap.voter.models;

public class CandidateDetails {
    public String candidates_id;
    public String candidates_photo;
    public String  candidates_party;


    public CandidateDetails(String candidates_id, String candidates_photo, String candidates_party) {
        this.candidates_id = candidates_id;
        this.candidates_photo = candidates_photo;
        this.candidates_party=candidates_party;
    }

    public String getCandidates_id() {
        return candidates_id;
    }

    public String getCandidates_photo() {
        return candidates_photo;
    }

    public void setCandidates_id(String candidates_id) {
        this.candidates_id = candidates_id;
    }

    public void setCandidates_photo(String candidates_photo) {
        this.candidates_photo = candidates_photo;
    }
    public String getCandidates_party() {
        return candidates_party;
    }

    public void setCandidates_party(String candidates_party) {
        this.candidates_party = candidates_party;
    }
}
