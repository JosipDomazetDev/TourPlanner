package com.example.tourplanner.service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Person {

    private Long id;
    private String name;
    private Boolean isEmployed;


}
