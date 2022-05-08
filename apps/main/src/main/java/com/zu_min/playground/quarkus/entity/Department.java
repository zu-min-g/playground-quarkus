package com.zu_min.playground.quarkus.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;

import lombok.Getter;
import lombok.Setter;

/**
 * 部門。
 */
@Entity
@Getter
@Setter
public class Department extends PanacheEntityBase {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @OneToMany(mappedBy = "department")
    private List<Employee> employees = new ArrayList<>();

}
