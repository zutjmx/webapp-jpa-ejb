package org.zutjmx.apiservlet.webapp.headers.configs;

import jakarta.enterprise.inject.Stereotype;
import jakarta.inject.Named;
import org.zutjmx.apiservlet.webapp.headers.interceptors.Logging;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Logging
@Stereotype
@Named
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Service {
}
