package com.example.ECommerce.Controllert;

import java.util.List;

import com.example.ECommerce.Dto.AddressDTO;
import com.example.ECommerce.Exception.APIException;
import com.example.ECommerce.Exception.ResourceNotFound;
import com.example.ECommerce.Service.AddressService;
import com.example.ECommerce.entityes.Address;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@SecurityRequirement(name = "E-Commerce Application")
public class AddressController {

//	@Autowired
	private final AddressService addressService;
	
	@PostMapping("/address")
	public AddressDTO createAddress(@Valid @RequestBody AddressDTO addressDTO) throws APIException {
		return addressService.createAddress(addressDTO);
		
	}
	
	@GetMapping("/addresses")
	public List<AddressDTO> getAddresses() {
		return addressService.getAddresses();
	}
	
	@GetMapping("/addresses/{addressId}")
	public AddressDTO getAddress(@PathVariable Long addressId) throws  ResourceNotFound {
		return addressService.getAddress(addressId);
		
	}
	
	@PutMapping("/addresses/{addressId}")
	public AddressDTO updateAddress(@PathVariable Long addressId, @RequestBody Address address) throws ResourceNotFound {
		return addressService.updateAddress(addressId, address);
	}
	
	@DeleteMapping("/addresses/{addressId}")
	public String deleteAddress(@PathVariable Long addressId) throws ResourceNotFound {
		return addressService.deleteAddress(addressId);
		
	}
}
