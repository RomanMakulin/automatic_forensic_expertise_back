package com.example.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "plan")  // Указываем имя таблицы в базе данных
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", columnDefinition = "UUID")
    private UUID id;  // UUID в PostgreSQL

    @Column(name = "name", nullable = false, length = 100)
    private String name;  // Название тарифного плана

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;  // Описание тарифного плана

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;  // Цена тарифа с точностью до 2 знаков после запятой

    @Column(name = "storage_limit", nullable = false)
    private int storageLimit;  // Лимит хранения (например, в мегабайтах или гигабайтах)

}
