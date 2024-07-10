package com.pgsintl.supplychaintracking.Dto;

import lombok.*;

import java.util.Date;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class ChatMessage {
    String message;
    String user;
    Date date = new Date();
}
