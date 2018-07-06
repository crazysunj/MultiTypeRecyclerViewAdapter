package com.crazysunj.multitypeadapter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface AdapterHelper {

    String superObj() default "com.crazysunj.multitypeadapter.helper.AsynAdapterHelper";

    String entity() default "com.crazysunj.multitypeadapter.entity.MultiTypeEntity";
}
