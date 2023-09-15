package com.fintech.globalBank.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fintech.globalBank.Entity.User;

@Repository
public interface UserRepo extends JpaRepository<User, Long>{
    Boolean existsByEmail(String email);
}
