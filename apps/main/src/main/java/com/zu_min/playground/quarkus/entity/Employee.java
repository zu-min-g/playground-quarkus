package com.zu_min.playground.quarkus.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;

import lombok.Getter;
import lombok.Setter;

/**
 * 社員。
 */
@Entity
@Getter
@Setter
public class Employee extends PanacheEntityBase {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    private Department department;

    @ManyToMany(mappedBy = "employees")
    private List<Project> projects = new ArrayList<>();

}
