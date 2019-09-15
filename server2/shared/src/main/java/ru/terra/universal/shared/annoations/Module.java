package ru.terra.universal.shared.annoations;

import java.lang.annotation.*;

/**
 * Date: 25.04.14
 * Time: 14:00
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Module {
    String value();
}