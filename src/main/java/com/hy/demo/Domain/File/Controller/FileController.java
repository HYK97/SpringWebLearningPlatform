package com.hy.demo.Domain.File.Controller;

import com.hy.demo.Domain.File.Service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.AccessDeniedException;
import java.util.Map;

;

@Controller
@RequestMapping("/file/*")
@RequiredArgsConstructor
@Slf4j
public class FileController {

    private final FileService fileService;


    @GetMapping("/download/{courseId}/{courseBoardId}/{fileId}")
    @PreAuthorize("@authorizationChecker.isFile(#fileId,#courseId,#courseBoardId)")
    public ResponseEntity<Object> BoardManagement(@PathVariable Long fileId, @PathVariable Long courseId, @PathVariable Long courseBoardId) throws IOException {

        Map<String, Object> map = fileService.fileDownLoad(fileId);
        File file = (File) map.get("file");
        Resource resource = (Resource) map.get("resource");
        String filename = (String) map.get("fileName");
        log.debug("filename = {}", filename);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(ContentDisposition.builder("attachment").filename(URLEncoder.encode(filename, String.valueOf(StandardCharsets.UTF_8))).build());  // 다운로드 되거나 로컬에 저장되는 용도로 쓰이는지를 알려주는 헤더

        return new ResponseEntity<Object>(resource, headers, HttpStatus.OK);
    }


    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException e, HttpServletRequest request, HttpServletResponse response) throws Exception {

        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<?> handleFileNotFoundException(FileNotFoundException e, HttpServletRequest request, HttpServletResponse response) throws Exception {

        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<?> handleIOExceptionException(FileNotFoundException e, HttpServletRequest request, HttpServletResponse response) throws Exception {

        return ResponseEntity.badRequest().build();
    }


}

