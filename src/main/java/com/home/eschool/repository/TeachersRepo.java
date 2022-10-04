package com.home.eschool.repository;

import com.home.eschool.entity.Teachers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TeachersRepo extends JpaRepository<Teachers, UUID> {

    Page<Teachers> findAllByFirstNameContains(Pageable pageable, String search);
}
