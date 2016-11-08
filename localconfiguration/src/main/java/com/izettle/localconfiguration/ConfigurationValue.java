package com.izettle.localconfiguration;

public class ConfigurationValue {
    public long _id;
    public long configurationId;
    public String value;

    @Override
    public String toString() {
        return value;
    }
}
