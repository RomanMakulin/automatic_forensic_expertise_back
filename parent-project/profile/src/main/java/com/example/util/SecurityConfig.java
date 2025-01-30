package com.example.util;


import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Конфигурация безопасности приложения.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Константы Keycloak.
     */
    private final KeycloakConsts keycloakConsts;

    /**
     * Конструктор класса SecurityConfig.
     *
     * @param keycloakConsts константы Keycloak
     */
    public SecurityConfig(KeycloakConsts keycloakConsts) {
        this.keycloakConsts = keycloakConsts;
    }

    /**
     * Настройка цепочки фильтров безопасности.
     *
     * @param http объект HttpSecurity для настройки
     * @return настроенная цепочка фильтров безопасности
     * @throws Exception если возникает ошибка при настройке
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Отключение CSRF, так как мы используем JWT
                .csrf(AbstractHttpConfigurer::disable)

                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/**").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(Customizer.withDefaults()) // Проверка JWT токена с учетом issuer-uri
                );

        return http.build();
    }

    /**
     * Создание клиента Keycloak для администрирования.
     *
     * @return клиент Keycloak для администрирования
     */
    @Bean
    public Keycloak keycloakAdminClient() {
        return KeycloakBuilder.builder()
                .serverUrl(keycloakConsts.getAuthServerUrlAdmin())
                .realm(keycloakConsts.getRealmAdmin())
                .clientId(keycloakConsts.getClientIdAdmin())
                .username(keycloakConsts.getUsernameAdmin())
                .password(keycloakConsts.getPasswordAdmin())
                .build();
    }
}
