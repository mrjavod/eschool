package com.home.eschool.services;

import com.home.eschool.entity.States;
import com.home.eschool.entity.enums.StateEnum;
import com.home.eschool.repository.StateRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class StateService {

    private final StateRepo stateRepo;

    public StateService(StateRepo stateRepo) {
        this.stateRepo = stateRepo;
    }

    public List<States> generateDefaultStates() {
        if (stateRepo.count() == 0) {
            List<States> list = new ArrayList<>();
            list.add(new States(UUID.randomUUID(), "Deleted", StateEnum.DELETED));
            list.add(new States(UUID.randomUUID(), "Active", StateEnum.ACTIVE));

            return stateRepo.saveAll(list);
        }
        return new ArrayList<>();
    }

    public List<States> getStatesList() {
        return stateRepo.findAll();
    }

    public States getStateByLabel(StateEnum label) {
        return stateRepo.findStateByLabel(label);
    }
}
