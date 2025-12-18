package com.hazerta.SpendwiseBack.repository;

import com.hazerta.SpendwiseBack.entity.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionTypeRepository extends JpaRepository<TransactionType, Long> {
    List<TransactionType> findByUserId(Long userId);

    Optional<TransactionType> findByIdAndUserId(Long id, Long userId);
}
