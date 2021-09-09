package com.education.eduprime.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListUserDto {
    @JsonProperty("username")
    private String userName;

    @JsonProperty("age")
    private Integer age;

    @JsonProperty("address")
    private String address;
}
