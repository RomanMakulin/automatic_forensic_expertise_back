package com.example.tariff_plans.service;

import com.example.tariff_plans.model.Plan;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;

public interface ProfilePlanService {

    /**
     * Покупка нового тарифного плана пользователем.
     * Если у пользователя уже есть активный тариф, будет выброшено исключение.
     *
     * @param profileId ID профиля пользователя
     * @param planId ID тарифного плана, который хочет купить пользователь
     * @return купленный тарифный план
     */
    @Transactional
    Plan buyPlan(UUID profileId, UUID planId);

    /**
     * Получение текущего активного тарифного плана пользователя.
     * Если активного тарифа нет, вернется null.
     *
     * @param profileId ID профиля пользователя
     * @return активный тарифный план или null, если подписки нет
     */
    Plan getActivePlan(UUID profileId);

    /**
     * Оплата текущего активного тарифного плана пользователя.
     * Если активного плана нет, будет выброшено исключение.
     *
     * @param profileId ID профиля пользователя
     */
    @Transactional
    void payForPlan(UUID profileId);

    /**
     * Проверяет, оплачен ли текущий тарифный план пользователя.
     *
     * @param profileId ID профиля пользователя
     * @return true, если подписка оплачена, иначе false
     */
    boolean isPlanPaid(UUID profileId);

    /**
     * Получение списка всех доступных тарифных планов.
     *
     * @return список тарифных планов
     */
    List<Plan> getAvailablePlans();
}
