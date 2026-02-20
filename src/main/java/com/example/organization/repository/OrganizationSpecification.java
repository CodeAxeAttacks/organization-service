package com.example.organization.repository;

import com.example.organization.model.Organization;
import com.example.organization.model.OrganizationType;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class OrganizationSpecification {

    public static Specification<Organization> hasId(Long id) {
        return (root, query, cb) -> id == null ? null : cb.equal(root.get("id"), id);
    }

    public static Specification<Organization> nameLike(String name) {
        return (root, query, cb) -> name == null ? null :
                cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Organization> hasCoordinateX(Double x) {
        return (root, query, cb) -> x == null ? null :
                cb.equal(root.get("coordinates").get("x"), x);
    }

    public static Specification<Organization> hasCoordinateY(Integer y) {
        return (root, query, cb) -> y == null ? null :
                cb.equal(root.get("coordinates").get("y"), y);
    }

    public static Specification<Organization> createdAfter(LocalDateTime date) {
        return (root, query, cb) -> date == null ? null :
                cb.greaterThanOrEqualTo(root.get("creationDate"), date);
    }

    public static Specification<Organization> createdBefore(LocalDateTime date) {
        return (root, query, cb) -> date == null ? null :
                cb.lessThanOrEqualTo(root.get("creationDate"), date);
    }

    public static Specification<Organization> annualTurnoverMin(Double min) {
        return (root, query, cb) -> min == null ? null :
                cb.greaterThanOrEqualTo(root.get("annualTurnover"), min);
    }

    public static Specification<Organization> annualTurnoverMax(Double max) {
        return (root, query, cb) -> max == null ? null :
                cb.lessThanOrEqualTo(root.get("annualTurnover"), max);
    }

    public static Specification<Organization> fullNameLike(String fullName) {
        return (root, query, cb) -> fullName == null ? null :
                cb.like(cb.lower(root.get("fullName")), "%" + fullName.toLowerCase() + "%");
    }

    public static Specification<Organization> employeesCountMin(Integer min) {
        return (root, query, cb) -> min == null ? null :
                cb.greaterThanOrEqualTo(root.get("employeesCount"), min);
    }

    public static Specification<Organization> employeesCountMax(Integer max) {
        return (root, query, cb) -> max == null ? null :
                cb.lessThanOrEqualTo(root.get("employeesCount"), max);
    }

    public static Specification<Organization> hasType(OrganizationType type) {
        return (root, query, cb) -> type == null ? null :
                cb.equal(root.get("type"), type);
    }

    public static Specification<Organization> streetLike(String street) {
        return (root, query, cb) -> street == null ? null :
                cb.like(cb.lower(root.get("officialAddress").get("street")), "%" + street.toLowerCase() + "%");
    }
}