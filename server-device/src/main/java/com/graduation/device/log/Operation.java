package com.graduation.device.log;

import javax.validation.constraints.NotNull;
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface Operation {
    @NotNull
    String type();
}