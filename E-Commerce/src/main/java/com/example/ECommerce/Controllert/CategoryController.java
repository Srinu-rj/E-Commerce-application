package com.example.ECommerce.Controllert;

import com.example.ECommerce.Config.AppConstants;
import com.example.ECommerce.Dto.CategoryDTO;
import com.example.ECommerce.Dto.CategoryResponse;
import com.example.ECommerce.Exception.APIException;
import com.example.ECommerce.Service.CategoryService;
import com.example.ECommerce.entityes.Category;
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
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "E-Commerce Application")
public class CategoryController {

	@Autowired
	private CategoryService categoryService;

	@PostMapping("/admin/category")
	public CategoryDTO createCategory(@Valid @RequestBody Category category) throws APIException {
	return 	 categoryService.createCategory(category);
	}

	@GetMapping("/public/categories")
	public CategoryResponse getCategories(
			@RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
			@RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
			@RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_CATEGORIES_BY) String sortBy,
			@RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR/*, required = false*/) String sortOrder) throws APIException {

	return categoryService.getCategories(pageNumber, pageSize, sortBy, sortOrder);

	}

	@PutMapping("/admin/categories/{categoryId}")
	public CategoryDTO updateCategory(@RequestBody Category category,
			@PathVariable Long categoryId) throws APIException {
		return categoryService.updateCategory(category, categoryId);
	}

	@DeleteMapping("/admin/categories/{categoryId}")
	public String deleteCategory(@PathVariable Long categoryId) {
		return categoryService.deleteCategory(categoryId);
	}

}
