package com.education.eduprime.model;

import com.education.eduprime.utils.AuditModel;
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

    @Column(name = "name")
    private String userName;

    @Column(name = "age")
    private Integer age;

    @Column(name = "address")
    private String address;
}
