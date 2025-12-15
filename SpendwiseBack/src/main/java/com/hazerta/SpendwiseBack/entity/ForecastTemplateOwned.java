package com.hazerta.SpendwiseBack.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "forecast_template_owned")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForecastTemplateOwned {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long like_id;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "forecast_id", nullable = false)
    private ForecastTemplate template;

}
