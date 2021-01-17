package com.pmap.voter.models;

public class VoterRegisterInput {
    public String voter_name;
    public String  voter_state  ;
    public String voter_mobile_number;
    public String voter_aadhar	;
    public String  voter_district;
    public String voter_taluk;
    public String voter_el_id	;
    public String voter_password;

    public VoterRegisterInput(String voter_name, String voter_state, String voter_mobile_number, String voter_aadhar, String voter_district, String voter_taluk, String voter_el_id, String voter_password) {
        this.voter_name = voter_name;
        this.voter_state = voter_state;
        this.voter_mobile_number = voter_mobile_number;
        this.voter_aadhar = voter_aadhar;
        this.voter_district = voter_district;
        this.voter_taluk = voter_taluk;
        this.voter_el_id = voter_el_id;
        this.voter_password = voter_password;
    }
}
