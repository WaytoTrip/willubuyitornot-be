package com.willu.buyitornot.web.ui.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to indicate that a controller method's return value
 * should be automatically wrapped in an ApiResponse object.
 *
 * Can be applied to individual methods or entire controller classes.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface WrapResponse {
}
