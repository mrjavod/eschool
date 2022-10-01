package com.home.eschool.controller;

import com.home.eschool.entity.States;
import com.home.eschool.services.StateService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/states")
@Tag(name = "states")
public class StateController {

    private final StateService stateService;

    public StateController(StateService stateService) {
        this.stateService = stateService;
    }

    @GetMapping("/")
    public List<States> rolesList() {
        return stateService.getStatesList();
    }
}
