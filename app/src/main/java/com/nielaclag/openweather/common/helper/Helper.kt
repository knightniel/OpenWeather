package com.nielaclag.openweather.common.helper

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import com.nielaclag.openweather.common.constants.UtilConstants
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapter
import okhttp3.ResponseBody
import org.json.JSONObject
import timber.log.Timber
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.regex.Pattern
import kotlin.math.*

/**
 * Created by Niel on 10/21/2024.
 */
fun log(message: String, vararg args: Any?, throwable: Throwable? = null, tag: String = "") {
    val maxLogSize = 1900
    for (i in 0..(message.length / maxLogSize)) {
        val start = i * maxLogSize
        var end = (i+1) * maxLogSize
        end = if (end > message.length) message.length else end
        Timber
            .tag("xxxLog${ if (tag.isNotBlank()) " $tag" else "" }")
            .d(t = throwable, message = message.substring(start, end), args = args)
    }
}

fun String.isEmail(): Boolean {
    return if (isBlank()) {
        false
    } else {
        Pattern.compile(UtilConstants.EMAIL_ADDRESS_PATTERN, Pattern.CASE_INSENSITIVE).matcher(this).matches()
    }
}

fun Date.dateDiffInDays(dateTo: Date?, computeBeforeAndAfter: Boolean = true): Long? {
    return if (dateTo != null) {
        val diff = if (computeBeforeAndAfter) {
            if (time >= dateTo.time) {
                time - dateTo.time
            } else {
                dateTo.time - time
            }
        } else {
            dateTo.time - time
        }
        val seconds = diff / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24
        return days
    } else {
        null
    }
}

fun Date.dateDiffInHours(dateTo: Date?, computeBeforeAndAfter: Boolean = true): Long? {
    return if (dateTo != null) {
        val diff = if (computeBeforeAndAfter) {
            if (time >= dateTo.time) {
                time - dateTo.time
            } else {
                dateTo.time - time
            }
        } else {
            dateTo.time - time
        }
        val seconds = diff / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        return hours
    } else {
        null
    }
}

fun Date.dateDiffInMinutes(dateTo: Date?, computeBeforeAndAfter: Boolean = true): Long? {
    return if (dateTo != null) {
        val diff = if (computeBeforeAndAfter) {
            if (time >= dateTo.time) {
                time - dateTo.time
            } else {
                dateTo.time - time
            }
        } else {
            dateTo.time - time
        }
        val seconds = diff / 1000
        val minutes = seconds / 60
        return minutes
    } else {
        null
    }
}

fun Date.dateDiffInSeconds(dateTo: Date?, computeBeforeAndAfter: Boolean = true): Long? {
    return if (dateTo != null) {
        val diff = if (computeBeforeAndAfter) {
            if (time >= dateTo.time) {
                time - dateTo.time
            } else {
                dateTo.time - time
            }
        } else {
            dateTo.time - time
        }
        val seconds = diff / 1000
        return seconds
    } else {
        null
    }
}

fun Date.dateDiffInMilliSeconds(dateTo: Date?, computeBeforeAndAfter: Boolean = true): Long? {
    return if (dateTo != null) {
        val diff = if (computeBeforeAndAfter) {
            if (time >= dateTo.time) {
                time - dateTo.time
            } else {
                dateTo.time - time
            }
        } else {
            dateTo.time - time
        }
        return diff
    } else {
        null
    }
}

fun Date.toCalendar(): Calendar {
    return Calendar.getInstance().apply {
        timeInMillis = this@toCalendar.time
    }
}

@OptIn(ExperimentalStdlibApi::class)
inline fun <reified T> Moshi.fromJson(data: String): T? {
    return this.adapter<T>().fromJson(data)
}

@OptIn(ExperimentalStdlibApi::class)
inline fun <reified T> Moshi.toJson(data: T): String {
    return this.adapter<T>().toJson(data)
}

fun Float.toFloat2(forceDecimal: Boolean = false): String {
    val float2f = String.format(Locale.getDefault(), "%.2f", this)
    return if (forceDecimal || float2f.split(".").last() != "00") {
        float2f
    } else {
        toInt().toString()
    }
}

fun String.toProperCase(): String {
    return if (isNotEmpty()) {
        split(" ")  // Split the string into words
            .joinToString(" ") { word ->
                word.lowercase().replaceFirstChar { char ->
                    char.uppercase(Locale.getDefault())
                }
            }
    } else {
        uppercase(Locale.getDefault())
    }
}

fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

inline fun <reified T> ResponseBody.toJsonObject(moshi: Moshi): T? {
    val jsonObject = JSONObject(charStream().readText())
    return moshi.fromJson(jsonObject.toString())
}

fun ZonedDateTime.toLocalZonedDateTime(): ZonedDateTime {
    val zoneId = ZoneId.of(TimeZone.getDefault().id)
    val newZonedDateTime = toLocalDateTime().atZone(zoneId)
    return newZonedDateTime
}

fun String.toUrlEncoded(): String {
    return URLEncoder.encode(this, StandardCharsets.UTF_8.toString())
}

fun haversine(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
    val r = 6371.0 // Earth's radius in km
    val latDistance = Math.toRadians(lat2 - lat1)
    val lonDistance = Math.toRadians(lon2 - lon1)
    val a = sin(latDistance / 2) * sin(latDistance / 2) +
            cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
            sin(lonDistance / 2) * sin(lonDistance / 2)
    val c = 2 * atan2(sqrt(a), sqrt(1 - a))
    return r * c // Distance in km
}