package com.example.ECommerce.Controllert;

import java.util.List;

import com.example.ECommerce.Dto.CartDTO;
import com.example.ECommerce.Exception.APIException;
import com.example.ECommerce.Exception.ResourceNotFound;
import com.example.ECommerce.Service.CartService;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@SecurityRequirement(name = "E-Commerce Application")
public class CartController {

	private final CartService cartService;

	@PostMapping("/public/carts/{cartId}/products/{productId}/quantity/{quantity}")
	public CartDTO addProductToCart(@PathVariable Long cartId, @PathVariable Long productId, @PathVariable Integer quantity) throws  APIException, ResourceNotFound {
		return cartService.addProductToCart(cartId, productId, quantity);
}

	@GetMapping("/admin/carts")
	public List<CartDTO> getCarts() throws APIException {

		return cartService.getAllCarts();
}

	@GetMapping("/public/users/{emailId}/carts/{cartId}")
	public CartDTO getCartById(@PathVariable String emailId, @PathVariable Long cartId) throws ResourceNotFound {
		return cartService.getCart(emailId, cartId);
}

	@PutMapping("/public/carts/{cartId}/products/{productId}/quantity/{quantity}")
	public CartDTO updateCartProduct(@PathVariable Long cartId, @PathVariable Long productId, @PathVariable Integer quantity) throws APIException, ResourceNotFound {
		return cartService.updateProductQuantityInCart(cartId,productId,quantity);
}

	@DeleteMapping("/public/carts/{cartId}/product/{productId}")
	public String deleteProductFromCart(@PathVariable Long cartId, @PathVariable Long productId) throws ResourceNotFound {
		return cartService.deleteProductFromCart(cartId, productId);
}
}
