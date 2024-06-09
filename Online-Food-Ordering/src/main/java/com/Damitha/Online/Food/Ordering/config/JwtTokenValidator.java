package com.Damitha.Online.Food.Ordering.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.List;

/**
 * This filter class is responsible for validating JWT tokens in incoming HTTP requests.
 * It extends OncePerRequestFilter to ensure that it is executed once per request.
 */
public class JwtTokenValidator extends OncePerRequestFilter {

    /**
     * This method is called for every incoming HTTP request. It validates the JWT token
     * if present in the request header.
     *
     * @param request  the incoming HTTP request
     * @param response the HTTP response
     * @param filterChain the filter chain
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // Get the JWT token from the Authorization header
        String jwt = request.getHeader(JwtConstant.jwtHeader);

        if (jwt != null) {
            // Remove the "Bearer " prefix from the token
            jwt = jwt.substring(7);

            try {
                // Create a SecretKey object using the secret key bytes
                SecretKey key = Keys.hmacShaKeyFor(JwtConstant.secretKey.getBytes());

                // Parse the claims from the JWT token
                Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody();

                // Extract the email and authorities from the claims
                String email = String.valueOf(claims.get("email"));
                String authorities = String.valueOf(claims.get("authorities"));

                // Convert the authorities string to a list of GrantedAuthority objects
                List<GrantedAuthority> auth = AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);

                // Create an Authentication object with the email and authorities
                Authentication authentication = new UsernamePasswordAuthenticationToken(email, null, auth);

                // Set the authentication object in the security context
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (Exception e) {
                // Throw an exception if the token is invalid
                throw new BadCredentialsException("Invalid Token.....");
            }

        }

        // Continue with the filter chain
        filterChain.doFilter(request, response);
    }
}

