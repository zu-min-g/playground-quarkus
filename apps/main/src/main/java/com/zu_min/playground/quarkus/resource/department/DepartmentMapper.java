package com.zu_min.playground.quarkus.resource.department;

import java.util.List;

import com.zu_min.playground.quarkus.entity.Department;
import com.zu_min.playground.quarkus.entity.Employee;
import com.zu_min.playground.quarkus.entity.Project;

import org.mapstruct.Mapper;

/**
 * 部門情報マッパー。
 */
@Mapper
public interface DepartmentMapper {
    DepartmentDto toDto(Department entity);

    List<DepartmentDto> toDto(List<Department> entity);

    DepartmentDto.DepartmentDtoEmployee toEmployeeDto(Employee entity);

    List<DepartmentDto.DepartmentDtoEmployee> toEmployeeDto(List<Employee> entity);

    DepartmentDto.DepartmentDtoProject toProjectDto(Project entity);

    List<DepartmentDto.DepartmentDtoProject> toProjectDto(List<Project> entity);

    Department toEntity(DepartmentDto obj);

    Employee toEntity(DepartmentDto.DepartmentDtoEmployee obj);

    Project toEntity(DepartmentDto.DepartmentDtoProject obj);

}
