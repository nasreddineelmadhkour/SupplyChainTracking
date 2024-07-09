package com.pgsintl.supplychaintracking.dto;

import com.pgsintl.supplychaintracking.entities.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter @Setter
public class AccountLoginDto {


    Long userNumber;
    String name;
    String token;

    byte[] photo;
    String phoneNumber;
    String codeTel;
    String resetToken;
    String email;
    String password;

    @Enumerated(EnumType.STRING)
    Role role;
    Boolean isAccountNonLocked;
    Boolean isAccountNonExpired;
    Boolean isCredentialsNonExpired;
    Boolean isEnabled;
    String activateCode;
    Date datecreation;
    String serialNumber=null;
    String cardNumber=null;

}
