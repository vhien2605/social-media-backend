package com.hien.back_end_app.utils.anotations;


import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PreSocketAuthorize {
    String[] authorities() default {};
}
