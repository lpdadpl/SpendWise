package com.hazerta.SpendwiseBack.service;

import com.hazerta.SpendwiseBack.dto.TransactionTypeDto;
import com.hazerta.SpendwiseBack.entity.User;
import com.hazerta.SpendwiseBack.repository.TransactionTypeRepository;
import com.hazerta.SpendwiseBack.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionTypeService {

    private final TransactionTypeRepository transactionTypeRepository;
    private final UserRepository userRepository;

    public TransactionTypeService(TransactionTypeRepository transactionTypeRepository, UserRepository userRepository) {
        this.transactionTypeRepository = transactionTypeRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<TransactionTypeDto> findAllByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return transactionTypeRepository.findByUserId(user.getId()).stream()
                .map(tt -> new TransactionTypeDto(tt.getId(), tt.getName(), tt.getKind()))
                .collect(Collectors.toList());
    }
}
