package com.home.eschool.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;
import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@MappedSuperclass
class BaseEntity {

    @Id
    private UUID id;
    private Timestamp createDate;
    private Timestamp changeDate;

    @OneToOne(cascade = CascadeType.ALL)
    private Users createUser;

    @OneToOne
    private Users changeUser;
}
