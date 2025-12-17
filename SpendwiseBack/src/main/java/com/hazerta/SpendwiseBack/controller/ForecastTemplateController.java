package com.hazerta.SpendwiseBack.controller;

import com.hazerta.SpendwiseBack.entity.ForecastTemplate;
import com.hazerta.SpendwiseBack.service.ForecastTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/templates")
public class ForecastTemplateController {

    @Autowired
    private ForecastTemplateService forecastTemplateService;

    @GetMapping("/mine")
    public List<ForecastTemplate> getUserTemplates(Authentication authentication) {
        String username = authentication.getName();
        return forecastTemplateService.getUserTemplates(username);
    }

    @GetMapping("/{id}")
    public ForecastTemplate getTemplateById(@PathVariable Long id) {
        return forecastTemplateService.getTemplateById(id);
    }
}
