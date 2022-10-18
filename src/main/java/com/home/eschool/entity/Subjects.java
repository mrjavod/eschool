package com.home.eschool.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Subjects extends BaseEntity {

    private String name;

    @OneToOne(cascade = CascadeType.ALL)
    private Languages lang;

    @OneToOne
    private States state;
}
