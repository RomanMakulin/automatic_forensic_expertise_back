package com.example.tariff_plans.service;



import com.example.tariff_plans.model.Plan;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Сервис управления тарифными планами пользователей.
 */
public interface ProfilePlanService {

    /**
     * Получить список всех доступных тарифных планов.
     *
     * @return Список активных тарифов.
     */
    List<Plan> getAvailablePlans();

    /**
     * Получить текущий тарифный план пользователя.
     *
     * @param userId ID пользователя.
     * @return Текущий тарифный план, если он есть.
     */
    Optional<Plan> getCurrentPlan(UUID userId);

    /**
     * Выбрать новый тарифный план для пользователя.
     *
     * @param userId          ID пользователя.
     * @param planId          ID выбранного тарифа.
     * @param durationInMonths Количество месяцев подписки.
     */
    void selectPlan(UUID userId, UUID planId, int durationInMonths);



    /**
     * Продлить текущий тарифный план пользователя.
     *
     * @param userId          ID пользователя.
     * @param additionalMonths Количество дополнительных месяцев.
     */
    void renewPlan(UUID userId, int additionalMonths);

    /**
     * Отменить текущий тарифный план пользователя.
     *
     * @param userId ID пользователя.
     */
    void cancelPlan(UUID userId);


    /**
     * Расчет стоимости тарифа перед оплатой (учитывая скидки, налоги и т. д.).
     *
     * @param planId          ID тарифного плана.
     * @param durationInMonths Количество месяцев подписки.
     * @param userId          ID пользователя.
     * @return Итоговая сумма для оплаты.
     */
    BigDecimal calculatePlanPrice(UUID planId, int durationInMonths, UUID userId);

}
