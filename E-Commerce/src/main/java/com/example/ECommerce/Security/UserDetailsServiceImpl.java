package com.example.ECommerce.Security;

import com.example.ECommerce.Config.UserInfoConfig;
import com.example.ECommerce.Exception.ResourceNotFound;
import com.example.ECommerce.Repository.UserRepo;
import com.example.ECommerce.entityes.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
	private final UserRepo userRepo;
	@Override
	public UserDetails loadUserByUsername(String username) {
		
		Optional<User>user=userRepo.findByEmail(username);
		try {
			return user.map(UserInfoConfig::new).orElseThrow(()->new ResourceNotFound("User", "email", username));
		} catch (ResourceNotFound e) {
			throw new RuntimeException(e);
		}
		
	}
}
