package com.hy.demo.Domain.Mail.Dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MailDto {

    private String address;
    private String title;
    private String message;

}
