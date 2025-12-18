package com.hazerta.SpendwiseBack.controller;

import com.hazerta.SpendwiseBack.dto.TransactionTypeDto;
import com.hazerta.SpendwiseBack.service.TransactionTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transaction-types")
public class TransactionTypeController {

    private final TransactionTypeService transactionTypeService;

    public TransactionTypeController(TransactionTypeService transactionTypeService) {
        this.transactionTypeService = transactionTypeService;
    }

    @GetMapping
    public ResponseEntity<List<TransactionTypeDto>> getAll(Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(transactionTypeService.findAllByUsername(username));
    }
}
