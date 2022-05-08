package com.zu_min.playground.quarkus.resource.employee;

import java.util.List;

import com.zu_min.playground.quarkus.entity.Employee;

import org.mapstruct.Mapper;

/**
 * 社員情報マッパー。
 */
@Mapper
public interface EmployeeMapper {
    EmployeeDto toDto(Employee entity);

    List<EmployeeDto> toDto(List<Employee> entity);

    Employee toEntity(EmployeeDto obj);
}
