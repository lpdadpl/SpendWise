package com.hazerta.SpendwiseBack.repository;

import com.hazerta.SpendwiseBack.entity.ForecastTemplateOwned;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ForecastTemplateOwnedRepository extends JpaRepository<ForecastTemplateOwned, Long> {
    List<ForecastTemplateOwned> findByUser_Username(String username);

    List<ForecastTemplateOwned> findByUser_Id(Long userId);
}
