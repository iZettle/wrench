package com.izettle.wrench.service

import com.izettle.wrench.service.helper.Invokator
import com.izettle.wrench.service.helper.KTestInterface

import org.junit.Assert
import org.junit.Before
import org.junit.Test

import java.util.Arrays


class KKeyProcessorTest {

    private lateinit var processor: KeyProcessor

    @Before
    @Throws(Exception::class)
    fun setUp() {
        processor = KeyProcessor()
    }

    @Test(expected = NullPointerException::class)
    fun getValue_NullMethod_ShouldThrows() {
        processor.getValue(null, arrayOf())
    }

    @Test
    fun getValue_MissingAnnotation_ShouldThrow() {
        val invokations = Arrays.asList(
                Invokator.invoke(KTestInterface::class.java) { i -> i.intReturnTypeMissingKey },
                Invokator.invoke(KTestInterface::class.java) { i -> i.stringReturnTypeMissingKey },
                Invokator.invoke(KTestInterface::class.java) { i -> i.booleanReturnTypeMissingKey }
        )

        for (invokation in invokations) {
            try {
                processor.getValue(invokation.method, invokation.args)
                Assert.fail(invokation.method.name)
            } catch (e: IllegalArgumentException) {
            }

        }
    }

    @Test
    fun getValue_PresentAnnotation_ShouldReturnValue() {
        val invokations = Arrays.asList(
                Invokator.invoke(KTestInterface::class.java) { i -> i.intReturnType },
                Invokator.invoke(KTestInterface::class.java) { i -> i.stringReturnType },
                Invokator.invoke(KTestInterface::class.java) { i -> i.booleanReturnType }
        )

        for (invokation in invokations) {
            val key = processor.getValue(invokation.method, invokation.args)
            Assert.assertEquals(key, invokation.method.name)
        }
    }

    @Test
    fun getValue_PresentAnnotation_NullArgs_ShouldReturnValue() {
        val invokations = Arrays.asList(
                Invokator.invoke(KTestInterface::class.java) { i -> i.intReturnType },
                Invokator.invoke(KTestInterface::class.java) { i -> i.stringReturnType },
                Invokator.invoke(KTestInterface::class.java) { i -> i.booleanReturnType }
        )

        for (invokation in invokations) {
            val key = processor.getValue(invokation.method, null)
            Assert.assertEquals(key, invokation.method.name)
        }
    }
}