package com.project.auth.utility;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * @author Murat Saka
 * @created 19/10/2025 - 20:40
 * @project AuthServer
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FieldErrorDetails {

    private String field;
    private Object rejectedValue;
    private String message;
}
