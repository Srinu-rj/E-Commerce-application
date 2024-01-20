package com.example.ECommerce.Service;

import com.example.ECommerce.Dto.AddressDTO;
import com.example.ECommerce.Exception.APIException;
import com.example.ECommerce.Exception.ResourceNotFound;
import com.example.ECommerce.entityes.Address;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AddressService {
	AddressDTO createAddress(AddressDTO addressDTO) throws APIException;
	List<AddressDTO> getAddresses();
	
	AddressDTO getAddress(Long addressId) throws ResourceNotFound;
	
	AddressDTO updateAddress(Long addressId, Address address) throws  ResourceNotFound;
	
	String deleteAddress(Long addressId) throws  ResourceNotFound;
	
}
