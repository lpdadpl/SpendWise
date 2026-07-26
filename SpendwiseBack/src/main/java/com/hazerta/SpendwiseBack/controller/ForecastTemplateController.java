package com.hazerta.SpendwiseBack.controller;

import com.hazerta.SpendwiseBack.dto.ForecastTemplateDto;
import com.hazerta.SpendwiseBack.dto.CreateForecastTemplateDto;
import com.hazerta.SpendwiseBack.dto.UpdateForecastTemplateDto;
import com.hazerta.SpendwiseBack.entity.User;
import com.hazerta.SpendwiseBack.repository.UserRepository;
import com.hazerta.SpendwiseBack.service.ForecastTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import jakarta.validation.Valid;

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

    @GetMapping("/public")
    public List<ForecastTemplateDto> searchPublicTemplates(@RequestParam(required = false) String search) {
        return forecastTemplateService.searchPublicTemplates(search);
    }

    @GetMapping("/{id}")
    public ForecastTemplateDto getTemplateById(@PathVariable Long id, Authentication authentication) {
        return forecastTemplateService.getTemplateById(id, authentication.getName());
    }

    @org.springframework.web.bind.annotation.PostMapping
    public ForecastTemplateDto createTemplate(
            @Valid @org.springframework.web.bind.annotation.RequestBody CreateForecastTemplateDto template,
            Authentication authentication) {
        return forecastTemplateService.createTemplate(template, authentication.getName());
    }

    @org.springframework.web.bind.annotation.PutMapping("/{id}")
    public ForecastTemplateDto updateTemplate(@PathVariable Long id,
            @Valid @org.springframework.web.bind.annotation.RequestBody UpdateForecastTemplateDto template,
            Authentication authentication) {
        return forecastTemplateService.updateTemplate(id, template, authentication.getName());
    }

    @org.springframework.web.bind.annotation.PostMapping("/{id}/copy")
    public ForecastTemplateDto copyTemplate(@PathVariable Long id, Authentication authentication) {
        return forecastTemplateService.copyTemplate(id, authentication.getName());
    }

    @org.springframework.web.bind.annotation.DeleteMapping("/{id}")
    public void deleteTemplate(@PathVariable Long id, Authentication authentication) {
        forecastTemplateService.deleteTemplate(id, authentication.getName());
    }
}
