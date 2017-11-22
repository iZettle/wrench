package com.izettle.wrench.core

import android.content.ContentValues
import android.database.Cursor

data class Bolt(var id: Long = 0,
                val type: String = "",
                val key: String = "",
                val value: String? = null) {

    fun toContentValues(): ContentValues {
        val contentValues = ContentValues()

        contentValues.put(ColumnNames.Bolt.COL_ID, id)
        contentValues.put(ColumnNames.Bolt.COL_KEY, key)
        contentValues.put(ColumnNames.Bolt.COL_VALUE, value)
        contentValues.put(ColumnNames.Bolt.COL_TYPE, type)

        return contentValues
    }

    object TYPE {
        const val BOOLEAN = "boolean"
        const val STRING = "string"
        const val INTEGER = "integer"
        const val ENUM = "enum"
    }

    companion object {

        @JvmStatic
        fun fromContentValues(values: ContentValues): Bolt {

            return Bolt(id = values.getAsLong(ColumnNames.Bolt.COL_ID) ?: 0,
                    type = values.getAsString(ColumnNames.Bolt.COL_TYPE),
                    key = values.getAsString(ColumnNames.Bolt.COL_KEY),
                    value = values.getAsString(ColumnNames.Bolt.COL_VALUE))
        }

        @JvmStatic
        fun fromCursor(cursor: Cursor): Bolt {
            return Bolt(id = cursor.getLongOrThrow(ColumnNames.Bolt.COL_ID),
                    type = cursor.getStringOrThrow(ColumnNames.Bolt.COL_TYPE),
                    key = cursor.getStringOrThrow(ColumnNames.Bolt.COL_KEY),
                    value = cursor.getStringOrNull(ColumnNames.Bolt.COL_VALUE)
            )
        }
    }
}


private fun Cursor.getStringOrThrow(columnName: String): String = getStringOrNull(columnName)!!

private fun Cursor.getStringOrNull(columnName: String): String? {
    val index = getColumnIndexOrThrow(columnName)
    return if (isNull(index)) null else getString(index)
}

private fun Cursor.getLongOrThrow(columnName: String): Long = getLongOrNull(columnName)!!

private fun Cursor.getLongOrNull(columnName: String): Long? {
    val index = getColumnIndexOrThrow(columnName)
    return if (isNull(index)) null else getLong(index)
}

