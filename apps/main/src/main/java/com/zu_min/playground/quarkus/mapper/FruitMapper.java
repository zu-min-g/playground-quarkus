package com.zu_min.playground.quarkus.mapper;

import com.zu_min.playground.quarkus.dto.FruitDto;
import com.zu_min.playground.quarkus.extension.runtime.Fruit;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * 果物用のマッパー。
 */
@Mapper(componentModel = "cdi")
public interface FruitMapper {
    @Mapping(target = "id", ignore = true)
    void copyTo(@MappingTarget Fruit entity, FruitDto dto);

    FruitDto createFrom(Fruit entity);

    @Mapping(target = "id", ignore = true)
    Fruit createFrom(FruitDto dto);
}
