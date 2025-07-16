package com.example.task.service.auth;


import com.example.task.dto.auth.TokenInfoResponse;
import com.example.task.dto.auth.UserDtoResponse;
import com.example.task.dto.request.LoginDto;
import com.example.task.exception.ForbiddenException;
import com.example.task.exception.UnauthorizedException;
import com.example.task.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Log4j2
@Service
public class AuthTokenService {
  private static final int TOKEN_EXPIRE_IN_SECONDS = 86400;
  private final UserRepository userRepository;
  private final UserDetailsService userDetailsService;
  private final JwtEncoder encoder;
  private final JwtDecoder decoder;
  private final PasswordEncoder passwordEncoder;

  public AuthTokenService(
      final UserRepository userRepository,
      UserDetailsService userDetailsService,
      final JwtEncoder encoder,
      final JwtDecoder decoder,
      final PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.userDetailsService = userDetailsService;
    this.encoder = encoder;
    this.decoder = decoder;
    this.passwordEncoder = passwordEncoder;
  }

  public TokenInfoResponse generateToken(LoginDto userTokenRequest) {

    var user =
        userRepository
            .findByUsername(userTokenRequest.username())
            .filter(u -> passwordEncoder.matches(userTokenRequest.password(), u.getPassword()))
            .orElseThrow(() -> new UnauthorizedException("invalid credentials"));

    Instant now = Instant.now();
    Instant tokenExpiryDate =
        Instant.ofEpochMilli(Instant.now().toEpochMilli() + TOKEN_EXPIRE_IN_SECONDS * 1000);


    JwtClaimsSet claims =
        JwtClaimsSet.builder()
            .issuer("digital-active.mvp.uz")
            .issuedAt(now)
            .expiresAt(tokenExpiryDate)
            .subject(user.getUsername())
            .claim("user_id", user.getId())
            .claim("isActive", user.getEnabled())
            .build();
    var token = this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

    return TokenInfoResponse.builder()
        .userId(user.getId())
        .username(user.getUsername())
        .token(token)
        .build();
  }

  public UsernamePasswordAuthenticationToken verifyAccessToken(final String token) {
    Assert.hasText(token, "JWT token cannot be null or blank");

    var jwt = decoder.decode(token);
    String username = jwt.getSubject();


    // Get user from cache instead of database
    UserDetails user = userDetailsService.loadUserByUsername(username);
    if (user == null) {
      log.error("User {} not found", username);
      throw new UnauthorizedException("user not found");
    }
    if (!user.isEnabled()) {
      log.error("[{}] user blocked", username);
      throw new ForbiddenException(String.format("%s user blocked", username));
    }

    return new UsernamePasswordAuthenticationToken(user, token);
  }

  public UserDtoResponse getCurrentUserData() {
    var authentication = SecurityContextHolder.getContext().getAuthentication();
    var decodedJWT = decoder.decode(authentication.getCredentials().toString());
    return UserDtoResponse.builder()
        .id(decodedJWT.getClaim("user_id"))
        .username(decodedJWT.getSubject())
        .isActive(decodedJWT.getClaim("isActive"))
        .build();
  }

  public UUID getCurrentUserId() {
    var authentication = SecurityContextHolder.getContext().getAuthentication();
    var decodedJWT = decoder.decode(authentication.getCredentials().toString());
    return UUID.fromString(decodedJWT.getClaim("user_id"));
  }
}
