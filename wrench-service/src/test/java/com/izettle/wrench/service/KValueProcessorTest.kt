package com.izettle.wrench.service

import com.izettle.wrench.service.helper.Invokator
import com.izettle.wrench.service.helper.Invokator.Invokation
import com.izettle.wrench.service.helper.KTestInterface

import org.junit.Before
import org.junit.Test

import java.util.Arrays
import java.util.HashMap

import org.junit.Assert.assertEquals
import org.junit.Assert.fail

class KValueProcessorTest {

    private lateinit var processor: ValueProcessor

    @Before
    @Throws(Exception::class)
    fun setUp() {
        processor = ValueProcessor()
    }

    @Test(expected = NullPointerException::class)
    fun getValue_NullMethod_ShouldThrows() {
        processor.getValue(null, arrayOf())
    }

    @Test
    fun getValue_MissingAnnotation_ShouldThrow() {
        val invokations = Arrays.asList(
                Invokator.invoke(KTestInterface::class.java) { i -> i.intReturnTypeMissingDefaultValue },
                Invokator.invoke(KTestInterface::class.java) { i -> i.stringReturnTypeMissingDefaultValue },
                Invokator.invoke(KTestInterface::class.java) { i -> i.booleanReturnTypeMissingDefaultValue }
        )

        for (invokation in invokations) {
            try {
                processor.getValue(invokation.method, invokation.args)
                fail(invokation.method.name)
            } catch (e: IllegalArgumentException) {
            }

        }
    }

    @Test
    fun getValue_PresentAnnotationOnMethod_ShouldReturnValue() {
        val invokations = HashMap<Invokation, Any>()
        invokations[Invokator.invoke(KTestInterface::class.java) { i -> i.intReturnType }] = 1
        invokations[Invokator.invoke(KTestInterface::class.java) { i -> i.stringReturnType }] = "getStringReturnType"
        invokations[Invokator.invoke(KTestInterface::class.java) { i -> i.booleanReturnType }] = true

        for ((invokation, expected) in invokations) {
            val `object` = processor.getValue(invokation.method, invokation.args)
            assertEquals(expected, `object`)
        }
    }

    @Test
    fun getValue_PresentAnnotationOnMethod_NullArgs_ShouldReturnValue() {
        val invokations = HashMap<Invokation, Any>()
        invokations[Invokator.invoke(KTestInterface::class.java) { i -> i.intReturnType }] = 1
        invokations[Invokator.invoke(KTestInterface::class.java) { i -> i.stringReturnType }] = "getStringReturnType"
        invokations[Invokator.invoke(KTestInterface::class.java) { i -> i.booleanReturnType }] = true

        for ((invokation, expected) in invokations) {
            val `object` = processor.getValue(invokation.method, null)
            assertEquals(expected, `object`)
        }
    }
}