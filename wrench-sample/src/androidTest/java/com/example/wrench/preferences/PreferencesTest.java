package com.example.wrench.preferences;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.mock.MockContentProvider;
import android.test.mock.MockContentResolver;
import android.test.mock.MockContext;

import com.izettle.wrench.core.WrenchProviderContract;
import com.izettle.wrench.preferences.WrenchPreferences;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class PreferencesTest {

    private ResolverMockContext mockContext;

    @Before
    public void setUp() {
        mockContext = new ResolverMockContext(InstrumentationRegistry.getContext());
    }

    @Test
    public void checkBoolean() {
        String key = "boolean";
        WrenchPreferences wrenchPreferences = new WrenchPreferences(mockContext);
        assertEquals(false, wrenchPreferences.getBoolean(key, false));
        assertEquals(false, wrenchPreferences.getBoolean(key, true));
    }

    @Test
    public void checkInteger() {
        String key = "integer";
        WrenchPreferences wrenchPreferences = new WrenchPreferences(mockContext);
        assertEquals(42, wrenchPreferences.getInt(key, 42));
        assertEquals(42, wrenchPreferences.getInt(key, 1));
    }

    @Test
    public void checkString() {
        String key = "string";
        WrenchPreferences wrenchPreferences = new WrenchPreferences(mockContext);
        assertEquals("correct", wrenchPreferences.getString(key, "correct"));
        assertEquals("correct", wrenchPreferences.getString(key, "invalid"));
    }

    @Test
    public void checkEnum() {
        String key = "enum";

        WrenchPreferences wrenchPreferences = new WrenchPreferences(mockContext);
        assertEquals(MyEnum.FIRST, wrenchPreferences.getEnum(key, MyEnum.class, MyEnum.FIRST));
        assertEquals(MyEnum.FIRST, wrenchPreferences.getEnum(key, MyEnum.class, MyEnum.SECOND));
    }

    enum MyEnum {
        FIRST, SECOND
    }

    private class ResolverMockContext extends MockContext {
        private MockContentResolver contentResolver;
        private MockContentProvider mockProvider;

        public ResolverMockContext(Context context) {
            mockProvider = new MockWrenchProvider(context);
            contentResolver = new MockContentResolver();
            contentResolver.addProvider(WrenchProviderContract.WRENCH_AUTHORITY, mockProvider);
        }

        @Override
        public ContentResolver getContentResolver() {
            return contentResolver;
        }
    }

    private class MockWrenchProvider extends MockContentProvider {
        public MockWrenchProvider(Context context) {
            super(context);
        }

        @Override
        public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
            return null;
        }
    }
}
