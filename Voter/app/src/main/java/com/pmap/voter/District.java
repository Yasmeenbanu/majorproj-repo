package com.pmap.voter;


    public class District implements Comparable<District> {

        private int districtID;
        private State state;
        private String districtName;

        public District(int districtID, State state, String districtName) {
            this.districtID = districtID;
            this.state = state;
            this.districtName = districtName;
        }

        public int getDistrictID() {
            return districtID;
        }

        public State getState() {
            return state;
        }

        public String getDistrictName() {
            return districtName;
        }

        @Override
        public String toString() {
            return districtName;
        }

        @Override
        public int compareTo(District another) {
            return this.getDistrictID() - another.getDistrictID();//ascending order
//            return another.getStateID()-this.getStateID();//descending order
        }
    }



