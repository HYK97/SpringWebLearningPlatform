package com.hy.demo.domain.user.repository;

import com.hy.demo.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    //findBy규칙 -> Username문법
    User findByUsername(String username);

    void deleteById(Long id);

    List<User> findByRole(String role);

    Long countByNickname(String nickname);

    Long countByNicknameIsAndUsernameIsNot(String nickname, String username);
}
