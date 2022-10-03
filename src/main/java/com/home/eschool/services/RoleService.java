package com.home.eschool.services;

import com.home.eschool.entity.Roles;
import com.home.eschool.entity.enums.RoleEnum;
import com.home.eschool.repository.RoleRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class RoleService {

    private final RoleRepo roleRepo;

    public RoleService(RoleRepo roleRepo) {
        this.roleRepo = roleRepo;
    }

    public List<Roles> generateRoles() {
        List<Roles> list = new ArrayList<>();
        list.add(new Roles(UUID.randomUUID(), "Administrator", RoleEnum.ROLE_ADMIN));
        list.add(new Roles(UUID.randomUUID(), "Teacher", RoleEnum.ROLE_TEACHER));

        return roleRepo.saveAll(list);
    }

    public List<Roles> getRolesList() {
        return roleRepo.findAll();
    }

    public Roles getRoleByLabel(RoleEnum label) {
        return roleRepo.getRolesByLabel(label);
    }
}
