package com.hazerta.SpendwiseBack.controller;

import com.hazerta.SpendwiseBack.dto.ForecastTemplateDto;
import com.hazerta.SpendwiseBack.entity.ForecastTemplate;
import com.hazerta.SpendwiseBack.entity.User;
import com.hazerta.SpendwiseBack.repository.UserRepository;
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

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/mine")
    public List<ForecastTemplateDto> getUserTemplates(Authentication authentication) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
        return forecastTemplateService.getUserTemplatesByUserId(user.getId());
    }

    @GetMapping("/{id}")
    public ForecastTemplate getTemplateById(@PathVariable Long id) {
        return forecastTemplateService.getTemplateById(id);
    }

    @org.springframework.web.bind.annotation.PostMapping
    public ForecastTemplate createTemplate(
            @org.springframework.web.bind.annotation.RequestBody ForecastTemplate template,
            Authentication authentication) {
        return forecastTemplateService.createTemplate(template, authentication.getName());
    }

    @org.springframework.web.bind.annotation.PutMapping("/{id}")
    public ForecastTemplate updateTemplate(@PathVariable Long id,
            @org.springframework.web.bind.annotation.RequestBody ForecastTemplate template,
            Authentication authentication) {
        return forecastTemplateService.updateTemplate(id, template, authentication.getName());
    }

    @org.springframework.web.bind.annotation.DeleteMapping("/{id}")
    public void deleteTemplate(@PathVariable Long id, Authentication authentication) {
        forecastTemplateService.deleteTemplate(id, authentication.getName());
    }
}