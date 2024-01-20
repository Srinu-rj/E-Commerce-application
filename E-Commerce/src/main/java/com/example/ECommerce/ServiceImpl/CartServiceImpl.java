package com.example.ECommerce.ServiceImpl;

import com.example.ECommerce.Dto.CartDTO;
import com.example.ECommerce.Dto.ProductDTO;
import com.example.ECommerce.Exception.APIException;
import com.example.ECommerce.Exception.ResourceNotFound;
import com.example.ECommerce.Repository.*;
import com.example.ECommerce.Service.CartService;
import com.example.ECommerce.entityes.Cart;
import com.example.ECommerce.entityes.CartItem;
import com.example.ECommerce.entityes.Product;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
	
	private final CartItemRepo cartItemRepo;
	private final CartRepo cartRepo;
	private final ModelMapper modelMapper;
	private final ProductRepo productRepo;
	
	@Override
	public CartDTO addProductToCart(Long cartId, Long productId, Integer quantity) throws  APIException, ResourceNotFound {
		//TODO GET CART BY ID
		Cart cart = cartRepo.findById(cartId)
				.orElseThrow(() -> new ResourceNotFound("cart", "cartId", cartId));
		Product product = productRepo.findById(productId).orElse(null);
		CartItem cartItem = cartItemRepo.findCartItemByProductIdAndCartId(cartId, productId);
		
		if (cartItem != null) {
			throw new APIException("product" + product.getProductName() + " already exists in the cart");
		}
		if (product.getQuantity() == 0) {
			throw new APIException(product.getProductName() + "is not available");
		}
		if (product.getQuantity() < quantity) {
			throw new APIException("Please, make an order of the " + product.getProductName()
					+ " less than or equal to the quantity " + product.getQuantity() + ".");
		}
		CartItem newCardItem = new CartItem();
		
		newCardItem.setCart(cart);
		newCardItem.setProduct(product);
		newCardItem.setQuantity(quantity);
		newCardItem.setDiscount(product.getDiscount());
		newCardItem.setProductPrice(product.getSpecialPrice());
		
		cartItemRepo.save(newCardItem);
		product.setQuantity(product.getQuantity() - quantity);
		
		cart.setTotalPrice(cart.getTotalPrice() + (product.getSpecialPrice() * quantity));
		CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
		
		List<ProductDTO> productDTOS = cart.getCartItems().stream()
				.map(p -> modelMapper.map(p.getProduct(), ProductDTO.class)).collect(Collectors.toList());
		cartDTO.setProducts(productDTOS);
		return cartDTO;
	}
	
	@Override
	public List<CartDTO> getAllCarts() throws APIException {
		List<Cart> carts = cartRepo.findAll();
		
		if (carts.size() == 0) {
			throw new APIException("No cart exists");
		}
		
		List<CartDTO> cartDTOs = carts.stream().map(cart -> {
			CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
			
			List<ProductDTO> products = cart.getCartItems().stream()
					.map(p -> modelMapper.map(p.getProduct(), ProductDTO.class)).collect(Collectors.toList());
			
			cartDTO.setProducts(products);
			
			return cartDTO;
			
		}).collect(Collectors.toList());
		
		return cartDTOs;
	}
	
	@Override
	public CartDTO getCart(String emailId, Long cartId) throws  ResourceNotFound {
		Cart cart = cartRepo.findCartByEmailAndCartId(emailId, cartId);
		
		if (cart == null) {
			throw new ResourceNotFound("Cart", "cartId", cartId);
		}
		
		CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
		
		List<ProductDTO> products = cart.getCartItems().stream()
				.map(p -> modelMapper.map(p.getProduct(), ProductDTO.class)).collect(Collectors.toList());
		
		cartDTO.setProducts(products);
		
		return cartDTO;	}
	
	@Override
	public CartDTO updateProductQuantityInCart(Long cartId, Long productId, Integer quantity) throws  APIException, ResourceNotFound {
		Cart cart = cartRepo.findById(cartId)
				.orElseThrow(() -> new ResourceNotFound("Cart", "cartId", cartId));
		
		Product product = productRepo.findById(productId)
				.orElseThrow(() -> new ResourceNotFound("Product", "productId", productId));
		
		CartItem cartItem = cartItemRepo.findCartItemByProductIdAndCartId(cartId, productId);
		
		if (cartItem == null) {
			throw new APIException("Product " + product.getProductName() + " not available in the cart!!!");
		}
		
		double cartPrice = cart.getTotalPrice() - (cartItem.getProductPrice() * cartItem.getQuantity());
		
		cartItem.setProductPrice(product.getSpecialPrice());
		
		cart.setTotalPrice(cartPrice + (cartItem.getProductPrice() * cartItem.getQuantity()));
		
		cartItem = cartItemRepo.save(cartItem);
		
		CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
		
		List<ProductDTO> productDTOs = cart.getCartItems().stream()
				.map(p -> modelMapper.map(p.getProduct(), ProductDTO.class)).collect(Collectors.toList());
		
		cartDTO.setProducts(productDTOs);
		
		return cartDTO;
	}
	
	@Override
	public String deleteProductFromCart(Long cartId, Long productId) throws  ResourceNotFound {
		Cart cart = cartRepo.findById(cartId)
				.orElseThrow(() -> new ResourceNotFound("Cart", "cartId", cartId));
		
		CartItem cartItem = cartItemRepo.findCartItemByProductIdAndCartId(cartId, productId);
		
		if (cartItem == null) {
			throw new ResourceNotFound("Product", "productId", productId);
		}
		
		cart.setTotalPrice(cart.getTotalPrice() - (cartItem.getProductPrice() * cartItem.getQuantity()));
		
		Product product = cartItem.getProduct();
		product.setQuantity(product.getQuantity() + cartItem.getQuantity());
		
		cartItemRepo.deleteCartItemByProductIdAndCartId(cartId, productId);
		
		return "Product " + cartItem.getProduct().getProductName() + " removed from the cart !!!";	}
	
	@Override
	public void updateProductInCarts(Long cartId, Long productId) throws ResourceNotFound, APIException {
		Cart cart = cartRepo.findById(cartId)
				.orElseThrow(() -> new ResourceNotFound("Cart", "cartId", cartId));
		
		Product product = productRepo.findById(productId)
				.orElseThrow(() -> new ResourceNotFound("Product", "productId", productId));
		
		CartItem cartItem = cartItemRepo.findCartItemByProductIdAndCartId(cartId, productId);
		
		if (cartItem == null) {
			throw new APIException("Product " + product.getProductName() + " not available in the cart!!!");
		}
		
		double cartPrice = cart.getTotalPrice() - (cartItem.getProductPrice() * cartItem.getQuantity());
		
		cartItem.setProductPrice(product.getSpecialPrice());
		
		cart.setTotalPrice(cartPrice + (cartItem.getProductPrice() * cartItem.getQuantity()));
		
		cartItem = cartItemRepo.save(cartItem);
	}
}
