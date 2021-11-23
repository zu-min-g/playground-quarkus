package com.zu_min.playground.quarkus.extension.runtime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

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
