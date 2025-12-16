package com.hazerta.SpendwiseBack.controller;

import com.hazerta.SpendwiseBack.dto.CategoryDto;
import com.hazerta.SpendwiseBack.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAll(Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(categoryService.findAllByUsername(username));
    }
}
