package com.zu_min.playground.quarkus.extension.runtime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;

/**
 * 果物エンティティ。
 */
@Entity
public class Fruit extends PanacheEntityBase {
    @Id
    @GeneratedValue
    public Long id;

    public String name;
}
