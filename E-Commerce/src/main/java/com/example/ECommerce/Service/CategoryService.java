package com.example.ECommerce.Service;

import com.example.ECommerce.Dto.CategoryDTO;
import com.example.ECommerce.Dto.CategoryResponse;
import com.example.ECommerce.Exception.APIException;
import com.example.ECommerce.entityes.Category;
import org.springframework.stereotype.Service;

@Service
public interface CategoryService {
	CategoryDTO createCategory(Category category) throws APIException;
	
	CategoryResponse getCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) throws APIException;
	
	CategoryDTO updateCategory(Category category, Long categoryId) throws APIException;
	
	String deleteCategory(Long categoryId);
	
	
}
