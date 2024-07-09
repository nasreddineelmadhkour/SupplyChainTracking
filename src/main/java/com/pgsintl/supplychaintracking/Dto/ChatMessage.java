package com.pgsintl.supplychaintracking.dto;

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
