package com.example.ECommerce.ServiceImpl;

import com.example.ECommerce.Config.AppConstants;
import com.example.ECommerce.Dto.*;
import com.example.ECommerce.Exception.APIException;
import com.example.ECommerce.Repository.AddressRepo;
import com.example.ECommerce.Repository.CartRepo;
import com.example.ECommerce.Repository.RoleRepo;
import com.example.ECommerce.Repository.UserRepo;
import com.example.ECommerce.Service.UserService;
import com.example.ECommerce.entityes.Address;
import com.example.ECommerce.entityes.Cart;
import com.example.ECommerce.entityes.Role;
import com.example.ECommerce.entityes.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

//@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepo userRepo;
	@Autowired
	private AddressRepo addressRepo;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private RoleRepo roleRepo;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private CartRepo cartService;
	
	
	public UserServiceImpl(ModelMapper modelMapper) {
		this.modelMapper = modelMapper;
	}
	
	@Override
	public UserDTO registerUser(UserDTO userDTO) throws APIException {
		try {
			User user = modelMapper.map(userDTO, User.class);
			
			Cart cart = new Cart();
			user.setCart(cart);
			
			Role role = roleRepo.findById(AppConstants.USER_ID).get();
			user.getRoles().add(role);
			
			String country = userDTO.getAddress().getCountry();
			String state = userDTO.getAddress().getState();
			String city = userDTO.getAddress().getCity();
			String pinCode = userDTO.getAddress().getPinCode();
			String street = userDTO.getAddress().getStreet();
			String buildingName = userDTO.getAddress().getBuildingName();
			
			Address address = addressRepo.findByCountryAndStateAndCityAndPincodeAndStreetAndBuildingName(country, state, city, pinCode, street, buildingName);
			
			if (address == null) {
				address = new Address(country, state, city, pinCode, street, buildingName);
				
				address = addressRepo.save(address);
			}
			
			user.setAddresses(List.of(address));
			
			User registeredUser = userRepo.save(user);
			
			cart.setUser(registeredUser);
			
			userDTO = modelMapper.map(registeredUser, UserDTO.class);
			
			userDTO.setAddress(modelMapper.map(user.getAddresses().stream().findFirst().get(), AddressDTO.class));
			
			return userDTO;
		} catch (DataIntegrityViolationException e) {
			throw new APIException("User already exists with emailId: " + userDTO.getEmail());
		}
		
	}
	
	@Override
	public UserResponse getAllUsers(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) throws APIException {
		
		Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
		Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
		
		Page<User> pageUsers = userRepo.findAll(pageDetails);
		
		List<User> users = pageUsers.getContent();
		
		if (users.size() == 0) {
			throw new APIException("no User exists");
		}
		List<UserDTO> userDTOS = users.stream().map(user -> {
			UserDTO dto = modelMapper.map(user, UserDTO.class);
			
			if (user.getAddresses().size() != 0) {
				dto.setAddress(modelMapper.map(user.getAddresses().stream().findFirst().get(), AddressDTO.class));
			}
			CartDTO cartDTO = modelMapper.map(user.getCart(), CartDTO.class);
			
			List<ProductDTO> products = user.getCart().getCartItems().stream().map(item -> modelMapper.map(item.getProduct(), ProductDTO.class)).collect(Collectors.toList());
			
			dto.setCart(cartDTO);
			return dto;
		}).collect(Collectors.toList());
		
		UserResponse userResponse = new UserResponse();
		
		userResponse.setContent(userDTOS);
		userResponse.setPageNumber(pageUsers.getNumber());
		userResponse.setPageSize(pageUsers.getSize());
		userResponse.setTotalElements(pageUsers.getTotalElements());
		userResponse.setTotalPages(pageUsers.getTotalPages());
		userResponse.setLastPage(pageUsers.isLast());
		return userResponse;
	}
	
	
}
