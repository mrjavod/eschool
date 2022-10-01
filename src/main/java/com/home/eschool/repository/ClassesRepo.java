package com.home.eschool.repository;

import com.home.eschool.entity.Classes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ClassesRepo extends JpaRepository<Classes, UUID> {
}
