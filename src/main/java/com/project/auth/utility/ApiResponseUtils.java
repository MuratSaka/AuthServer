package com.project.auth.utility;


import com.project.auth.dto.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
/**
 * @author Murat Saka
 * @created 19/10/2025 - 12:25
 * @project AuthServer
 */
public final class ApiResponseUtils {

    // [OPTIMIZE] Thread-safe, tek bir formatter kullan (ISO-8601 + offset)
    private static final DateTimeFormatter ISO_OFFSET = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    // [OPTIMIZE] Test edilebilirlik için Clock enjekte edilebilir; default UTC
    private static Clock clock = Clock.systemUTC();

    // [OPTIMIZE] Örneklenmeyi engelle
    private ApiResponseUtils() { }

    // [OPTIMIZE] Testlerde farklı saat kullanmak istenirse
    public static void setClock(Clock newClock) {
        clock = Objects.requireNonNull(newClock, "clock");
    }

    // [OPTIMIZE] Ortak timestamp üretimi
    private static String nowIsoOffsetUtc() {
        return OffsetDateTime.now(clock).withOffsetSameInstant(ZoneOffset.UTC).format(ISO_OFFSET);
    }

    /**
     * Verilerle bir başarı yanıtı oluşturun.
     *
     * @param message Yanıt mesajı
     * @param data Yanıt verileri
     * @return Yanıt bilgilerini içeren ApiResponse
     */
    public static <T> ApiResponse<T> successResponse(String message, T data) {
        return ApiResponse.<T>builder()
                .timestamp(nowIsoOffsetUtc()) // [OPTIMIZE] ISO-8601 + UTC offset
                .status(HttpStatus.OK.value())
                .message(message)
                .data(data)
                .build();
    }

    // [OPTIMIZE] Ergonomi: sadece data ile başarılı yanıt
    public static <T> ApiResponse<T> successResponse(T data) {
        return successResponse("OK", data);
    }

    /**
     * Hata listesiyle bir hata yanıtı oluşturun.
     *
     * @param httpStatus HTTP durum kodu
     * @param message Yanıt mesajı
     * @param errors Ayrıntılı hata listesi
     * @return ApiResponse hata bilgilerini içerir
     */
    public static <T> ApiResponse<T> errorResponse(
            HttpStatus httpStatus,
            String message,
            List<FieldErrorDetails> errors) {

        // [FIX] httpStatus null olamaz; NPE’den kaçın
        int status = Objects.requireNonNull(httpStatus, "httpStatus must not be null").value();

        // [OPTIMIZE] Listeyi dışarıdan immutability garantisiyle al
        List<FieldErrorDetails> safeErrors = errors == null ? List.of() : List.copyOf(errors);

        return ApiResponse.<T>builder()
                .timestamp(nowIsoOffsetUtc()) // [OPTIMIZE]
                .status(status)
                .message(message)
                .errors(safeErrors)
                .build();
    }

    /**
     * Tek bir hata içeren bir hata yanıtı oluşturur.
     *
     * @param httpStatus HTTP durum kodu
     * @param message Yanıt mesajı
     * @param field Özellik hatası
     * @param rejectedValue Reddedilen değer
     * @param errorMessage Hata mesajı
     * @return Hata bilgilerini içeren ApiResponse
     */
    public static <T> ApiResponse<T> errorResponse(
            HttpStatus httpStatus,
            String message,
            String field,
            Object rejectedValue,
            String errorMessage) {

        // [NOTE] FieldErrorDetails ikinci parametreyi Object bekliyorsa toString() yapmayın.
        return errorResponse(
                httpStatus,
                message,
                List.of(new FieldErrorDetails(field, rejectedValue, errorMessage)) // [FIX] tipi koru
        );
    }

    // [OPTIMIZE] Ergonomi: sadece status + message ile hata
    public static <T> ApiResponse<T> errorResponse(HttpStatus httpStatus, String message) {
        return errorResponse(httpStatus, message, List.of());
    }

    /**
     * Validasyon hatalarından hata yanıtı üretir.
     */
    public static <T> ApiResponse<T> generateValidationErrorResponse(
            BindingResult bindingResult,
            String errorMessage,
            LocalizationUtils localizationUtils) {

        // [FIX] Null koruması
        Objects.requireNonNull(bindingResult, "bindingResult must not be null");

        // [FIX] localizationUtils null olabilir → güvenli çözüm
        final boolean hasLocalization = localizationUtils != null;

        List<FieldErrorDetails> fieldErrors = bindingResult.getFieldErrors()
                .stream()
                .map((FieldError error) -> {
                    String field = error.getField();

                    // [FIX] rejectedValue null olabilir; tipi korumak için doğrudan geç
                    Object rejected = error.getRejectedValue(); // Object olarak bırak

                    // [FIX] defaultMessage null olabilir
                    String defaultMsg = error.getDefaultMessage();
                    String localized =
                            hasLocalization
                                    ? // [OPTIMIZE] boş/ null bir mesajı güvenli çevir
                                    localizationUtils.getLocalizedMessage(
                                            defaultMsg != null ? defaultMsg : "validation.error")
                                    : (defaultMsg != null ? defaultMsg : "Validation error");

                    return new FieldErrorDetails(field, rejected, localized);
                })
                .collect(Collectors.toUnmodifiableList()); // [OPTIMIZE] immutable liste

        return ApiResponse.<T>builder()
                .timestamp(nowIsoOffsetUtc()) // [OPTIMIZE]
                .status(HttpStatus.BAD_REQUEST.value())
                .message(errorMessage)
                .errors(fieldErrors)
                .data(null) // [NOTE] Hata durumunda veri boş
                .build();
    }
}
