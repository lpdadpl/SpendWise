package com.hazerta.SpendwiseBack.service;

import com.hazerta.SpendwiseBack.dto.CreateTransactionDto;
import com.hazerta.SpendwiseBack.dto.TransactionDto;
import com.hazerta.SpendwiseBack.entity.Category;
import com.hazerta.SpendwiseBack.entity.Transaction;
import com.hazerta.SpendwiseBack.entity.User;
import com.hazerta.SpendwiseBack.repository.CategoryRepository;
import com.hazerta.SpendwiseBack.repository.TransactionRepository;
import com.hazerta.SpendwiseBack.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public TransactionService(TransactionRepository transactionRepository, UserRepository userRepository,
            CategoryRepository categoryRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public List<TransactionDto> findAllByUsername(String username) {
        User user = getUser(username);
        return transactionRepository.findByUserId(user.getId()).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public TransactionDto create(String username, CreateTransactionDto dto) {
        User user = getUser(username);
        Category category = categoryRepository.findByIdAndUserId(dto.categoryId(), user.getId())
                .orElseThrow(() -> new RuntimeException("Category not found or access denied"));

        Transaction transaction = new Transaction();
        transaction.setDescription(dto.description());
        transaction.setAmount(dto.amount());
        transaction.setTransactionType(dto.transactionType());
        transaction.setUser(user);
        transaction.setCategory(category);

        Transaction saved = transactionRepository.save(transaction);
        return mapToDto(saved);
    }

    private User getUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    private TransactionDto mapToDto(Transaction t) {
        return new TransactionDto(
                t.getId(),
                t.getDescription(),
                t.getAmount(),
                t.getTransactionType(),
                t.getCategory().getId(),
                t.getCategory().getName());
    }
}
