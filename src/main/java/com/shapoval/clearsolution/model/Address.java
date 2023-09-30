package com.shapoval.clearsolution.model;


import lombok.*;

import javax.persistence.Embeddable;

@Embeddable
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Address {

    private String country;
    private String city;
    private String street;
    private String streetNumber;
    private String apartment;
    private String zip;


}
