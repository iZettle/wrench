package com.izettle.wrench.core

import android.content.ContentValues
import android.database.Cursor

data class Bolt(var id: Long = 0,
                var type: String,
                var key: String,
                var value: String?) {

    constructor() : this(0, "", "", "")

    fun toContentValues(): ContentValues {
        val contentValues = ContentValues()

        contentValues.put(ColumnNames.Bolt.COL_ID, id)
        contentValues.put(ColumnNames.Bolt.COL_KEY, key)
        contentValues.put(ColumnNames.Bolt.COL_VALUE, value)
        contentValues.put(ColumnNames.Bolt.COL_TYPE, type)

        return contentValues
    }

    companion object {

        @JvmStatic
        fun fromContentValues(values: ContentValues): Bolt {
            val boltValue = Bolt()
            if (values.containsKey(ColumnNames.Bolt.COL_ID)) {
                boltValue.id = values.getAsLong(ColumnNames.Bolt.COL_ID)!!
            }
            boltValue.key = values.getAsString(ColumnNames.Bolt.COL_KEY)
            boltValue.value = values.getAsString(ColumnNames.Bolt.COL_VALUE)
            boltValue.type = values.getAsString(ColumnNames.Bolt.COL_TYPE)

            return boltValue
        }

        @JvmStatic
        fun fromCursor(cursor: Cursor): Bolt {
            return Bolt(cursor.getLong(ColumnNames.Bolt.COL_ID),
                    cursor.getString(ColumnNames.Bolt.COL_TYPE),
                    cursor.getString(ColumnNames.Bolt.COL_KEY),
                    cursor.getString(ColumnNames.Bolt.COL_VALUE)
            )
        }
    }
}


private fun Cursor.getString(columnName: String): String = getStringOrNull(columnName)!!

private fun Cursor.getStringOrNull(columnName: String): String? {
    val index = getColumnIndexOrThrow(columnName)
    return if (isNull(index)) null else getString(index)
}

private fun Cursor.getLong(columnName: String): Long = getLongOrNull(columnName)!!

private fun Cursor.getLongOrNull(columnName: String): Long? {
    val index = getColumnIndexOrThrow(columnName)
    return if (isNull(index)) null else getLong(index)
}

