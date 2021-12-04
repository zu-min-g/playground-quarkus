package com.zu_min.playground.quarkus.dto;

import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

/**
 * 果物情報。
 */
@Data
public class FruitDto {
    @Schema(readOnly = true)
    private Long id;

    private String name;

}
