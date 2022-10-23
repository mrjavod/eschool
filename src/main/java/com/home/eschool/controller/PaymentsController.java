package com.home.eschool.controller;

import com.home.eschool.models.dto.PaymentsDto;
import com.home.eschool.models.payload.PageablePayload;
import com.home.eschool.models.payload.PaymentsPayloadDetails;
import com.home.eschool.models.payload.PaymentsStatsPayload;
import com.home.eschool.services.PaymentsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payments")
@Tag(name = "Payments", description = "To'lovlar")
public class PaymentsController {

    private final PaymentsService paymentsService;

    public PaymentsController(PaymentsService paymentsService) {
        this.paymentsService = paymentsService;
    }

    @GetMapping("/")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public PageablePayload getAll(@RequestParam(required = false, name = "page", defaultValue = "0") int page,
                                  @RequestParam(required = false, name = "cdate", defaultValue = "") String cdate,
                                  @RequestParam(required = false, name = "name", defaultValue = "") String name) {
        return paymentsService.getAll(page, cdate, name);
    }

    @GetMapping("/getById/{id}")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public PaymentsPayloadDetails getById(@PathVariable("id") UUID id) {
        return paymentsService.getById(id);
    }

    @PostMapping("/create")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    @Secured("ROLE_ADMIN")
    public void create(@Valid @RequestBody PaymentsDto paymentsDto) {
        paymentsService.create(paymentsDto);
    }

    @PostMapping("/delete")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    @Secured("ROLE_ADMIN")
    public void delete(@RequestBody List<UUID> payments) {
        paymentsService.delete(payments);
    }

    @GetMapping("/stats")
    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    public PaymentsStatsPayload getStats() {
        return paymentsService.getStats();
    }
}
