package ru.terra.universal.shared.annoations;

import java.lang.annotation.*;

/**
 * User: terranz
 * Date: 31.08.13
 * Time: 11:51
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Packet {
    int opCode() default 0;
}

