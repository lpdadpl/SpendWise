package com.hazerta.SpendwiseBack.service;

import com.hazerta.SpendwiseBack.dto.CategoryDto;
import com.hazerta.SpendwiseBack.entity.User;
import com.hazerta.SpendwiseBack.repository.CategoryRepository;
import com.hazerta.SpendwiseBack.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public CategoryService(CategoryRepository categoryRepository, UserRepository userRepository) {
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<CategoryDto> findAllByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return categoryRepository.findByUserId(user.getId()).stream()
                .map(c -> new CategoryDto(c.getId(), c.getName(), c.getType()))
                .collect(Collectors.toList());
    }
}
