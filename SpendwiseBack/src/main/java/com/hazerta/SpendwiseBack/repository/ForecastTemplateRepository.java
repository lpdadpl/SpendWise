package com.hazerta.SpendwiseBack.repository;

import com.hazerta.SpendwiseBack.entity.ForecastTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ForecastTemplateRepository extends JpaRepository<ForecastTemplate, Long> {

    List<ForecastTemplate> findByOwnerId(Long ownerId);
}
