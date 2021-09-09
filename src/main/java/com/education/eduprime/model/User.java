package com.education.eduprime.model;

import com.education.eduprime.utils.AuditModel;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User extends AuditModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username")
    @JsonProperty("username")
    private String userName;

    @Column(name = "age")
    private Integer age;

    @Column(name = "address")
    private String address;
}
