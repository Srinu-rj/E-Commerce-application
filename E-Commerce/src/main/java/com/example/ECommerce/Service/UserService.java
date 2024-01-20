package com.example.ECommerce.Service;

import com.example.ECommerce.Dto.UserDTO;
import com.example.ECommerce.Dto.UserResponse;
import com.example.ECommerce.Exception.APIException;
import org.springframework.http.ResponseEntity;

public interface UserService {
	UserDTO registerUser(UserDTO userDTO) throws APIException;
	UserResponse getAllUsers(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) throws APIException;
//	UserDTO getUserById(Long userId);
//	ResponseEntity<UserDTO> updateUser(Long userId, UserDTO userDTO);
}
