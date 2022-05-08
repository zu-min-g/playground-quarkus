package com.zu_min.playground.quarkus.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;

import lombok.Getter;
import lombok.Setter;

/**
 * プロジェクト。
 */
@Entity
@Getter
@Setter
public class Project extends PanacheEntityBase {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @ManyToMany
    private List<Employee> employees = new ArrayList<>();

}
