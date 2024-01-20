package com.example.ECommerce.Dto;

import lombok.Data;

@Data
public class JWTAuthRequest {
	private String username;  // email
	private String password;
}