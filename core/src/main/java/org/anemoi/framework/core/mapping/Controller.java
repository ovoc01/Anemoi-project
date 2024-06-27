package org.anemoi.framework.core.mapping;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Controller {
    String baseUrl() default "/";
    String [] origins() default "" ;
    String [] allowedMethods() default "";
}
