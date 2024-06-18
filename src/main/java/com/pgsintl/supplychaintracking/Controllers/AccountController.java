package com.pgsintl.supplychaintracking.Controllers;

import com.pgsintl.supplychaintracking.Dto.AccountLoginDto;
import com.pgsintl.supplychaintracking.Entities.Account;
import com.pgsintl.supplychaintracking.Entities.AuthRequest;
import com.pgsintl.supplychaintracking.Services.AccountIService;
import com.pgsintl.supplychaintracking.Services.AccountSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/account")
public class AccountController {


    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private AccountIService accountIService;

    @Autowired
    AccountSecurityService accountSecurityService;

    @PostMapping("/login")
    public AccountLoginDto login(@RequestBody AuthRequest authRequest){
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if(authenticate.isAuthenticated()){
            return accountSecurityService.loginSucces(authRequest.getUsername());

           // return jwtService.generateToken(authRequest.getUsername());
        }else {
            throw new UsernameNotFoundException("Invalid user request");
        }
    }



    @PostMapping("/addCarrier")
    public Account addCarrier(@RequestBody Account carrier) {
        return accountIService.CreatAccountCarrier(carrier);
    }



    @PostMapping("/addDriver/{idCarrier}")
    @PreAuthorize("hasAuthority('CARRIER')")
    public Account addDriver(@PathVariable Long idCarrier
            ,@RequestParam("image") MultipartFile file
            ,@RequestParam("name") String name
            ,@RequestParam("phoneNumber") String phoneNumber
            ,@RequestParam("email") String email
            ,@RequestParam("password") String password
            ,@RequestParam("serialNumber") String serialNumber
            ,@RequestParam("cardNumber") String cardNumber
    ) throws IOException{



            return accountIService.CreatAccountDriver( name,  password ,  email,  cardNumber ,  serialNumber ,  phoneNumber,  file, idCarrier) ;
    }


    @PostMapping("/updateProfile/{idAccount}")
    public Account updateProfile(@PathVariable Long idAccount
            ,@RequestParam("image") MultipartFile file
            ,@RequestParam("name") String name
            ,@RequestParam("phoneNumber") String phoneNumber
            ,@RequestParam("email") String email
            ,@RequestParam("password") String password
            ,@RequestParam("isName") String isName
            ,@RequestParam("isEmail") String isEmail
            ,@RequestParam("isPhone") String isPhone
            ,@RequestParam("isPassword") String isPassword
            ,@RequestParam("isPhoto") String isPhoto


    ) throws IOException{

        return accountIService.updateProfile(idAccount,file,name,phoneNumber,email,password,isEmail,isPhone,isPassword,isPhoto,isName);

    }



    @PostMapping("/deleteDriver/{idDriver}")
    @PreAuthorize("hasAuthority('CARRIER')")
    public boolean deleteDriver(@PathVariable Long idDriver){
        return accountIService.deleteDriver(idDriver);
    }


    @GetMapping("/driversByCarrier/{idCarrier}")
    //@PreAuthorize("hasAuthority('CARRIER') and hasAuthority('OTHER_AUTHORITY') and hasAuthority('ANOTHER_AUTHORITY')")
    @PreAuthorize("hasAuthority('CARRIER')")
    public List<Account> getDriversByCarrier(@PathVariable Long idCarrier)
    {
        return accountIService.getAllDriverByCarrier(idCarrier);
    }

    @GetMapping("/admin")
    public List<AccountLoginDto> getAllAccount(){
        return accountIService.getAllUser();
    }


    @PostMapping("/SetAllNoPDP")
    public boolean SetAllNoPDP(@RequestParam("image") MultipartFile file) throws IOException {

        return accountIService.SetAllNoPDP(file);

    }




    // *************************** RESET PASSWORD WITH TWILIO ****************************************
    @GetMapping("/resetpassword/SendCodeReset/{identity}")
    public boolean SendCodeReset(@PathVariable String identity){
        return accountIService.SendCodeReset(identity);
    }

    @GetMapping("/resetpassword/verifyCode/{code}/{identity}")
    public boolean verifyCode(@PathVariable String code ,@PathVariable String identity) {
        return accountIService.verifyCode(code,identity);
    }

    @GetMapping("/resetpassword/ChangePasswordAfterVerification/{newPassword}/{identity}")
    public boolean ChangePasswordAfterVerification(@PathVariable String newPassword ,@PathVariable String identity){
        return accountIService.ChangePasswordAfterVerification(newPassword,identity);
    }



}
