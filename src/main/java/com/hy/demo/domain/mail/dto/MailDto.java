package com.hy.demo.domain.mail.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MailDto {

    private String address;
    private String title;
    private String message;

}
