package com.example.ECommerce.Service;

import com.example.ECommerce.Dto.CartDTO;
import com.example.ECommerce.Exception.APIException;
import com.example.ECommerce.Exception.ResourceNotFound;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CartService {
	
	CartDTO addProductToCart(Long cartId, Long productId, Integer quantity) throws  APIException, ResourceNotFound;

	List<CartDTO> getAllCarts() throws APIException;

	CartDTO getCart(String emailId, Long cartId) throws  ResourceNotFound;

	CartDTO updateProductQuantityInCart(Long cartId, Long productId, Integer quantity) throws  APIException, ResourceNotFound;

	String deleteProductFromCart(Long cartId, Long productId) throws  ResourceNotFound;
	
	void updateProductInCarts(Long cartId, Long productId) throws ResourceNotFound, APIException;
}
