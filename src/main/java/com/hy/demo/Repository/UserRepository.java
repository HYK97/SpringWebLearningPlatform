package com.hy.demo.Repository;

import com.hy.demo.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer> {

    //findBy규칙 -> Username문법
    User findByUsername(String username);
}
