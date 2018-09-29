package com.example.wrench

import android.content.ContentResolver
import android.content.ContentValues.TAG
import android.content.res.Resources
import android.database.Cursor
import android.net.Uri
import android.util.Log
import androidx.test.InstrumentationRegistry
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.izettle.wrench.core.Bolt
import com.izettle.wrench.core.WrenchProviderContract
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class LaunchTest {

    @Rule
    var mActivityRule = ActivityTestRule<MainActivity>(MainActivity::class.java)

    private var contentResolver: ContentResolver? = null
    private var resources: Resources? = null

    private fun updateInteger(contentResolver: ContentResolver?, key: String, value: Int) {
        var bolt = getBolt(contentResolver!!, Bolt.TYPE.INTEGER, key)
        if (bolt == null) {
            Log.w(TAG, "provider not found")
            return
        }

        if (bolt.id == 0L) {
            bolt = bolt.copy(bolt.id, Bolt.TYPE.INTEGER, key, value.toString())
            val uri = insertBolt(contentResolver, bolt)
            bolt.setId(java.lang.Long.parseLong(uri!!.lastPathSegment!!))
        } else {
            bolt = bolt.copy(bolt.id, bolt.type, bolt.key, value.toString())
            updateBolt(contentResolver, bolt)
        }
    }

    private fun updateBoolean(contentResolver: ContentResolver?, key: String, value: Boolean) {
        var bolt = getBolt(contentResolver!!, Bolt.TYPE.BOOLEAN, key)
        if (bolt == null) {
            Log.w(TAG, "provider not found")
            return
        }

        if (bolt.id == 0L) {
            bolt = bolt.copy(bolt.id, Bolt.TYPE.BOOLEAN, key, value.toString())
            val uri = insertBolt(contentResolver, bolt)
            bolt.setId(java.lang.Long.parseLong(uri!!.lastPathSegment!!))
        } else {
            bolt = bolt.copy(bolt.id, bolt.type, bolt.key, value.toString())
            updateBolt(contentResolver, bolt)
        }
    }

    private fun updateString(contentResolver: ContentResolver?, key: String, value: String) {
        var bolt = getBolt(contentResolver!!, Bolt.TYPE.STRING, key)
        if (bolt == null) {
            Log.w(TAG, "provider not found")
            return
        }

        if (bolt.id == 0L) {
            bolt = bolt.copy(bolt.id, Bolt.TYPE.STRING, key, value)
            val uri = insertBolt(contentResolver, bolt)
            bolt.setId(java.lang.Long.parseLong(uri!!.lastPathSegment!!))
        } else {
            bolt = bolt.copy(bolt.id, bolt.type, bolt.key, value)
            updateBolt(contentResolver, bolt)
        }
    }

    private fun getBolt(contentResolver: ContentResolver, @Bolt.BoltType type: String, key: String): Bolt? {
        var cursor: Cursor? = null
        try {
            cursor = contentResolver.query(WrenchProviderContract.boltUri(key), null, null, null, null)
            if (cursor == null) {
                return null
            }

            if (cursor.moveToFirst()) {
                return Bolt.fromCursor(cursor)
            }


        } finally {
            if (cursor != null && !cursor.isClosed) {
                cursor.close()
            }
        }
        return Bolt(0L, type, key, "")
    }

    private fun insertBolt(contentResolver: ContentResolver, bolt: Bolt): Uri? {
        return contentResolver.insert(WrenchProviderContract.boltUri(), bolt.toContentValues())
    }

    private fun updateBolt(contentResolver: ContentResolver, bolt: Bolt) {
        contentResolver.update(WrenchProviderContract.boltUri(bolt.id!!),
                bolt.toContentValues(), null, null)
    }

    @Before
    fun init() {
        val context = InstrumentationRegistry.getTargetContext()
        contentResolver = context.contentResolver
        resources = context.resources
    }

    @Test
    fun checkStringValue() {
        updateString(contentResolver, resources!!.getString(R.string.string_configuration), "string1")
        onView(withId(R.id.string_configuration)).check(matches(withText("string1")))
        updateString(contentResolver, resources!!.getString(R.string.string_configuration), "string2")
        onView(withId(R.id.string_configuration)).check(matches(withText("string2")))
        updateString(contentResolver, resources!!.getString(R.string.string_configuration), "string3")
        onView(withId(R.id.string_configuration)).check(matches(withText("string3")))
    }

    @Test
    fun checkIntValue() {
        updateInteger(contentResolver, resources!!.getString(R.string.int_configuration), 1)
        onView(withId(R.id.int_configuration)).check(matches(withText("1")))
        updateInteger(contentResolver, resources!!.getString(R.string.int_configuration), 2)
        onView(withId(R.id.int_configuration)).check(matches(withText("2")))
        updateInteger(contentResolver, resources!!.getString(R.string.int_configuration), 1)
        onView(withId(R.id.int_configuration)).check(matches(withText("1")))
    }

    @Test
    fun checkBooleanValue() {
        updateBoolean(contentResolver, resources!!.getString(R.string.boolean_configuration), true)
        onView(withId(R.id.boolean_configuration)).check(matches(withText("true")))
        updateBoolean(contentResolver, resources!!.getString(R.string.boolean_configuration), false)
        onView(withId(R.id.boolean_configuration)).check(matches(withText("false")))
        updateBoolean(contentResolver, resources!!.getString(R.string.boolean_configuration), true)
        onView(withId(R.id.boolean_configuration)).check(matches(withText("true")))
    }
}
