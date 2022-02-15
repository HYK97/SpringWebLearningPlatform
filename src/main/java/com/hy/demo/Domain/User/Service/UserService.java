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


    public void register(User user,User provider) {


        if (provider == null) {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            logger.info("user.getPassword() = " + user.getPassword());
            userRepository.save(user);
        } else {
            User user2 = User.builder()
                    .username(provider.getUsername())
                    .password(provider.getPassword())
                    .email(provider.getEmail())
                    .role(user.getRole())
                    .provider(provider.getProvider())
                    .providerId(provider.getProviderId())
                    .build();
            logger.info("user.getPassword() = " + user2.getPassword());
            userRepository.save(user2);
        }





    }

}
