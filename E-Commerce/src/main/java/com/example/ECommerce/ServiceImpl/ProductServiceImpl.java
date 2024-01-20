package com.example.ECommerce.ServiceImpl;

import com.example.ECommerce.Dto.CartDTO;
import com.example.ECommerce.Dto.ProductDTO;
import com.example.ECommerce.Dto.ProductResponse;
import com.example.ECommerce.Exception.APIException;
import com.example.ECommerce.Exception.ResourceNotFound;
import com.example.ECommerce.Repository.CartRepo;
import com.example.ECommerce.Repository.CategoryRepo;
import com.example.ECommerce.Repository.ProductRepo;
import com.example.ECommerce.Service.CartService;
import com.example.ECommerce.Service.FileService;
import com.example.ECommerce.Service.ProductService;
import com.example.ECommerce.entityes.Cart;
import com.example.ECommerce.entityes.Category;
import com.example.ECommerce.entityes.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {
	
	private FileService fileService;
	private CartService cartService;
	private ProductRepo productRepo;
	private CartRepo cartRepo;
	private CategoryRepo categoryRepo;
	private ModelMapper modelMapper;
	@Value("${project.image}")
	private String path;
	
	@Override
	public ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
		
		Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
		
		Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
		
		Page<Product> pageProducts = productRepo.findAll(pageDetails);
		
		List<Product> products = pageProducts.getContent();
		
		List<ProductDTO> productDTOs = products.stream().map(product -> modelMapper.map(product, ProductDTO.class)).collect(Collectors.toList());
		
		ProductResponse productResponse = new ProductResponse();
		
		productResponse.setContent(productDTOs);
		productResponse.setPageNumber(pageProducts.getNumber());
		productResponse.setPageSize(pageProducts.getSize());
		productResponse.setTotalElements(pageProducts.getTotalElements());
		productResponse.setTotalPages(pageProducts.getTotalPages());
		productResponse.setLastPage(pageProducts.isLast());
		
		return productResponse;
		
	}
	
	@Override
	public ProductResponse searchByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) throws APIException, ResourceNotFound {
		
		Category category = categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFound("Category", "categoryId", categoryId));
		
		Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
		
		Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
		
		Page<Product> pageProducts = productRepo.findAll(pageDetails);
		
		List<Product> products = pageProducts.getContent();
		
		if (products.size() == 0) {
			throw new APIException(category.getCategoryName() + " category doesn't contain any products !!!");
		}
		
		List<ProductDTO> productDTOs = products.stream().map(p -> modelMapper.map(p, ProductDTO.class)).collect(Collectors.toList());
		
		ProductResponse productResponse = new ProductResponse();
		
		productResponse.setContent(productDTOs);
		productResponse.setPageNumber(pageProducts.getNumber());
		productResponse.setPageSize(pageProducts.getSize());
		productResponse.setTotalElements(pageProducts.getTotalElements());
		productResponse.setTotalPages(pageProducts.getTotalPages());
		productResponse.setLastPage(pageProducts.isLast());
		
		return productResponse;
	}
	
	@Override
	public ProductResponse searchProductByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) throws APIException {
		Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
		
		Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
		
		Page<Product> pageProducts = productRepo.findByProductNameLike(keyword, pageDetails);
		
		List<Product> products = pageProducts.getContent();
		
		if (products.size() == 0) {
			throw new APIException("Products not found with keyword: " + keyword);
		}
		
		List<ProductDTO> productDTOs = products.stream().map(p -> modelMapper.map(p, ProductDTO.class)).collect(Collectors.toList());
		
		ProductResponse productResponse = new ProductResponse();
		
		productResponse.setContent(productDTOs);
		productResponse.setPageNumber(pageProducts.getNumber());
		productResponse.setPageSize(pageProducts.getSize());
		productResponse.setTotalElements(pageProducts.getTotalElements());
		productResponse.setTotalPages(pageProducts.getTotalPages());
		productResponse.setLastPage(pageProducts.isLast());
		
		return productResponse;
	}
	
	@Override
	public ProductDTO updateProduct(Long productId, Product product) throws APIException, ResourceNotFound {
		Product productFromDB = productRepo.findById(productId).orElseThrow(() -> new ResourceNotFound("Product", "productId", productId));
		
		if (productFromDB == null) {
			throw new APIException("Product not found with productId: " + productId);
		}
		
		product.setImage(productFromDB.getImage());
		product.setProductId(productId);
		product.setCategory(productFromDB.getCategory());
		
		double specialPrice = product.getPrice() - ((product.getDiscount() * 0.01) * product.getPrice());
		product.setSpecialPrice(specialPrice);
		
		Product savedProduct = productRepo.save(product);
		
		List<Cart> carts = cartRepo.findCartsByProductId(productId);
		
		List<CartDTO> cartDTOs = carts.stream().map(cart -> {
			CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
			
			List<ProductDTO> products = cart.getCartItems().stream().map(p -> modelMapper.map(p.getProduct(), ProductDTO.class)).collect(Collectors.toList());
			
			cartDTO.setProducts(products);
			
			return cartDTO;
			
		}).collect(Collectors.toList());
		
		cartDTOs.forEach(cart -> {
			try {
				cartService.updateProductInCarts(cart.getCartId(), productId);
			} catch (ResourceNotFound e) {
				throw new RuntimeException(e);
			} catch (APIException e) {
				throw new RuntimeException(e);
			}
		});
		
		return modelMapper.map(savedProduct, ProductDTO.class);
	}
	
	@Override
	public ProductDTO updateProductImage(Long productId, MultipartFile image) throws ResourceNotFound, APIException, IOException {
		
		Product productFromDB = productRepo.findById(productId).orElseThrow(() -> new ResourceNotFound("Product", "productId", productId));
		
		if (productFromDB == null) {
			throw new APIException("Product not found with productId: " + productId);
		}
		
		String fileName = fileService.uploadImage(path, image);
		
		productFromDB.setImage(fileName);
		
		Product updatedProduct = productRepo.save(productFromDB);
		
		return modelMapper.map(updatedProduct, ProductDTO.class);
	}
	
	@Override
	public String deleteProduct(Long productId) throws ResourceNotFound {
		Product product = productRepo.findById(productId).orElseThrow(() -> new ResourceNotFound("Product", "productId", productId));
		
		List<Cart> carts = cartRepo.findCartsByProductId(productId);
		
		carts.forEach(cart -> {
			try {
				cartService.deleteProductFromCart(cart.getCartId(), productId);
			} catch (ResourceNotFound e) {
				throw new RuntimeException(e);
			}
		});
		
		productRepo.delete(product);
		
		return "Product with productId: " + productId + " deleted successfully !!!";
		
	}
	
	@Override
	public ProductDTO addProduct(Long categoryId, Product product) throws APIException, ResourceNotFound {
		Category category = categoryRepo.findById(categoryId).orElseThrow(() -> new ResourceNotFound("category", "categoryId", categoryId));
		
		boolean isProductNotPresent = true;
		
		List<Product> products = category.getProducts();
		
		for (int i = 0; i < products.size(); i++) {
			if (products.get(i).getProductName().equals(product.getProductName()) && products.get(i).getDescription().equals(product.getDescription())) {
				
				isProductNotPresent = false;
				break;
			}
		}
		
		if (isProductNotPresent) {
			product.setImage("default.png");
			
			product.setCategory(category);
			
			double specialPrice = product.getPrice() - ((product.getDiscount() * 0.01) * product.getPrice());
			product.setSpecialPrice(specialPrice);
			
			Product savedProduct = productRepo.save(product);
			
			return modelMapper.map(savedProduct, ProductDTO.class);
		} else {
			throw new APIException("Product already exists !!!");
		}
	}
}
