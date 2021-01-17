package com.pmap.voter.models;

public class ViewResultDetails {

        public String candidates_id;
        public String  	candidates_el_id;


    public ViewResultDetails(String candidates_id, String 	candidates_el_id) {
        this.candidates_id = candidates_id;
        this.candidates_el_id = candidates_el_id;
    }

    public String getCandidates_id() {
            return candidates_id;
        }


        public void setCandidates_id(String candidates_id) {
            this.candidates_id = candidates_id;
        }





    }


