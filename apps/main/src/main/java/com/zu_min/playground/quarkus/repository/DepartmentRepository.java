package com.zu_min.playground.quarkus.repository;

import javax.enterprise.context.ApplicationScoped;

import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase;

import com.zu_min.playground.quarkus.entity.Department;

/**
 * 部門情報関連処理。
 */
@ApplicationScoped
public class DepartmentRepository implements PanacheRepositoryBase<Department, Long> {

}
