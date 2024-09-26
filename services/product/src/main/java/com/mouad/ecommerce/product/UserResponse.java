package com.mouad.ecommerce.product;

import lombok.Data;
import java.io.Serializable;

@Data
public class UserResponse implements Serializable {
    private Integer id;
    private String name;
    private String username;
    private String email;
    private Address address;
    private String phone;
    private String website;
    private Company company;

    @Data
    public static class Address implements Serializable {
        private String street;
        private String suite;
        private String city;
        private String zipcode;
        private Geo geo;

        @Data
        public static class Geo implements Serializable {
            private String lat;
            private String lng;
        }
    }

    @Data
    public static class Company implements Serializable {
        private String name;
        private String catchPhrase;
        private String bs;
    }
}
