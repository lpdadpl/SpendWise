package com.hazerta.SpendwiseBack.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "forecast_template_owned")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForecastTemplateOwned {

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false) // Columna FK en la tabla
    private User user; // El usuario dueño de esta categoría

    @JoinColumn(name = "forecast_id", nullable = false)
    private ForecastTemplate template;

}
