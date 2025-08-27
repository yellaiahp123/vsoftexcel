package com.vsoftexcel.vsoftexcel.repository;

import java.util.List;

import com.vsoftexcel.vsoftexcel.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    boolean existsByTitle(String title);

}