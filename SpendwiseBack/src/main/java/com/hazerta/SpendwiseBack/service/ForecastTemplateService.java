package com.hazerta.SpendwiseBack.service;

import com.hazerta.SpendwiseBack.entity.ForecastTemplate;
import com.hazerta.SpendwiseBack.entity.ForecastTemplateOwned;
import com.hazerta.SpendwiseBack.repository.ForecastTemplateOwnedRepository;
import com.hazerta.SpendwiseBack.repository.ForecastTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ForecastTemplateService {

    @Autowired
    private ForecastTemplateRepository forecastTemplateRepository;

    @Autowired
    private ForecastTemplateOwnedRepository forecastTemplateOwnedRepository;

    @Transactional(readOnly = true)
    public List<ForecastTemplate> getUserTemplates(String username) {
        return forecastTemplateOwnedRepository.findByUser_Username(username)
                .stream()
                .map(ForecastTemplateOwned::getTemplate)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ForecastTemplate getTemplateById(Long id) {
        return forecastTemplateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Template not found with id: " + id));
    }
}
