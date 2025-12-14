package com.willu.buyitornot.web.advice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.willu.buyitornot.web.ui.common.ApiResponse;
import com.willu.buyitornot.web.ui.common.WrapResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice
public class ResponseWrapperAdvice implements ResponseBodyAdvice<Object> {

    private final ObjectMapper objectMapper;

    public ResponseWrapperAdvice(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // Apply to methods/classes with @WrapResponse annotation
        return returnType.hasMethodAnnotation(WrapResponse.class) ||
               returnType.getDeclaringClass().isAnnotationPresent(WrapResponse.class);
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                   MethodParameter returnType,
                                   MediaType selectedContentType,
                                   Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                   ServerHttpRequest request,
                                   ServerHttpResponse response) {

        // If the body is already an ApiResponse, return it as-is
        if (body instanceof ApiResponse) {
            return body;
        }

        // If the body is a ResponseEntity, don't wrap it
        if (body instanceof ResponseEntity) {
            return body;
        }

        // Wrap the body in ApiResponse
        ApiResponse<?> wrappedResponse = ApiResponse.success(body);

        // CRITICAL: If StringHttpMessageConverter is being used, we must return a String
        // not an ApiResponse object, otherwise we get ClassCastException
        if (StringHttpMessageConverter.class.isAssignableFrom(selectedConverterType)) {
            try {
                // Force content type to JSON
                response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
                // Convert ApiResponse to JSON string
                return objectMapper.writeValueAsString(wrappedResponse);
            } catch (Exception e) {
                throw new RuntimeException("Failed to serialize ApiResponse to JSON", e);
            }
        }

        // For other converters (like Jackson), return the ApiResponse object
        return wrappedResponse;
    }
}
