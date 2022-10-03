package com.home.eschool.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
@Getter
@Setter
public class Users extends BaseEntity {

    private String fullName;

    @Column(unique = true)
    private String login;
    private String password;

    @OneToOne(cascade = CascadeType.ALL)
    private Roles role;

    @OneToOne
    private States state;
}
