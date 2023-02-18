package com.zu_min.playground.quarkus.extension.runtime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 果物エンティティ。
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
public class Fruit extends PanacheEntityBase {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

}
