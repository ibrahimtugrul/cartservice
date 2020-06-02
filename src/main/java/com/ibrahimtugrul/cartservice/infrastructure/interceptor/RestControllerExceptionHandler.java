package com.ibrahimtugrul.cartservice.infrastructure.interceptor;

import com.google.common.collect.ImmutableMap;
import com.ibrahimtugrul.cartservice.domain.exception.BusinessException;
import com.ibrahimtugrul.cartservice.infrastructure.locale.MessageSourceLocalizer;
import com.ibrahimtugrul.cartservice.infrastructure.model.ExceptionType;
import com.ibrahimtugrul.cartservice.infrastructure.model.RestErrorMessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;

@RestControllerAdvice
@RequiredArgsConstructor
public class RestControllerExceptionHandler extends ResponseEntityExceptionHandler {

    private final MessageSourceLocalizer messageSourceLocalizer;

    private static final String ERROR_MESSAGE_SPLITTER = ";";
    private static final String REQUEST_VALIDATION_EXCEPTION_USER_MESSAGE = "Unexpected error";
    private static final String INTERNAL_SERVER_EXCEPTION_USER_MESSAGE = "An unknown exception has occurred";

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException exception, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        logger.warn("An invalid api call occurred: ", exception);
        final String messageKey = exception.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        final RestErrorMessageResponse error = createErrorResponse(REQUEST_VALIDATION_EXCEPTION_USER_MESSAGE, ExceptionType.PARAMETER_VALIDATION, messageKey, null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Object> handleBusinessException(final BusinessException exception) {
        logger.warn("A business exception has occurred: ", exception);
        final RestErrorMessageResponse error = createErrorResponse(REQUEST_VALIDATION_EXCEPTION_USER_MESSAGE, ExceptionType.BUSINESS, exception.getMessage(), exception.getArguments());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
/*
    @ExceptionHandler(RequestValidationException.class)
    public ResponseEntity<Object> handleRequestValidationException(RequestValidationException exception) {
        logger.warn("A request validation exception has occurred: ", exception);
        final RestErrorMessageResponse error = createErrorResponse(REQUEST_VALIDATION_EXCEPTION_USER_MESSAGE, ExceptionType.PARAMETER_VALIDATION, exception.getMessage(), exception.getArguments());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<Object> handleInvalidRequestException(final InvalidRequestException exception) {
        logger.warn("An invalid request exception has occurred: ", exception);
        final RestErrorMessageResponse error = createErrorResponse(REQUEST_VALIDATION_EXCEPTION_USER_MESSAGE, ExceptionType.PARAMETER_VALIDATION, exception.getMessage(), null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
*/
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleRuntimeException(Exception exception) {
        logger.error("A runtime exception has occurred: ", exception);
        final RestErrorMessageResponse error = createErrorResponse(INTERNAL_SERVER_EXCEPTION_USER_MESSAGE, ExceptionType.SERVER_ERROR, "internal.server.error", null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    private RestErrorMessageResponse createErrorResponse(String userMessage, ExceptionType exceptionType, String messageKey, Object... args) {
        final String message = messageSourceLocalizer.getLocaleMessage(messageKey, args);
        final Map<String, String> messageKeyMap = retrieveErrorCodeAndMessagePair(message);

        final int errorCode = Integer.parseInt(messageKeyMap.get("errorCode"));
        final String errorMessage = messageKeyMap.get("errorMessage");

        return RestErrorMessageResponse.builder()
                .code(errorCode)
                .message(errorMessage)
                .userMessage(userMessage)
                .exceptionType(exceptionType)
                .build();
    }

    private Map<String, String> retrieveErrorCodeAndMessagePair(final String message) {
        final String[] errorCodeMessage = message.split(ERROR_MESSAGE_SPLITTER);
        return ImmutableMap.of("errorCode", errorCodeMessage[0], "errorMessage", errorCodeMessage[1]);
    }
}