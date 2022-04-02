package com.hy.demo.Domain.Course.Controller;

import com.hy.demo.Config.Auth.PrincipalDetails;
import com.hy.demo.Domain.Course.Entity.SummerNoteImage;
import com.hy.demo.Domain.Course.Service.ImageService;
import com.hy.demo.Domain.User.Entity.User;
import com.hy.demo.Domain.User.Service.UserService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;


@Controller

public class ImageController {

    @Autowired
    ImageService imageService;
    @Autowired
    UserService userService;

    @Autowired
    ResourceLoader resourceLoader;

    @Autowired
    Logger logger;


    @PostMapping("/image")
    public ResponseEntity<?> imageUpload(MultipartFile file) {
        try {
            SummerNoteImage uploadFile = imageService.store(file);
            return ResponseEntity.ok().body("/image/" + uploadFile.getId());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/profileUpdate")
    @ResponseBody
    public String profileUpdate(MultipartFile file, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        try {
            User findUser = userService.findByUsername(principalDetails.getUser());
            imageService.deleteImage(findUser.getProfileImage());
            SummerNoteImage uploadFile = imageService.store(file);
            userService.updateUserProfileImage("/image/" + uploadFile.getId(), principalDetails.getUser());
            return "/image/" + uploadFile.getId();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    @GetMapping("/image/{fileId}")
    public ResponseEntity<?> serveFile(@PathVariable Long fileId) {
        try {
            SummerNoteImage uploadFile = imageService.load(fileId);
            Resource resource = resourceLoader.getResource("file:" + uploadFile.getFilePath());
            return ResponseEntity.ok().body(resource);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }


}
