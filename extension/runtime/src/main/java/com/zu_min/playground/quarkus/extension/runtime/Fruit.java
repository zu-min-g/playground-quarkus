package com.zu_min.playground.quarkus.extension.runtime;

import javax.persistence.Entity;
import javax.persistence.Id;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;

@Entity
public class Fruit extends PanacheEntityBase {
    @Id
    public Long id;

    public String name;
}
