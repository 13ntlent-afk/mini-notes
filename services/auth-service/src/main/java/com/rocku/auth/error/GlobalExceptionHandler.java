package com.rocku.auth.error;

import com.rocku.auth.domain.AuthException;
import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AuthException.class)
    ResponseEntity<ProblemDetail> authError(AuthException exception, HttpServletRequest request) {
        var problem = ProblemDetail.forStatusAndDetail(exception.getStatus(), exception.getMessage());
        problem.setTitle(exception.getStatus().getReasonPhrase());
        problem.setInstance(URI.create(request.getRequestURI()));
        return ResponseEntity.status(exception.getStatus())
                .contentType(MediaType.APPLICATION_PROBLEM_JSON).body(problem);
    }
}