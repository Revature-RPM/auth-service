package com.revature.rpm.web.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.rpm.dtos.UserCredentials;
import com.revature.rpm.dtos.UserPrincipal;
import com.revature.rpm.security.config.JwtConfig;
import com.revature.rpm.security.util.JwtGenerator;
import java.io.IOException;
import java.util.Collections;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Authenticates a login request using the provided username and password. Upon successful
 * authentication, a JWT will be passed back to the client via a HTTP response header.
 */
public class AuthFilter extends UsernamePasswordAuthenticationFilter {

  private AuthenticationManager authManager;

  private JwtConfig jwtConfig;

  /**
   * Constructor for the AuthFilter that instantiates the AuthenticationManager and the JwtConfig
   * fields. <br>
   * <br>
   * The default endpoint is being leveraged. All authentication (login) requests should be POST
   * requests made to /login.
   *
   * @param authManager - Processes authentication requests.
   * @param jwtConfig - Provides the configuration for how JWT tokens are created/validated.
   */
  public AuthFilter(AuthenticationManager authManager, JwtConfig jwtConfig) {
    this.authManager = authManager;
    this.jwtConfig = jwtConfig;
  }

  /**
   * Attempts to authenticate a login request. Credentials are retrieved from the HTTP request body
   * and stored within a UserCredentials object. This is passed to the constructor of Spring
   * Security's UsernamePasswordAuthenticationToken class to generate a token that will be
   * authenticated. <br>
   * <br>
   * Tries to:<br>
   * 1. Get credentials from request body.<br>
   * 2. Create an authentication token (contains user credentials) which will be used by the
   * AuthenticationManager.<br>
   * 3. Leverage AuthenticationManager to authenticate the user.
   *
   * @param request - Provides information regarding the HTTP request.
   * @param response - Provides information regarding the HTTP response.
   * @return Authentication - Represents the token for an authenticated principal.
   */
  @Override
  public Authentication attemptAuthentication(
      HttpServletRequest request, HttpServletResponse response) {

    try {

      UserCredentials creds =
          new ObjectMapper().readValue(request.getInputStream(), UserCredentials.class);
      UsernamePasswordAuthenticationToken authToken =
          new UsernamePasswordAuthenticationToken(
              creds.getUsername(), creds.getPassword(), Collections.emptyList());
      return authManager.authenticate(authToken);

    } catch (IOException e) {

      throw new RuntimeException(e);
    }
  }

  /**
   * Upon a successful authentication, a token should be generated. The token is generated from the
   * JwtGenerator using the configuration found within the JwtConfig field. After a token is
   * generated, add AppUser object as JSON to the response body upon successful login by getting the
   * print writer of HttpServletResponse. Token is then added to the response header from JwtConfig
   * with a corresponding prefix.
   *
   * @param request - Provides information regarding the HTTP request.
   * @param response - Provides information regarding the HTTP response.
   * @param chain - Used to pass the HTTP request and response objects to the next filter in the
   *     chain (unused here).
   * @param auth - Represents a token for a user which was successfully authenticated.
   */
  @Override
  protected void successfulAuthentication(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain chain,
      Authentication auth)
      throws IOException, ServletException {

    String token = JwtGenerator.createJwt(auth, jwtConfig);
    response
        .getWriter()
        .write(
            new ObjectMapper()
                .writeValueAsString(((UserPrincipal) auth.getPrincipal()).getAppUser()));
    response.addHeader(jwtConfig.getHeader(), jwtConfig.getPrefix() + token);
  }
}
