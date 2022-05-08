package com.zu_min.playground.quarkus.resource.department;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
 * 部門情報。
 */
@Data
public class DepartmentDto {
    @Schema(readOnly = true)
    private Long id;

    @NotNull
    private List<@Valid DepartmentDtoEmployee> employees;

    /**
     * 部門情報の社員。
     */
    @Data
    public static class DepartmentDtoEmployee {
        @Schema(readOnly = true)
        private Long id;

        @NotNull
        private List<@Valid DepartmentDtoProject> projects;

    }

    /**
     * 部門情報のプロジェクト。
     */
    @Data
    public static class DepartmentDtoProject {
        @Schema(readOnly = true)
        private Long id;

    }
}
