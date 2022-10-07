package com.home.eschool.entity;

import com.home.eschool.entity.enums.SetsEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppSettings extends BaseEntity {

    @Column(unique = true)
    @Enumerated
    private SetsEnum label;
    private String name;
    private UUID key;
    private String value;
}
