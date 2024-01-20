package com.example.ECommerce.ServiceImpl;

import com.example.ECommerce.Dto.AddressDTO;
import com.example.ECommerce.Exception.APIException;
import com.example.ECommerce.Exception.ResourceNotFound;
import com.example.ECommerce.Repository.AddressRepo;
import com.example.ECommerce.Repository.UserRepo;
import com.example.ECommerce.Service.AddressService;
import com.example.ECommerce.entityes.Address;
import com.example.ECommerce.entityes.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AddressServiceImpl implements AddressService {
	
	private final AddressRepo addressRepo;
	private final UserRepo userRepo;
	private final ModelMapper modelMapper;
	
	
	@Override
	public AddressDTO createAddress(AddressDTO addressDTO) throws APIException {
		
		String country = addressDTO.getCountry();
		String state = addressDTO.getState();
		String city = addressDTO.getCity();
		String pinCode = addressDTO.getPinCode();
		String street = addressDTO.getStreet();
		String buildingName = addressDTO.getBuildingName();
		
		Address addressFromDB = addressRepo.findByCountryAndStateAndCityAndPincodeAndStreetAndBuildingName
				(country, state, street, city, pinCode, buildingName);
		if (addressFromDB != null) {
			throw new APIException("Address already exits");
		}
		//TODO : combine addressDTO address class
		Address address = modelMapper.map(addressDTO, Address.class);
		//TODO :save the address
		Address saveAddress = addressRepo.save(address);
		//TODO :
		return modelMapper.map(saveAddress, AddressDTO.class);
	}
	
	@Override
	public List<AddressDTO> getAddresses() {
		//TODO : get List Of Addresses
		List<Address> addressDTOS = addressRepo.findAll();
		List<AddressDTO> addressDTOSS = addressDTOS.stream().map(address -> modelMapper.map(address, AddressDTO.class)).collect(Collectors.toList());
		return addressDTOSS;
	}
	
	@Override
	public AddressDTO getAddress(Long addressId) throws ResourceNotFound {
		Address addressDTO = addressRepo.findById(addressId).orElseThrow(
				() -> new ResourceNotFound("address", "addressId", addressId));
		return modelMapper.map(addressDTO, AddressDTO.class);
		
		
	}
	
	@Override
	public AddressDTO updateAddress(Long addressId, Address address) throws ResourceNotFound {
		//todo : get address from DB
		Address addressFromDB = addressRepo.findByCountryAndStateAndCityAndPincodeAndStreetAndBuildingName(
				address.getCountry(), address.getState(), address.getCity(), address.getPincode(), address.getStreet(),
				address.getBuildingName());
		//TODO check address is null it will throw RessourceNotFoundException
		if (addressFromDB == null) {
			//TODO throw exception
			addressFromDB = addressRepo.findById(addressId)
					.orElseThrow(() -> new ResourceNotFound("address", "addressId", addressId));
			//todo save updated Address
			addressFromDB.setCountry(address.getCountry());
			addressFromDB.setState(address.getState());
			addressFromDB.setCity(address.getCity());
			addressFromDB.setPincode(address.getPincode());
			addressFromDB.setStreet(address.getStreet());
			addressFromDB.setBuildingName(address.getBuildingName());
			//todo save updated Address
			Address updateAddress = addressRepo.save(addressFromDB);
			return modelMapper.map(updateAddress, AddressDTO.class);
		} else {
			//TODO: getting addresses list of list of users
			List<User> users = userRepo.findByAddress(addressId);
			
			final Address a = addressFromDB;
			users.forEach(user -> user.getAddresses().add(a));
			
			deleteAddress(addressId);
			return modelMapper.map(addressFromDB, AddressDTO.class);
		}
		
		
	}
	
	@Override
	public String deleteAddress(Long addressId) throws  ResourceNotFound {
		Address address = addressRepo.findById(addressId)
				.orElseThrow(() -> new ResourceNotFound("address", "addressId", addressId));
		
		//TODO: find by a
		List<User> users = userRepo.findByAddress(addressId);
		
		//TODO iterate
		users.forEach(user -> {
			user.getAddresses().remove(address);
			userRepo.save(user);
		});
		
		addressRepo.deleteById(addressId);
		return "Address deleted successfully with addressId";
	}
}
