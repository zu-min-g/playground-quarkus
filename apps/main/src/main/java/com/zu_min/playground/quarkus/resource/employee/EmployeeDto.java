package com.zu_min.playground.quarkus.resource.employee;

import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
 * 社員情報。
 */
@Data
public class EmployeeDto {
    @Schema(readOnly = true)
    private Long id;
}
