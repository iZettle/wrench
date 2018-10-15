package com.izettle.wrench.service.helper;

import com.izettle.wrench.service.DefaultValue;
import com.izettle.wrench.service.Key;

public interface TestInterface {

    default Object objectReturnTypeDefaultMethod(final Object value) {
        return value;
    }


    /* J8 default method */

    @Key("voidReturnType")
    void voidReturnType();

    /* Invalid return type */

    @Key("runnableReturnType")
    Runnable runnableReturnType();

    @Key("booleanReturnType")
    @DefaultValue.Boolean(true)
    boolean booleanReturnType();

    /* Constants default value */

    @Key("stringReturnType")
    @DefaultValue.String("stringReturnType")
    String stringReturnType();

    @Key("intReturnType")
    @DefaultValue.Int(1)
    int intReturnType();

    @Key("booleanReturnTypeWithDynamicValue")
    boolean booleanReturnTypeWithDynamicValue(@DefaultValue boolean value);

    /* Dynamic default value  */

    @Key("stringReturnTypeWithDynamicValue")
    String stringReturnTypeWithDynamicValue(@DefaultValue String value);

    @Key("intReturnTypeWithDynamicValue")
    int intReturnTypeWithDynamicValue(@DefaultValue int value);

    @Key("enumReturnTypeWithDynamicValue")
    Test enumReturnTypeWithDynamicValue(@DefaultValue Test value);

    @DefaultValue.Boolean(true)
    boolean booleanReturnTypeMissingKey();

    /* Constants default value with missing key */

    @DefaultValue.String("stringReturnTypeMissingKey")
    String stringReturnTypeMissingKey();

    @DefaultValue.Int(1)
    int intReturnTypeMissingKey();

    boolean booleanReturnTypeWithDynamicValueMissingKey(@DefaultValue boolean value);

    /* Dynamic default value with missing key */

    String stringReturnTypeWithDynamicValueMissingKey(@DefaultValue String value);

    int intReturnTypeWithDynamicValueMissingKey(@DefaultValue int value);

    Test enumReturnTypeWithDynamicValueMissingKey(@DefaultValue Test value);

    @Key("booleanReturnTypeMissingDefaultValue")
    boolean booleanReturnTypeMissingDefaultValue();

    /* Missing default value */

    @Key("stringReturnTypeMissingDefaultValue")
    String stringReturnTypeMissingDefaultValue();

    @Key("intReturnTypeMissingDefaultValue")
    int intReturnTypeMissingDefaultValue();

    @Key("booleanReturnTypeWithDynamicValueMissingDefaultValue")
    boolean booleanReturnTypeWithDynamicValueMissingDefaultValue(boolean value);

    /*  Missing default value with signature */

    @Key("stringReturnTypeWithDynamicValueMissingDefaultValue")
    String stringReturnTypeWithDynamicValueMissingDefaultValue(String value);

    @Key("intReturnTypeWithDynamicValueMissingDefaultValue")
    int intReturnTypeWithDynamicValueMissingDefaultValue(int value);

    @Key("enumReturnTypeWithDynamicValueMissingDefaultValue")
    Test enumReturnTypeWithDynamicValueMissingDefaultValue(Test value);

    enum Test {
        VALUE
    }
}
