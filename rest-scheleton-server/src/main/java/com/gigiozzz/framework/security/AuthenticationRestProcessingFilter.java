package com.gigiozzz.framework.security;

import java.io.IOException;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.GenericFilterBean;


public class AuthenticationRestProcessingFilter extends GenericFilterBean {

  
    AuthenticationManager authManager;

    public AuthenticationRestProcessingFilter(AuthenticationManager authManager) {
        this.authManager = authManager;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        @SuppressWarnings("unchecked")
        Map<String, String[]> parms = request.getParameterMap();

        if(parms.containsKey("username") && parms.containsKey("password")) {
            String username = parms.get("username")[0]; // grab the username
            String password = parms.get("password")[0]; // grab the password

           		
                UsernamePasswordAuthenticationToken authentication = 
                        new UsernamePasswordAuthenticationToken(username, password);

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails((HttpServletRequest) request));
                // set the authentication into the SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authManager.authenticate(authentication));         
        
            

        }
        // continue thru the filter chain
        chain.doFilter(request, response);
    }
}
