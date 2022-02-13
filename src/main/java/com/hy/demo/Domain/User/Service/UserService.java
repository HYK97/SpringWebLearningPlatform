package com.hy.demo.Domain.User.Service;

import com.hy.demo.Domain.User.Entity.User;
import com.hy.demo.Domain.User.Repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    public void register(User user) {
        user.builder()
                .password(bCryptPasswordEncoder.encode(user.getPassword()))
                .role("ROLE_USER").build();
        logger.info("user.toString() = " + user.toString());

        userRepository.save(user);
    }
}
