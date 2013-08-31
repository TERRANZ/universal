package ru.terra.universal.shared.annoations;

import java.lang.annotation.*;

/**
 * Created with IntelliJ IDEA.
 * User: terranz
 * Date: 31.08.13
 * Time: 11:51
 * To change this template use File | Settings | File Templates.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Packet {
    int opCode() default 0;
}

