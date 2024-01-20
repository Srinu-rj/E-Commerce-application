package com.example.ECommerce.Controllert;

import java.io.IOException;

import com.example.ECommerce.Config.AppConstants;
import com.example.ECommerce.Dto.ProductDTO;
import com.example.ECommerce.Dto.ProductResponse;
import com.example.ECommerce.Exception.APIException;
import com.example.ECommerce.Exception.ResourceNotFound;
import com.example.ECommerce.Service.ProductService;
import com.example.ECommerce.entityes.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "E-Commerce Application")
public class ProductController {
	
	@Autowired
	private ProductService productService;
	
	@PostMapping("/admin/categories/{categoryId}/product")
	public ProductDTO addProduct(@Valid @RequestBody Product product, @PathVariable Long categoryId) throws APIException, ResourceNotFound {
		
		return productService.addProduct(categoryId, product);
		
	}
	
	@GetMapping("/public/products")
	public ProductResponse getAllProducts(
			@RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
			@RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
			@RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY, required = false) String sortBy,
			@RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) {
		return productService.getAllProducts(pageNumber, pageSize, sortBy, sortOrder);
		
	}
	
	@GetMapping("/public/categories/{categoryId}/products")
	public ProductResponse getProductsByCategory(@PathVariable Long categoryId,
												 @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
												 @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
												 @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY, required = false) String sortBy,
												 @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) throws APIException, ResourceNotFound {
		
		return productService.searchByCategory(categoryId, pageNumber, pageSize, sortBy,
				sortOrder);
		
	}
	
	@GetMapping("/public/products/keyword/{keyword}")
	public ProductResponse getProductsByKeyword(@PathVariable String keyword,
												@RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
												@RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
												@RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY, required = false) String sortBy,
												@RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder) throws APIException {
		return productService.searchProductByKeyword(keyword, pageNumber, pageSize, sortBy,
				sortOrder);
		
	}
	
	@PutMapping("/admin/products/{productId}")
	public ProductDTO updateProduct(@RequestBody Product product,
									@PathVariable Long productId) throws APIException, ResourceNotFound {
		return productService.updateProduct(productId, product);
	}
	
	@PutMapping("/admin/products/{productId}/image")
	public ProductDTO updateProductImage(@PathVariable Long productId, @RequestParam("image") MultipartFile image) throws IOException, APIException, ResourceNotFound {
		return productService.updateProductImage(productId, image);
	}
	
	@DeleteMapping("/admin/products/{productId}")
	public String deleteProductByCategory(@PathVariable Long productId) throws ResourceNotFound {
		return productService.deleteProduct(productId);
		
	}
	
}
