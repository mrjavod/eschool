package com.home.eschool.repository;

import com.home.eschool.entity.States;
import com.home.eschool.entity.enums.StateEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StateRepo extends JpaRepository<States, UUID> {

    States findStateByLabel(StateEnum label);

}
