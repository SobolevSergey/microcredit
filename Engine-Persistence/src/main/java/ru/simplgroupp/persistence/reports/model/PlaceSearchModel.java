package ru.simplgroupp.persistence.reports.model;

/**
 *
 */
    public class PlaceSearchModel {
        public PlaceSearchModel(String place, String placeName) {
            this.place = place;
            this.placeName = placeName;
        }

        private String place;
        private String placeName;
        private boolean city;

        public String getPlace() {
            return place;
        }

        public void setPlace(String place) {
            this.place = place;
        }

        public String getPlaceName() {
            return placeName;
        }

        public void setPlaceName(String placeName) {
            this.placeName = placeName;
        }

        public boolean isCity() {
            return city;
        }

        public void setCity(boolean city) {
            this.city = city;
        }
    }

