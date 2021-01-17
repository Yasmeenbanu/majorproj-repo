package com.pmap.voter.models;

public class CandidateRegisterInput {
    public String candidates_name;
    public String candidates_mobile_number;
    public String candidates_aadhar;
    public String candidates_state;
    public String candidates_district;
    public String candidates_taluk;
    public String candidates_party;
    public String candidates_el_id;
    public String candidates_photo;

    public CandidateRegisterInput(String candidates_name,String candidates_mobile_number,String candidates_aadhar,String candidates_state,String candidates_district,String candidates_taluk,String candidates_party,String candidates_el_id,String candidates_photo) {
        this.candidates_name = candidates_name;
        this.candidates_mobile_number = candidates_mobile_number;
        this.candidates_aadhar = candidates_aadhar;
        this.candidates_state = candidates_state;
        this.candidates_district = candidates_district;
        this.candidates_taluk = candidates_taluk;
        this.candidates_party = candidates_party;
        this.candidates_el_id = candidates_el_id;
        this.candidates_photo = candidates_photo;
    }
}
