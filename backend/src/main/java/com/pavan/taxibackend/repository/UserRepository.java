package com.pavan.taxibackend.repository;

import com.pavan.taxibackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>{

}
