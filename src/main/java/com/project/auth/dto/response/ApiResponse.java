package com.project.auth.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.auth.utility.FieldErrorDetails;
import lombok.*; // @Data, @Builder, vs.
import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
/**
 * @author Murat Saka
 * @created 19/10/2025 - 12:26
 * @project AuthServer
 */
@JsonInclude(JsonInclude.Include.NON_NULL) // [OPTIMIZE] null alanları JSON’da gizle
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {

    // [OPTIMIZE] ISO-8601 + offset (örn. 2025-10-19T09:12:34Z)
    private String timestamp;

    private int status;
    private String message;
    private T data;

    // [FIX] Boş listeler için güvenli varsayılan değer
    @Builder.Default
    private List<FieldErrorDetails> errors = List.of();

    // --- Factory helpers ---

    // [OPTIMIZE] Ortak zaman üretimi (tekrar eden kodu önler)
    private static String nowIsoOffsetUtc() {
        return OffsetDateTime.now(ZoneOffset.UTC)
                .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }

    public static <T> ApiResponse<T> ok(String message, T data) {
        return ApiResponse.<T>builder()
                .timestamp(nowIsoOffsetUtc()) // [OPTIMIZE]
                .status(HttpStatus.OK.value())
                .message(message)
                .data(data)
                // .errors(List.of()) // [NOTE] @Builder.Default zaten boş liste atıyor
                .build();
    }

    // [OPTIMIZE] Sadece data ile başarılı yanıt
    public static <T> ApiResponse<T> ok(T data) {
        return ok("OK", data);
    }

    // [OPTIMIZE] HttpStatus alan overload
    public static <T> ApiResponse<T> error(HttpStatus status, String message, List<FieldErrorDetails> errors) {
        return error(status.value(), message, errors);
    }

    public static <T> ApiResponse<T> error(int status, String message, List<FieldErrorDetails> errors) {
        return ApiResponse.<T>builder()
                .timestamp(nowIsoOffsetUtc()) // [OPTIMIZE]
                .status(status)
                .message(message)
                .data(null)
                .errors(errors != null ? errors : List.of()) // [FIX] null güvenliği
                .build();
    }

    // [OPTIMIZE] Tek hata için ergonomik helper
    public static <T> ApiResponse<T> error(HttpStatus status, String message, FieldErrorDetails error) {
        return error(status, message, List.of(error));
    }
}
