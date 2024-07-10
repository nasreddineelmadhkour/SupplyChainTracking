package com.pgsintl.supplychaintracking.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class ChatMessage {
    String message;
    String user;
    Date date = new Date();
}
