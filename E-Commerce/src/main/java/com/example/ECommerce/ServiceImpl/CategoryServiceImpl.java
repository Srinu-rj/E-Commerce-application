package com.example.ECommerce.ServiceImpl;

import com.example.ECommerce.Dto.CategoryDTO;
import com.example.ECommerce.Dto.CategoryResponse;
import com.example.ECommerce.Exception.APIException;
import com.example.ECommerce.Exception.ResourceNotFound;
import com.example.ECommerce.Exception.categoryNotFoundException;
import com.example.ECommerce.Repository.CartRepo;
import com.example.ECommerce.Repository.CategoryRepo;
import com.example.ECommerce.Repository.UserRepo;
import com.example.ECommerce.Service.CategoryService;
import com.example.ECommerce.Service.ProductService;
import com.example.ECommerce.entityes.Category;
import com.example.ECommerce.entityes.Product;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
	
	private final CategoryRepo categoryRepo;
	private final ProductService productService;
	private final ModelMapper modelMapper;
	
	@Override
	public CategoryDTO createCategory(Category category) throws APIException {
		Category savedCategory = categoryRepo.findByCategoryName(category.getCategoryName());
		
		if (savedCategory != null) {
			throw new APIException("Category with the name '" + category.getCategoryName() + "' already exists !!!");
		}
		savedCategory = categoryRepo.save(category);
		
		return modelMapper.map(savedCategory, CategoryDTO.class);
	}
	
	@Override
	public CategoryResponse getCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) throws APIException {
		
		Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
				?
				Sort.by(sortBy).ascending() :
				Sort.by(sortBy).descending();
		Pageable pageDetails = PageRequest.of(pageNumber, pageSize);
		
		Page<Category> pageCategories = categoryRepo.findAll(pageDetails);
		
		List<Category> categories = pageCategories.getContent();
		
		if (categories.size() == 0) {
			throw new APIException("No category is created till now");
		}
		
		List<CategoryDTO> categoryDTOs = categories.stream()
				.map(category -> modelMapper.map(category, CategoryDTO.class)).collect(Collectors.toList());
		
		CategoryResponse categoryResponse = new CategoryResponse();
		
		categoryResponse.setContent(categoryDTOs);
		categoryResponse.setPageNumber(pageCategories.getNumber());
		categoryResponse.setPageSize(pageCategories.getSize());
		categoryResponse.setTotalElements(pageCategories.getTotalElements());
		categoryResponse.setTotalPages(pageCategories.getTotalPages());
		categoryResponse.setLastPage(pageCategories.isLast());
		
		return categoryResponse;
		
	}
	
	@Override
	public CategoryDTO updateCategory(Category category, Long categoryId) throws APIException {
//		Category savedCategory = categoryRepo.findById(categoryId)
//				.orElseThrow(() -> new APIException("Category", category));
		Category savedCategory = categoryRepo.findById(categoryId).orElse(null);
		
		category.setCategoryId(categoryId);
		
		savedCategory = categoryRepo.save(category);
		
		return modelMapper.map(savedCategory, CategoryDTO.class);
		
	}
	
	@Override
	public String deleteCategory(Long categoryId) {
		Category category = categoryRepo.findById(categoryId)
				.orElse(null);
		
		List<Product> products = category.getProducts();
		
		products.forEach(product -> {
			try {
				productService.deleteProduct(product.getProductId());
			} catch (ResourceNotFound e) {
				throw new RuntimeException(e);
			}
		});
		
		categoryRepo.delete(category);
		
		return "Category with categoryId: " + categoryId + " deleted successfully !!!";
	}
}

