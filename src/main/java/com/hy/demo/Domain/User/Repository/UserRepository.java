package com.hy.demo.Domain.User.Repository;

import com.hy.demo.Domain.User.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User,Long> {

    //findBy규칙 -> Username문법
    User findByUsername(String username);
    void deleteById(Long id);
    List<User> findByRole(String role);
}
