package com.home.eschool.repository;

import com.home.eschool.entity.Users;
import com.home.eschool.entity.enums.RoleEnum;
import com.home.eschool.entity.enums.StateEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepo extends JpaRepository<Users, UUID> {

    List<Users> findAllByRole_Label(RoleEnum role);

    Optional<Users> findByIdAndStateLabel(UUID id, StateEnum state);

    Optional<Users> findByLoginAndStateLabel(String login, StateEnum state);

}
