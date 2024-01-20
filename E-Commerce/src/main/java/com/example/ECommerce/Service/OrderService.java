package com.example.ECommerce.Service;

import com.example.ECommerce.Dto.OrderDTO;
import com.example.ECommerce.Dto.OrderResponse;
import com.example.ECommerce.Exception.APIException;
import com.example.ECommerce.Exception.ResourceNotFound;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderService {
	OrderDTO placeOrder(String emailId, Long cartId, String paymentMethod) throws ResourceNotFound;
	
	OrderResponse getAllOrders(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) throws APIException;
	
	List<OrderDTO> getOrdersByUser(String emailId) throws APIException;
	
	OrderDTO getOrder(String emailId, Long orderId) throws ResourceNotFound;
	
	OrderDTO updateOrder(String emailId, Long orderId, String orderStatus) throws ResourceNotFound;
}
