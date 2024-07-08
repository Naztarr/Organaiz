package com.naz.organaiz.configuration;

import com.naz.organaiz.exception.OrganaizException;
import com.naz.organaiz.service.serviceImplementation.JwtImplementation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import java.util.Collections;


/**
 * Configuration class to handle logout functionality.
 */
@Configuration
@RequiredArgsConstructor
public class LogoutConfiguration implements LogoutHandler {
    private final UserDetailsService userDetailsService;
    private final JwtImplementation jwtImplementation;

    /**
     * Logout handler method.
     *
     * @param request        The HTTP servlet request.
     * @param response       The HTTP servlet response.
     * @param authentication The authentication object.
     */
    @Override
    public void logout(HttpServletRequest request,
                       HttpServletResponse response, Authentication authentication) {
        String header = request.getHeader("Authorization");

        if(header == null || !header.startsWith("Bearer")){
            return;
        }
        String jwToken = header.substring(7);

        if(!jwtImplementation.isExpired(jwToken)){
            // Invalidate the current session
            request.getSession().invalidate();
            // Clear the authentication token
            response.setHeader("Authorization", "");
        } else {
            throw new OrganaizException("Your session has expired. Please login");
        }

        String email = jwtImplementation.extractEmailAddressFromToken(jwToken);
        if(email != null){
            new UsernamePasswordAuthenticationToken(
                    this.userDetailsService.loadUserByUsername(email), null, Collections.emptyList()
            );
        }
    }
}