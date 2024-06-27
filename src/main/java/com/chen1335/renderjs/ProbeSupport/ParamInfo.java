package com.chen1335.renderjs.ProbeSupport;

import java.lang.annotation.*;
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface ParamInfo {
    String argName();
}
