package com.example.adminservice.api.dto.profile;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * DTO: информация о шаблоне, который выбрал эксперт
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TemplateDto {

    /**
     * Идентификатор шаблона
     */
    private String id;

    /**
     * Название шаблона
     */
    private String name;

    /**
     * Тип шаблона
     */
    private String type;

    /**
     * Путь к шаблону
     */
    private String path;

    /**
     * Дата создания шаблона
     */
    @JsonProperty
    private String createdAt;

}
