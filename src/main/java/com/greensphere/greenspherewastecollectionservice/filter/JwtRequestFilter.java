package com.greensphere.greenspherewastecollectionservice.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greensphere.greenspherewastecollectionservice.dto.*;
import com.greensphere.greenspherewastecollectionservice.service.ApiConnector;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

@Component
@Slf4j
@RequiredArgsConstructor
@Order(3)
public class JwtRequestFilter extends OncePerRequestFilter {
    private static final String MDC_UID_KEY = "uid";
    private final ObjectMapper objectMapper;
    private final ApiConnector apiConnector;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            String header = request.getHeader("Authorization");
            if (header == null || !header.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }
            String filteredToken = header.replace("Bearer ", "");
            /*
             * 1. Send token to auth service for validation
             * 2. Retrieve app user and user details objects from validation response
             * 3. Create UsernamePasswordAuthenticationToken and set in SecurityContext
             * 4. Set app user and username to request
             * */
            TokenValidationRequest tokenValidationRequest = new TokenValidationRequest();
            tokenValidationRequest.setToken(filteredToken);

            ValidateCoreResponse tokenValidationResponse = apiConnector.tokenValidate(tokenValidationRequest);
            if (tokenValidationResponse.getCode().equals("0000")) {
                log.info("[JwtRequestFilter]:doFilterInternal-> token validation success");
                UserDetail userDetails = tokenValidationResponse.getData().getUserDetails();

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null,userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                AppUser appUser = tokenValidationResponse.getData().getAppUser();
                if (appUser != null) {
                    request.setAttribute("appUser", appUser);
                    MDC.put(MDC_UID_KEY, !StringUtils.isEmpty(appUser.getUsername()) ? appUser.getUsername() : null);
                    log.info("[JwtRequestFilter]:doFilterInternal-> appUser: " + appUser.getUsername());
                }
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                filterChain.doFilter(request, response);
            }else {
                log.warn("[JwtRequestFilter]:doFilterInternal-> token validation failed");
                DefaultResponse defaultResponse = DefaultResponse.builder()
                        .code(tokenValidationResponse.getCode())
                        .title(tokenValidationResponse.getTitle())
                        .message(tokenValidationResponse.getMessage())
                        .build();
                generateErrorResponse(response, defaultResponse);
            }
        } catch (Exception e) {
            log.error("[JwtRequestFilter]:doFilterInternal-> Exception: " + e.getMessage(), e);
        }
        finally {
            MDC.remove(MDC_UID_KEY);
        }
    }

    private void generateErrorResponse(HttpServletResponse response, DefaultResponse defaultResponse) throws IOException {
        generateErrorResponse(response, defaultResponse, HttpServletResponse.SC_UNAUTHORIZED);
    }

    private void generateErrorResponse(HttpServletResponse response, DefaultResponse defaultResponse, int httpStatus) throws IOException {
        PrintWriter writer = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(httpStatus);
        writer.print(objectMapper.writeValueAsString(defaultResponse));
    }
}
