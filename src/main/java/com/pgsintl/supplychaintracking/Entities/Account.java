package com.pgsintl.supplychaintracking.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.*;

@Data
@Entity
@Table(name = "account")
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Account implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long userNumber;
    String name;
  @Lob
  byte[] photo;
  String namePhoto;

  String typePhoto;

    String phoneNumber;

    @JsonIgnore
    String codeTel = "";
    @JsonIgnore
    String resetToken= "";


    String email;
    String password;



    @JsonIgnore
    @Enumerated(EnumType.STRING)
    Role role;



    // setings of account
    @JsonIgnore
    Boolean isAccountNonLocked=true;
    @JsonIgnore
    Boolean isAccountNonExpired=true;
    @JsonIgnore
    Boolean isCredentialsNonExpired=true;
    @JsonIgnore
    Boolean isEnabled=false;
    @JsonIgnore
    String activateCode ="";

    @JsonIgnore
    Date datecreation;


    @JsonIgnore
    // conducteur avec commande
    @OneToMany (mappedBy = "driver")
    private List<Orders> ordersDriver = new ArrayList<>();
    @JsonIgnore
    // transporteur avec commande
    @OneToMany (mappedBy = "carrier")
    private List<Orders> ordersCarrier;



    @JsonIgnore
    @OneToMany
    @JoinColumn(name = "carrierNumber")
    private List<Account> drivers = new ArrayList<>();



    String serialNumber=null;
    String cardNumber=null;



}
