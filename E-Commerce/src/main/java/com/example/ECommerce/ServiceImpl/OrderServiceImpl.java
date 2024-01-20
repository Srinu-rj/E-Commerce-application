package com.example.ECommerce.ServiceImpl;

import com.example.ECommerce.Dto.OrderDTO;
import com.example.ECommerce.Dto.OrderItemDTO;
import com.example.ECommerce.Dto.OrderResponse;
import com.example.ECommerce.Exception.APIException;
import com.example.ECommerce.Exception.ResourceNotFound;
import com.example.ECommerce.Repository.CartRepo;
import com.example.ECommerce.Repository.OrderItemRepo;
import com.example.ECommerce.Repository.OrderRepo;
import com.example.ECommerce.Repository.PaymentRepo;
import com.example.ECommerce.Service.CartService;
import com.example.ECommerce.Service.OrderService;
import com.example.ECommerce.entityes.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
	
	
	private final CartService cartService;
	private final OrderItemRepo orderItemRepo;
	private final PaymentRepo paymentRepo;
	private final CartRepo cartRepo;
	private final OrderRepo orderRepo;
	private final ModelMapper modelMapper;
	
	@Override
	public OrderDTO placeOrder(String emailId, Long cartId, String paymentMethod) throws ResourceNotFound {
		
		Cart cart = cartRepo.findCartByEmailAndCartId(emailId, cartId);
		if (cart != null) {
			throw new ResourceNotFound("cart", "cartId", cartId);
		}
		Order order = new Order();
		order.setEmail(emailId);
		order.setOrderDate(LocalDate.now());
		order.setTotalAmount(cart.getTotalPrice());
		order.setOrderStatus("order Accepted?");
		
		Payment payment = new Payment();
		payment.setOrder(order);
		payment.setPaymentMethod(paymentMethod);
		payment = paymentRepo.save(payment);
		
		Order saveOrder = orderRepo.save(order);
		
		List<CartItem> cartItems = cart.getCartItems();
		
		List<OrderItem> orderItems = new ArrayList<>();
		
		for (CartItem cartItem : cartItems) {
			OrderItem orderItem = new OrderItem();
			
			orderItem.setProduct(cartItem.getProduct());
			orderItem.setDiscount(cartItem.getDiscount());
			orderItem.setQuantity(cartItem.getQuantity());
			orderItem.setOrderedProductPrice(cartItem.getProductPrice());
			
			orderItems.add(orderItem);
			
		}
		orderItems = orderItemRepo.saveAll(orderItems);
		
		cart.getCartItems().forEach(item -> {
			int quantity = item.getQuantity();
			Product product = item.getProduct();
			
			try {
				cartService.deleteProductFromCart(cartId, item.getProduct().getProductId());
			} catch (ResourceNotFound e) {
				throw new RuntimeException(e);
			}
			product.setQuantity(product.getQuantity() - quantity);
		});
		OrderDTO orderDTO = modelMapper.map(saveOrder, OrderDTO.class);
		
		orderItems.forEach(item -> orderDTO.getOrderItems().add(modelMapper.map(item, OrderItemDTO.class)));
		return orderDTO;
		
		
	}
	
	@Override
	public OrderResponse getAllOrders(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) throws APIException {
		
		Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
				: Sort.by(sortBy).descending();
		
		Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
		
		Page<Order> pageOrders = orderRepo.findAll(pageDetails);
		
		List<Order> orders = pageOrders.getContent();
		
		List<OrderDTO> orderDTOs = orders.stream().map(order -> modelMapper.map(order, OrderDTO.class))
				.collect(Collectors.toList());
		
		if (orderDTOs.size() == 0) {
			throw new APIException("No orders placed yet by the users");
		}
		
		OrderResponse orderResponse = new OrderResponse();
		
		orderResponse.setContent(orderDTOs);
		orderResponse.setPageNumber(pageOrders.getNumber());
		orderResponse.setPageSize(pageOrders.getSize());
		orderResponse.setTotalElements(pageOrders.getTotalElements());
		orderResponse.setTotalPages(pageOrders.getTotalPages());
		orderResponse.setLastPage(pageOrders.isLast());
		
		return orderResponse;
	}
	
	@Override
	public List<OrderDTO> getOrdersByUser(String emailId) throws APIException {
		List<Order> orders = orderRepo.findAllByEmail(emailId);
		
		List<OrderDTO> orderDTOs = orders.stream().map(order -> modelMapper.map(order, OrderDTO.class))
				.collect(Collectors.toList());
		
		if (orderDTOs.size() == 0) {
			throw new APIException("No orders placed yet by the user with email: " + emailId);
		}
		
		return orderDTOs;
	}
	
	@Override
	public OrderDTO getOrder(String emailId, Long orderId) throws ResourceNotFound {
		Order order = orderRepo.findOrderByEmailAndOrderId(emailId, orderId);
		if (order == null) {
			throw new ResourceNotFound("Order", "orderId", orderId);
		}
		return modelMapper.map(order, OrderDTO.class);
		
	}
	
	@Override
	public OrderDTO updateOrder(String emailId, Long orderId, String orderStatus) throws ResourceNotFound {
		Order order = orderRepo.findOrderByEmailAndOrderId(emailId, orderId);
		
		if (order == null) {
			throw new ResourceNotFound("Order", "orderId", orderId);
		}
		
		order.setOrderStatus(orderStatus);
		
		return modelMapper.map(order, OrderDTO.class);
	}
}
