package com.luana.videolocadora.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.luana.videolocadora.models.User;

public interface UsersRepository extends JpaRepository<User, String>{
	
	User findByUsername(String username);
}
