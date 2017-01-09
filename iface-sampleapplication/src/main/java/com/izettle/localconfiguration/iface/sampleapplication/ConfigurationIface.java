package com.izettle.localconfiguration.iface.sampleapplication;

import com.example.localconfiguration.iface.ConfigString;
import com.example.localconfiguration.iface.DefaultValue;

public interface ConfigurationIface {
    @ConfigString("WELCOME_TITLE")
    @DefaultValue("default value")
    String welcomeTitle();
}
