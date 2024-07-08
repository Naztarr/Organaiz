package com.naz.organaiz.configuration;

import com.naz.organaiz.exception.OrganaizException;
import com.naz.organaiz.service.serviceImplementation.JwtImplementation;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Configuration
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final UserDetailsService userDetailsService;
    private final JwtImplementation jwtImplementation;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain
    ) throws ServletException, IOException {
        String header = request.getHeader("Authorization");

        if(header == null || !header.startsWith("Bearer")){
            filterChain.doFilter(request, response);
            return;
        }

        String jwtToken = header.substring(7);

        try{
            if(jwtImplementation.isExpired(jwtToken)){
                throw new OrganaizException("Your session has expired. Please login");
            }
            String email = jwtImplementation.extractEmailAddressFromToken(jwtToken);
            if(email != null && SecurityContextHolder.getContext().getAuthentication() == null){
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                        this.userDetailsService.loadUserByUsername(email), null, Collections.emptyList()
                ) ;
                token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(token);
            }
        } catch (Exception e){
            response.sendError(1,"Your session has expired. Please login");
            request.setAttribute("token_error", "Your session has expired. Please login");
            return;
        }
        filterChain.doFilter(request, response);
    }
}