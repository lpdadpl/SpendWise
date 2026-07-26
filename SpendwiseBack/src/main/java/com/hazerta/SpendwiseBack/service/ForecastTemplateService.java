package com.hazerta.SpendwiseBack.service;

import com.hazerta.SpendwiseBack.dto.ForecastTemplateDto;
import com.hazerta.SpendwiseBack.dto.CreateForecastTemplateDto;
import com.hazerta.SpendwiseBack.dto.UpdateForecastTemplateDto;
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
        String ownerName = forecastTemplateOwnedRepository.findAll().stream()
                .filter(owned -> owned.getTemplate().getId().equals(template.getId()))
                .map(owned -> owned.getUser().getUsername())
                .findFirst()
                .orElse("Comunidad");
        return new ForecastTemplateDto(
                template.getId(),
                template.getName(),
                template.getDescription(),
                template.getStructure(),
                template.getIsPublic(),
                template.getCreatedAt(),
                ownerName);
    }

    @Transactional(readOnly = true)
    public ForecastTemplateDto getTemplateById(Long id, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
        ForecastTemplate template = forecastTemplateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Template not found with id: " + id));
        boolean isOwner = forecastTemplateOwnedRepository.existsByUser_IdAndTemplate_Id(user.getId(), id);
        if (!isOwner && !Boolean.TRUE.equals(template.getIsPublic())) {
            throw new org.springframework.security.access.AccessDeniedException("Template access denied");
        }
        return mapToDto(template);
    }

    @Transactional(readOnly = true)
    public List<ForecastTemplateDto> searchPublicTemplates(String search) {
        List<ForecastTemplate> templates = (search == null || search.isBlank())
                ? forecastTemplateRepository.findByIsPublicTrueOrderByCreatedAtDesc()
                : forecastTemplateRepository.findByIsPublicTrueAndNameContainingIgnoreCaseOrderByCreatedAtDesc(search);
        return templates.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Transactional
    public ForecastTemplateDto createTemplate(CreateForecastTemplateDto dto, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        ForecastTemplate template = new ForecastTemplate();
        template.setName(dto.name());
        template.setDescription(dto.description());
        template.setStructure(dto.structure());
        template.setIsPublic(dto.isPublic());
        ForecastTemplate savedTemplate = forecastTemplateRepository.save(template);

        ForecastTemplateOwned ownership = new ForecastTemplateOwned();
        ownership.setUser(user);
        ownership.setTemplate(savedTemplate);
        forecastTemplateOwnedRepository.save(ownership);

        return mapToDto(savedTemplate);
    }

    @Transactional
    public ForecastTemplateDto updateTemplate(Long id, UpdateForecastTemplateDto templateDetails, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        if (!forecastTemplateOwnedRepository.existsByUser_IdAndTemplate_Id(user.getId(), id)) {
            throw new org.springframework.security.access.AccessDeniedException("User not authorized to update this template");
        }

        ForecastTemplate template = forecastTemplateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Template not found with id: " + id));
        template.setName(templateDetails.name());
        template.setDescription(templateDetails.description());
        template.setIsPublic(templateDetails.isPublic());
        template.setStructure(templateDetails.structure());

        return mapToDto(forecastTemplateRepository.save(template));
    }

    @Transactional
    public ForecastTemplateDto copyTemplate(Long id, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
        ForecastTemplate source = forecastTemplateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Template not found with id: " + id));
        if (!Boolean.TRUE.equals(source.getIsPublic())
                && !forecastTemplateOwnedRepository.existsByUser_IdAndTemplate_Id(user.getId(), id)) {
            throw new org.springframework.security.access.AccessDeniedException("Template access denied");
        }

        ForecastTemplate copy = new ForecastTemplate();
        copy.setName(source.getName() + " (copia)");
        copy.setDescription(source.getDescription());
        copy.setStructure(source.getStructure());
        copy.setIsPublic(false);
        ForecastTemplate saved = forecastTemplateRepository.save(copy);

        ForecastTemplateOwned ownership = new ForecastTemplateOwned();
        ownership.setUser(user);
        ownership.setTemplate(saved);
        forecastTemplateOwnedRepository.save(ownership);
        return mapToDto(saved);
    }

    @Transactional
    public void deleteTemplate(Long id, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        ForecastTemplateOwned ownership = forecastTemplateOwnedRepository.findByUser_IdAndTemplate_Id(user.getId(), id)
                .orElseThrow(() -> new org.springframework.security.access.AccessDeniedException("User not authorized to delete this template"));

        // Delete ownership first
        forecastTemplateOwnedRepository.delete(ownership);

        // Delete template
        forecastTemplateRepository.deleteById(id);
    }
}
