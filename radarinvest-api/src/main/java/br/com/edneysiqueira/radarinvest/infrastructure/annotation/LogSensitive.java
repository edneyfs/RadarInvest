package br.com.edneysiqueira.radarinvest.infrastructure.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotação para marcar campos sensíveis em DTOs que devem ser mascarados nos
 * logs.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogSensitive {
    SensitiveType strategy() default SensitiveType.HIDE;
}
