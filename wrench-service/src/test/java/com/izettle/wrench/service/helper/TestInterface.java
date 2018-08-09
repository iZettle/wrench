package com.izettle.wrench.service.helper;

import com.izettle.wrench.service.DefaultValue;
import com.izettle.wrench.service.Key;

public interface TestInterface {

    enum Test {
        VALUE
    }


    /* J8 default method */

    default Object objectReturnTypeDefaultMethod(final Object value){
        return value;
    }

    /* Invalid return type */

    @Key("voidReturnType")
    void voidReturnType();

    @Key("runnableReturnType")
    Runnable runnableReturnType();

    /* Constants default value */

    @Key("booleanReturnType")
    @DefaultValue.Boolean(true)
    boolean booleanReturnType();

    @Key("stringReturnType")
    @DefaultValue.String("stringReturnType")
    String stringReturnType();

    @Key("intReturnType")
    @DefaultValue.Int(1)
    int intReturnType();

    /* Dynamic default value  */

    @Key("booleanReturnTypeWithDynamicValue")
    boolean booleanReturnTypeWithDynamicValue(@DefaultValue boolean value);

    @Key("stringReturnTypeWithDynamicValue")
    String stringReturnTypeWithDynamicValue(@DefaultValue String value);

    @Key("intReturnTypeWithDynamicValue")
    int intReturnTypeWithDynamicValue(@DefaultValue int value);

    @Key("enumReturnTypeWithDynamicValue")
    Test enumReturnTypeWithDynamicValue(@DefaultValue Test value);

    /* Constants default value with missing key */

    @DefaultValue.Boolean(true)
    boolean booleanReturnTypeMissingKey();

    @DefaultValue.String("stringReturnTypeMissingKey")
    String stringReturnTypeMissingKey();

    @DefaultValue.Int(1)
    int intReturnTypeMissingKey();

    /* Dynamic default value with missing key */

    boolean booleanReturnTypeWithDynamicValueMissingKey(@DefaultValue boolean value);

    String stringReturnTypeWithDynamicValueMissingKey(@DefaultValue String value);

    int intReturnTypeWithDynamicValueMissingKey(@DefaultValue int value);

    Test enumReturnTypeWithDynamicValueMissingKey(@DefaultValue Test value);

    /* Missing default value */

    @Key("booleanReturnTypeMissingDefaultValue")
    boolean booleanReturnTypeMissingDefaultValue();

    @Key("stringReturnTypeMissingDefaultValue")
    String stringReturnTypeMissingDefaultValue();

    @Key("intReturnTypeMissingDefaultValue")
    int intReturnTypeMissingDefaultValue();

    /*  Missing default value with signature */

    @Key("booleanReturnTypeWithDynamicValueMissingDefaultValue")
    boolean booleanReturnTypeWithDynamicValueMissingDefaultValue(boolean value);

    @Key("stringReturnTypeWithDynamicValueMissingDefaultValue")
    String stringReturnTypeWithDynamicValueMissingDefaultValue(String value);

    @Key("intReturnTypeWithDynamicValueMissingDefaultValue")
    int intReturnTypeWithDynamicValueMissingDefaultValue(int value);

    @Key("enumReturnTypeWithDynamicValueMissingDefaultValue")
    Test enumReturnTypeWithDynamicValueMissingDefaultValue(Test value);
}
