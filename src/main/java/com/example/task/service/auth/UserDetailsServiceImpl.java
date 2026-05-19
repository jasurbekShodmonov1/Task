package com.example.task.service.auth;


import com.example.task.entity.User;
import com.example.task.exception.NotFoundException;
import com.example.task.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {
  private final UserRepository userRepository;

  @Autowired
  public UserDetailsServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public User loadUserByUsername(String username) throws UsernameNotFoundException {

    return userRepository
        .findByUsername(username)
        .filter(User::getEnabled)
        .orElseThrow(() -> new NotFoundException("user not found with name:" + username));
  }
}
