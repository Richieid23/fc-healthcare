package id.web.fitrarizki.healthcare.common.exeption;

import id.web.fitrarizki.healthcare.dto.ErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.security.SignatureException;
import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class GeneralExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody ErrorResponse handleBadRequestException(BadRequestException e) {
        return ErrorResponse.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(InvalidPasswordException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public @ResponseBody ErrorResponse handleUnauthorizeException(InvalidPasswordException e) {
        return ErrorResponse.builder()
                .code(HttpStatus.UNAUTHORIZED.value())
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler({
            AccessDeniedException.class,
            SignatureException.class,
            ExpiredJwtException.class,
            AuthenticationException.class,
            InsufficientAuthenticationException.class
    })
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public @ResponseBody ErrorResponse handleForbiddenException(HttpServletResponse response, Exception e) {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        return ErrorResponse.builder()
                .code(HttpStatus.FORBIDDEN.value())
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler({
            ResourceNotFoundException.class,
            UserNotFoundException.class,
            RoleNotFoundException.class,
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public @ResponseBody ErrorResponse handleResourceNotFoundException(RuntimeException e) {
        return ErrorResponse.builder()
                .code(HttpStatus.NOT_FOUND.value())
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler({
            UsernameAlreadyExistsException.class,
            EmailAlreadyExistsException.class,
    })
    @ResponseStatus(HttpStatus.CONFLICT)
    public @ResponseBody ErrorResponse handleResourceConflictException(RuntimeException e) {
        return ErrorResponse.builder()
                .code(HttpStatus.CONFLICT.value())
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public @ResponseBody ErrorResponse handleGenericException(HttpServletRequest request, Exception e) {
        log.error("Telah terjadi kesalahan pada endpoint {}, status code :{}, error_message: {}",
                request.getRequestURI(), HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        return ErrorResponse.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }
}
