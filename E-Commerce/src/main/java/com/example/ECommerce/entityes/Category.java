package com.example.ECommerce.entityes;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@Table(name = "categories")
@NoArgsConstructor
@AllArgsConstructor
public class Category {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long categoryId;
	
	@NotBlank
	@Size(min = 5, message = "Category name must contain atleast 5 characters")
	private String categoryName;
	
	@OneToMany(mappedBy = "category", cascade =  CascadeType.ALL )
	private List<Product> products;
	
}
