package com.example.wrench;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.izettle.wrench.core.Bolt;
import com.izettle.wrench.core.WrenchProviderContract;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.content.ContentValues.TAG;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class LaunchTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    private ContentResolver contentResolver;
    private Resources resources;

    private static void updateInteger(ContentResolver contentResolver, String key, int value) {
        Bolt bolt = getBolt(contentResolver, key);
        if (bolt == null) {
            Log.w(TAG, "provider not found");
            return;
        }

        if (bolt.getId() == 0) {
            bolt = bolt.copy(bolt.getId(), Bolt.TYPE.INTEGER, key, String.valueOf(value));
            Uri uri = insertBolt(contentResolver, bolt);
            bolt.setId(Long.parseLong(uri.getLastPathSegment()));
        } else {
            bolt = bolt.copy(bolt.getId(), bolt.getType(), bolt.getKey(), String.valueOf(value));
            updateBolt(contentResolver, bolt);
        }
    }

    private static void updateBoolean(ContentResolver contentResolver, String key, boolean value) {
        Bolt bolt = getBolt(contentResolver, key);
        if (bolt == null) {
            Log.w(TAG, "provider not found");
            return;
        }

        if (bolt.getId() == 0) {
            bolt = bolt.copy(bolt.getId(), Bolt.TYPE.BOOLEAN, key, String.valueOf(value));
            Uri uri = insertBolt(contentResolver, bolt);
            bolt.setId(Long.parseLong(uri.getLastPathSegment()));
        } else {
            bolt = bolt.copy(bolt.getId(), bolt.getType(), bolt.getKey(), String.valueOf(value));
            updateBolt(contentResolver, bolt);
        }
    }

    private static void updateString(ContentResolver contentResolver, String key, String value) {
        Bolt bolt = getBolt(contentResolver, key);
        if (bolt == null) {
            Log.w(TAG, "provider not found");
            return;
        }

        if (bolt.getId() == 0) {
            bolt = bolt.copy(bolt.getId(), Bolt.TYPE.STRING, key, value);
            Uri uri = insertBolt(contentResolver, bolt);
            bolt.setId(Long.parseLong(uri.getLastPathSegment()));
        } else {
            bolt = bolt.copy(bolt.getId(), bolt.getType(), bolt.getKey(), String.valueOf(value));
            updateBolt(contentResolver, bolt);
        }
    }

    @Nullable
    private static Bolt getBolt(ContentResolver contentResolver, String key) {
        Cursor cursor = null;
        try {
            cursor = contentResolver.query(WrenchProviderContract.boltUri(key),
                    null,
                    null,
                    null,
                    null);
            if (cursor == null) {
                return null;
            }

            if (cursor.moveToFirst()) {
                return Bolt.fromCursor(cursor);
            }


        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return new Bolt();
    }

    private static Uri insertBolt(ContentResolver contentResolver, Bolt bolt) {
        return contentResolver.insert(WrenchProviderContract.boltUri(), bolt.toContentValues());
    }

    private static void updateBolt(ContentResolver contentResolver, Bolt bolt) {
        contentResolver.update(WrenchProviderContract.boltUri(bolt.getId()),
                bolt.toContentValues(),
                null,
                null);
    }

    @Before
    public void init() {
        Context context = InstrumentationRegistry.getTargetContext();
        contentResolver = context.getContentResolver();
        resources = context.getResources();
    }

    @Test
    public void checkStringValue() {
        updateString(contentResolver, resources.getString(R.string.string_configuration), "string1");
        onView(withId(R.id.string_configuration)).check(matches(withText("string1")));
        updateString(contentResolver, resources.getString(R.string.string_configuration), "string2");
        onView(withId(R.id.string_configuration)).check(matches(withText("string2")));
        updateString(contentResolver, resources.getString(R.string.string_configuration), "string3");
        onView(withId(R.id.string_configuration)).check(matches(withText("string3")));
    }

    @Test
    public void checkIntValue() {
        updateInteger(contentResolver, resources.getString(R.string.int_configuration), 1);
        onView(withId(R.id.int_configuration)).check(matches(withText("1")));
        updateInteger(contentResolver, resources.getString(R.string.int_configuration), 2);
        onView(withId(R.id.int_configuration)).check(matches(withText("2")));
        updateInteger(contentResolver, resources.getString(R.string.int_configuration), 1);
        onView(withId(R.id.int_configuration)).check(matches(withText("1")));
    }

    @Test
    public void checkBooleanValue() {
        updateBoolean(contentResolver, resources.getString(R.string.boolean_configuration), true);
        onView(withId(R.id.boolean_configuration)).check(matches(withText("true")));
        updateBoolean(contentResolver, resources.getString(R.string.boolean_configuration), false);
        onView(withId(R.id.boolean_configuration)).check(matches(withText("false")));
        updateBoolean(contentResolver, resources.getString(R.string.boolean_configuration), true);
        onView(withId(R.id.boolean_configuration)).check(matches(withText("true")));
    }
}
