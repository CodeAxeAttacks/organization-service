package com.example.organization.repository;

import com.example.organization.model.Organization;
import com.example.organization.model.OrganizationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long>,
        JpaSpecificationExecutor<Organization> {

    // Удаление по fullName
    List<Organization> deleteByFullName(String fullName);

    // Подсчет организаций с типом больше заданного
    @Query("SELECT COUNT(o) FROM Organization o WHERE o.type > :type")
    long countByTypeGreaterThan(@Param("type") OrganizationType type);

    // Средний оборот (исключая null)
    @Query("SELECT AVG(o.annualTurnover) FROM Organization o WHERE o.annualTurnover IS NOT NULL")
    Double calculateAverageAnnualTurnover();

    // Количество организаций с не-null оборотом
    @Query("SELECT COUNT(o) FROM Organization o WHERE o.annualTurnover IS NOT NULL")
    long countOrganizationsWithTurnover();
}