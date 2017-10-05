package com.example.wrench.preferences;

import android.content.ContentResolver;
import android.database.Cursor;
import android.support.test.runner.AndroidJUnit4;
import android.test.mock.MockContentResolver;
import android.test.mock.MockContext;

import com.izettle.wrench.core.WrenchProviderContract;
import com.izettle.wrench.preferences.WrenchPreferences;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

@RunWith(AndroidJUnit4.class)
public class MissingProviderTest {

    private ResolverMockContext mockContext;

    @Before
    public void setUp() {
        mockContext = new ResolverMockContext();

        Cursor query = mockContext.getContentResolver().query(WrenchProviderContract.boltUri(""), null, null, null, null);
        assertNull(query);
    }

    @Test
    public void checkBoolean() {
        String key = "boolean";

        WrenchPreferences wrenchPreferences = new WrenchPreferences(mockContext);
        assertEquals(false, wrenchPreferences.getBoolean(key, false));
        assertEquals(false, wrenchPreferences.getBoolean(key, false));
    }

    @Test
    public void checkInteger() {
        String key = "integer";
        WrenchPreferences wrenchPreferences = new WrenchPreferences(mockContext);
        assertEquals(42, wrenchPreferences.getInt(key, 42));
        assertEquals(1, wrenchPreferences.getInt(key, 1));
    }

    @Test
    public void checkString() {
        String key = "string";
        WrenchPreferences wrenchPreferences = new WrenchPreferences(mockContext);
        assertEquals("correct", wrenchPreferences.getString(key, "correct"));
        assertEquals("invalid", wrenchPreferences.getString(key, "invalid"));
    }

    @Test
    public void checkEnum() {
        String key = "enum";

        WrenchPreferences wrenchPreferences = new WrenchPreferences(mockContext);
        assertEquals(MyEnum.FIRST, wrenchPreferences.getEnum(key, MyEnum.class, MyEnum.FIRST));
        assertEquals(MyEnum.SECOND, wrenchPreferences.getEnum(key, MyEnum.class, MyEnum.SECOND));
    }

    enum MyEnum {
        FIRST, SECOND
    }

    private class ResolverMockContext extends MockContext {
        private MockContentResolver contentResolver;

        public ResolverMockContext() {
            contentResolver = new MockContentResolver();
        }

        @Override
        public ContentResolver getContentResolver() {
            return contentResolver;
        }
    }
}
