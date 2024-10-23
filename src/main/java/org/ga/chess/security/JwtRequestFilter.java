package org.ga.chess.security;

import ch.qos.logback.core.util.StringUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private MyUserDetailsService userDetailService;

    @Autowired
    private JwtUtil jwtUtil;

    private String parseJet(HttpServletRequest request){
        String headerAuth= request.getHeader("Authorization");
        if(StringUtil.notNullNorEmpty(headerAuth) && headerAuth.startsWith("Bearer ")){
            return headerAuth.split(" ")[1];
        }
        return null;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String jwt=parseJet(request);
            if(jwt!=null && jwtUtil.validateToken(jwt)){
                String username= jwtUtil.getUserNameFromToken(jwt);
                UserDetails userDetails=userDetailService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken
                        (userDetails,null,userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }catch (Exception e){
            logger.error("cannot set user authentication: {}",e);
        }

        filterChain.doFilter(request,response);
    }
}
