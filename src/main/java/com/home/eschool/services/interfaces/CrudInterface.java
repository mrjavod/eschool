package com.home.eschool.services.interfaces;

import com.home.eschool.models.payload.PageablePayload;

import java.util.List;
import java.util.UUID;

public interface CrudInterface<Dto, Payload> {

    void create(Dto t);

    void update(Dto t);

    void delete(List<UUID> ids);

    PageablePayload getAll(int page, String search);

    Payload getById(UUID id);
}
