package com.hazerta.SpendwiseBack.controller;

import com.hazerta.SpendwiseBack.dto.CreateTransactionDto;
import com.hazerta.SpendwiseBack.dto.TransactionDto;
import com.hazerta.SpendwiseBack.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    public ResponseEntity<List<TransactionDto>> getAll(Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(transactionService.findAllByUsername(username));
    }

    @PostMapping
    public ResponseEntity<TransactionDto> create(@Valid @RequestBody CreateTransactionDto dto, Authentication authentication) {
        String username = authentication.getName();
        TransactionDto created = transactionService.create(username, dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }
}
