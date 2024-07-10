package com.pgsintl.supplychaintracking.Filter;

import com.pgsintl.supplychaintracking.Dto.AccountDetails;
import com.pgsintl.supplychaintracking.Services.AccountSecurityService;
import com.pgsintl.supplychaintracking.Services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component @AllArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private JwtService jwtService;
    private AccountSecurityService accountSecurityService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String token= null;
        String userName = null;
        if(authHeader !=null && authHeader.startsWith("Bearer")){
            token = authHeader.substring(7);
            userName =jwtService.extractUserName(token);
        }
        if(userName !=null && SecurityContextHolder.getContext().getAuthentication()==null){
            AccountDetails accountDetails = accountSecurityService.loadUserByUsername(userName);
            if(jwtService.validateToken(token,accountDetails)){
                UsernamePasswordAuthenticationToken authToken =  new UsernamePasswordAuthenticationToken(accountDetails,null,accountDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request,response);
    }




}