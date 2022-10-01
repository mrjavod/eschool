package com.home.eschool.entity;

import com.home.eschool.entity.enums.LangEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Languages {

    @Id
    private UUID id;
    private String name;

    @Enumerated
    private LangEnum label;

    public Languages(LangEnum label) {
        this.label = label;
    }
}
