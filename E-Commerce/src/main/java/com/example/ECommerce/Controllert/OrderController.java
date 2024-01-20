package com.example.ECommerce.Controllert;

import java.util.List;

import com.example.ECommerce.Config.AppConstants;
import com.example.ECommerce.Dto.OrderDTO;
import com.example.ECommerce.Dto.OrderResponse;
import com.example.ECommerce.Exception.APIException;
import com.example.ECommerce.Exception.ResourceNotFound;
import com.example.ECommerce.Service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

//TODO PORT 7878
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@SecurityRequirement(name = "E-Commerce Application")
public class OrderController {
	
	public final OrderService orderService;
	
	@PostMapping("/public/users/{emailId}/carts/{cartId}/payments/{paymentMethod}/order")
	public OrderDTO orderProducts(@PathVariable String emailId, @PathVariable Long cartId, @PathVariable String paymentMethod) throws ResourceNotFound {
		return orderService.placeOrder(emailId, cartId, paymentMethod);
	}

	@GetMapping("/admin/orders")
	public OrderResponse getAllOrders(
			@RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_ORDERS_BY, required = false) String sortBy,
			@RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) throws APIException {
		
		//TODO Invoke the orderService to retrieve orders based on parameters
		return orderService.getAllOrders(pageNumber, pageSize, sortBy, sortOrder);

//		return new ResponseEntity<OrderResponse>(orderResponse, HttpStatus.FOUND);
	}
//	@GetMapping("/admin/orders")
//	public ResponseEntity<OrderResponse> getAllOrders(
//			@RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
//			@RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
//			@RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_ORDERS_BY) String sortBy,
//			@RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR) String sortOrder) {
//
//		// Invoke the orderService to retrieve orders based on parameters
//		OrderResponse orderResponse = orderService.getAllOrders(pageNumber, pageSize, sortBy, sortOrder);
//
//		// Create a ResponseEntity with the orderResponse and HttpStatus.FOUND
//		return ResponseEntity.status(HttpStatus.FOUND).body(orderResponse);
//	}
	
	
	@GetMapping("public/users/{emailId}/orders")
	public List<OrderDTO> getOrdersByUser(@PathVariable String emailId) throws APIException {
		return orderService.getOrdersByUser(emailId);
	}
	
	@GetMapping("public/users/{emailId}/orders/{orderId}")
	public OrderDTO getOrderByUser(@PathVariable String emailId, @PathVariable Long orderId) throws ResourceNotFound {
		return orderService.getOrder(emailId, orderId);
	}
	
	@PutMapping("admin/users/{emailId}/orders/{orderId}/orderStatus/{orderStatus}")
	public OrderDTO updateOrderByUser(@PathVariable String emailId, @PathVariable Long orderId, @PathVariable String orderStatus) throws ResourceNotFound {
		return orderService.updateOrder(emailId, orderId, orderStatus);
	}
}
