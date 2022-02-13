package com.hy.demo.Domain.User.Repository;

import com.hy.demo.Domain.User.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer> {

    //findBy규칙 -> Username문법
    User findByUsername(String username);
}
