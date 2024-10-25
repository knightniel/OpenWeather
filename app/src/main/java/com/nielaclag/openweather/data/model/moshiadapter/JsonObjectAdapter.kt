package com.nielaclag.openweather.data.model.moshiadapter

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.ToJson
import org.json.JSONObject

/**
 * Created by Niel on 10/21/2024.
 */
class JsonObjectAdapter : JsonAdapter<JSONObject>() {

    @FromJson
    override fun fromJson(reader: JsonReader): JSONObject? {
        return try {
            val json = reader.readJsonValue() as Map<*, *>
            JSONObject(json)
        } catch (e: JsonDataException) {
            null
        }
    }

    @ToJson
    override fun toJson(writer: JsonWriter, value: JSONObject?) {
        if (value != null) {
            writer.jsonValue(value.toString())
        }
    }
}