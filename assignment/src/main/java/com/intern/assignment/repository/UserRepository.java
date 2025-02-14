package com.intern.assignment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.intern.assignment.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, String>{

}
