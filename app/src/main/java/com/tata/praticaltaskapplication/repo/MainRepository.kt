package com.tata.praticaltaskapplication.repo

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Bundle
import com.tata.praticaltaskapplication.model.DataModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject


class MainRepository(private val context: Context) {
    companion object {
        private const val COLUMN_NAME = "data"
        val PROVIDER_NAME = "com.iav.contestdataprovider"
        val URL = "content://$PROVIDER_NAME/text"
        val CONTENT_URI = Uri.parse(URL)
    }

    suspend fun getData(enterValueLength: Int, enterValue: String): Result<DataModel> {
        return withContext(Dispatchers.IO) {
            try {
                val args = Bundle().apply {
                    putInt(ContentResolver.QUERY_ARG_LIMIT, enterValueLength)
                }
                context.contentResolver.query(
                    CONTENT_URI,
                    arrayOf(COLUMN_NAME),
                    args,
                    null
                )?.use { cursor ->
                    if (cursor.moveToFirst()) {
                        val jsonString = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
                        val jsonObject = JSONObject(jsonString).getJSONObject("randomText")
                        val dataModel = DataModel(
                            value = enterValue,
                            length = jsonObject.getInt("length"),
                            created = jsonObject.getString("created"),
                        )
                        Result.success(dataModel)
                    } else {
                        Result.failure(Exception("Empty cursor"))
                    }
                } ?: Result.failure(Exception("Cursor is null"))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}

