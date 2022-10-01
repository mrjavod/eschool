package com.home.eschool.repository;

import com.home.eschool.entity.Roles;
import com.home.eschool.entity.enums.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RoleRepo extends JpaRepository<Roles, UUID> {

    Roles getRolesByLabel(RoleEnum label);

}
