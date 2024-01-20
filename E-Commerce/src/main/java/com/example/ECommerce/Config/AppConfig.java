package com.example.ECommerce.Config;

import com.example.ECommerce.Dto.CartDTO;
import com.example.ECommerce.Service.CartService;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class AppConfig {
	
	@Bean
	public ModelMapper modelMapper(){
		return new ModelMapper();
	}
	

	}

