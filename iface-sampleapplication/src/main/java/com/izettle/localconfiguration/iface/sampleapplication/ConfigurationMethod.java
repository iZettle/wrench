package com.izettle.localconfiguration.iface.sampleapplication;

import com.example.localconfiguration.iface.ConfigString;
import com.example.localconfiguration.iface.DefaultValue;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

class ConfigurationMethod {
    public String configurationKey;
    public String configurationType;
    public String defaultValue;

    public ConfigurationMethod(Method method) {
        for (Annotation annotation : method.getAnnotations()) {
            parseAnnotation(annotation);
        }
    }

    private void parseAnnotation(Annotation annotation) {
        if (annotation instanceof ConfigString) {
            if (this.configurationType != null) {
                throw new IllegalStateException("configurationType already defined");
            }
            this.configurationType = "String";
            this.configurationKey = ((ConfigString) annotation).value();
        } else if (annotation instanceof DefaultValue) {
            if (this.defaultValue != null) {
                throw new IllegalStateException("configurationType already defined");
            }
            this.defaultValue = ((DefaultValue) annotation).value();
        }
    }
}
