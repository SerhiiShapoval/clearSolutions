package com.shapoval.clearsolution.model;

import jakarta.persistence.Embeddable;
import lombok.*;

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
