package com.education.eduprime.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class DetailUserDto implements Serializable {
    @JsonProperty("username")
    private String userName;

    @JsonProperty("age")
    private Integer age;

    @JsonProperty("address")
    private String address;
}
