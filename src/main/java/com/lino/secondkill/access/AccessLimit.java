package com.lino.secondkill.access;


import java.lang.annotation.Retention;
import java.lang.annotation.Target;


import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target(METHOD)
public @interface AccessLimit {
     int second();
     int maxCount();
     boolean needLogin() default true;

}
