package com.vsoftexcel.vsoftexcel.repository;

import java.util.Optional;

import com.vsoftexcel.vsoftexcel.entity.Auth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AuthRepository extends JpaRepository<Auth, Long> {
    Optional<Auth> findByEmail(String email);
}
