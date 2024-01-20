package com.example.ECommerce.Dto;

import lombok.Data;

@Data
public class JWTAuthResponse {
	
	private String token;
	private UserDTO user;
}