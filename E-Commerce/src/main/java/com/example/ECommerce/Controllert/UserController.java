package com.example.ECommerce.Controllert;

import com.example.ECommerce.Config.AppConstants;
import com.example.ECommerce.Dto.UserDTO;
import com.example.ECommerce.Dto.UserResponse;
import com.example.ECommerce.Exception.APIException;
import com.example.ECommerce.Service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@SecurityRequirement(name = "E-Commerce Application")
public class UserController {
	
	@Autowired
	private final UserService userService;
	
//	@PostMapping("/register")
//	public UserDTO registerUser(UserDTO userDTO) throws APIException {
//		return userService.registerUser(userDTO);
//	}
	@GetMapping("/admin/users")
	public UserResponse getUsers(
			@RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_USERS_BY, required = false) String sortBy,
			@RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) throws APIException {
		
		return userService.getAllUsers(pageNumber, pageSize, sortBy, sortOrder);

//		return new ResponseEntity<UserResponse>(userResponse, HttpStatus.FOUND);
	}

//	@GetMapping("/public/users/{userId}")
//	public UserDTO getUser(@PathVariable Long userId) {
//		return userService.getUserById(userId);
//	}
//
//	@PutMapping("/public/users/{userId}")
//	public ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO userDTO, @PathVariable Long userId) {
//		return userService.updateUser(userId, userDTO);
//	}

//	@DeleteMapping("/admin/users/{userId}")
//	public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
//		String status = userService.deleteUser(userId);
//
//		return new ResponseEntity<String>(status, HttpStatus.OK);
//	}
}
