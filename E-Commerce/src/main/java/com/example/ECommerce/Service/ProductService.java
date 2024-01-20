package com.example.ECommerce.Service;

import com.example.ECommerce.Dto.ProductDTO;
import com.example.ECommerce.Dto.ProductResponse;
import com.example.ECommerce.Exception.APIException;
import com.example.ECommerce.Exception.ResourceNotFound;
import com.example.ECommerce.entityes.Product;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {

	ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
	
	
	ProductResponse searchByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) throws APIException, ResourceNotFound;
	
	ProductResponse searchProductByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) throws APIException;
	
	ProductDTO updateProduct(Long productId, Product product) throws APIException, ResourceNotFound;
	
	ProductDTO updateProductImage(Long productId, MultipartFile image) throws ResourceNotFound, APIException, IOException;
	
	String deleteProduct(Long productId) throws ResourceNotFound;
	
	
	ProductDTO addProduct(Long categoryId, Product product) throws APIException, ResourceNotFound;
}
