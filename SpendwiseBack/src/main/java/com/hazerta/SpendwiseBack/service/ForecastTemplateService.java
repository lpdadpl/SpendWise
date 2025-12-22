package com.hazerta.SpendwiseBack.service;

import com.hazerta.SpendwiseBack.dto.ForecastTemplateDto;
import com.hazerta.SpendwiseBack.entity.ForecastTemplate;
import com.hazerta.SpendwiseBack.entity.ForecastTemplateOwned;
import com.hazerta.SpendwiseBack.entity.User;
import com.hazerta.SpendwiseBack.repository.ForecastTemplateOwnedRepository;
import com.hazerta.SpendwiseBack.repository.ForecastTemplateRepository;
import com.hazerta.SpendwiseBack.repository.UserRepository;
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

    @Autowired
    private UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<ForecastTemplateDto> getUserTemplates(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
        return getUserTemplatesByUserId(user.getId());
    }

    @Transactional(readOnly = true)
    public List<ForecastTemplateDto> getUserTemplatesByUserId(Long userId) {
        return forecastTemplateOwnedRepository.findByUser_Id(userId)
                .stream()
                .map(ForecastTemplateOwned::getTemplate)
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private ForecastTemplateDto mapToDto(ForecastTemplate template) {
        return new ForecastTemplateDto(
                template.getId(),
                template.getName(),
                template.getDescription(),
                template.getIsPublic(),
                template.getCreatedAt());
    }

    @Transactional(readOnly = true)
    public ForecastTemplate getTemplateById(Long id) {
        return forecastTemplateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Template not found with id: " + id));
    }

    @Transactional
    public ForecastTemplate createTemplate(ForecastTemplate template, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        ForecastTemplate savedTemplate = forecastTemplateRepository.save(template);

        ForecastTemplateOwned ownership = new ForecastTemplateOwned();
        ownership.setUser(user);
        ownership.setTemplate(savedTemplate);
        forecastTemplateOwnedRepository.save(ownership);

        return savedTemplate;
    }

    @Transactional
    public ForecastTemplate updateTemplate(Long id, ForecastTemplate templateDetails, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        // Check ownership
        boolean isOwner = forecastTemplateOwnedRepository.findByUser_Id(user.getId())
                .stream()
                .anyMatch(owned -> owned.getTemplate().getId().equals(id));

        if (!isOwner) {
            throw new RuntimeException("User not authorized to update this template");
        }

        ForecastTemplate template = getTemplateById(id);
        template.setName(templateDetails.getName());
        template.setDescription(templateDetails.getDescription());
        template.setIsPublic(templateDetails.getIsPublic());
        template.setStructure(templateDetails.getStructure());

        return forecastTemplateRepository.save(template);
    }
}
