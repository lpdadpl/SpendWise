package com.hazerta.SpendwiseBack.service;

import com.hazerta.SpendwiseBack.dto.CreateTransactionDto;
import com.hazerta.SpendwiseBack.dto.TransactionDto;
import com.hazerta.SpendwiseBack.entity.TransactionType;
import com.hazerta.SpendwiseBack.entity.Transaction;
import com.hazerta.SpendwiseBack.entity.User;
import com.hazerta.SpendwiseBack.repository.TransactionTypeRepository;
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
    private final TransactionTypeRepository transactionTypeRepository;

    public TransactionService(TransactionRepository transactionRepository, UserRepository userRepository,
            TransactionTypeRepository transactionTypeRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
        this.transactionTypeRepository = transactionTypeRepository;
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
        TransactionType transactionType = transactionTypeRepository
                .findByIdAndUserId(dto.transactionTypeId(), user.getId())
                .orElseThrow(() -> new RuntimeException("Transaction Type not found or access denied"));

        Transaction transaction = new Transaction();
        transaction.setDescription(dto.description());
        transaction.setAmount(dto.amount());
        transaction.setTransactionKind(dto.transactionKind());
        transaction.setUser(user);
        transaction.setTransactionType(transactionType);

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
                t.getTransactionKind(),
                t.getTransactionType().getId(),
                t.getTransactionType().getName());
    }
}
