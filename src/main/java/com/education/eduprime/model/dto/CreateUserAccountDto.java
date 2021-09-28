package com.education.eduprime.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserAccountDto {
    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("balances")
    private Integer balances;
}
