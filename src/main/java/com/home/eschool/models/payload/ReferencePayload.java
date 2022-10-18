package com.home.eschool.models.payload;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ReferencePayload {

    private final UUID id;
    private final String name;

    public ReferencePayload(UUID id, String name) {
        this.id = id;
        this.name = name;
    }
}
