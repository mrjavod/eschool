package com.home.eschool.repository;

import com.home.eschool.entity.Subjects;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SubjectsRepo extends JpaRepository<Subjects, UUID> {
}
