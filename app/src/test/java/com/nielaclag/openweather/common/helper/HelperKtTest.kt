package com.nielaclag.openweather.common.helper

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.util.Date

/**
 * Created by Niel on 10/25/2024.
 */
class HelperKtTest {

    @Test
    fun `isEmail, Valid Email`() {
        var email = "test@email.com"
        assertThat(email.isEmail()).isTrue()

        email = "paul.example@email.com"
        assertThat(email.isEmail()).isTrue()
    }

    @Test
    fun `isEmail, Invalid Email`() {
        var email = ""
        assertThat(email.isEmail()).isFalse()

        email = "test@email"
        assertThat(email.isEmail()).isFalse()

        email = "paul.example.com"
        assertThat(email.isEmail()).isFalse()

        email = "paul.example.com/"
        assertThat(email.isEmail()).isFalse()

        email = "paul.example.com.com"
        assertThat(email.isEmail()).isFalse()

        email = "paul@example@com.com"
        assertThat(email.isEmail()).isFalse()
    }

    @Test
    fun `dateDiffInDays on any order`() {
        // Thursday, October 24, 2024 6:32:37 PM
        val dateFrom = Date(1729794757000)
        // Saturday, October 26, 2024 6:32:37 PM
        val dateTo = Date(1729967557000)

        var diff = dateFrom.dateDiffInDays(
            dateTo = dateTo,
            computeBeforeAndAfter = true
        )
        assertThat(diff).isEqualTo(2)

        diff = dateTo.dateDiffInDays(
            dateTo = dateFrom,
            computeBeforeAndAfter = true
        )
        assertThat(diff).isEqualTo(2)
    }

    @Test
    fun `dateDiffInDays in order`() {
        // Thursday, October 24, 2024 6:32:37 PM
        val dateFrom = Date(1729794757000)
        // Saturday, October 26, 2024 6:32:37 PM
        val dateTo = Date(1729967557000)

        var diff = dateFrom.dateDiffInDays(
            dateTo = dateTo,
            computeBeforeAndAfter = false
        )
        assertThat(diff).isEqualTo(2)

        diff = dateTo.dateDiffInDays(
            dateTo = dateFrom,
            computeBeforeAndAfter = false
        )
        assertThat(diff).isEqualTo(-2)
    }

    @Test
    fun `dateDiffInHours on any order`() {
        // Saturday, October 26, 2024 6:32:37 PM
        val dateFrom = Date(1729967557000)
        // Saturday, October 26, 2024 8:32:37 PM
        val dateTo = Date(1729974757000)

        var diff = dateFrom.dateDiffInHours(
            dateTo = dateTo,
            computeBeforeAndAfter = true
        )
        assertThat(diff).isEqualTo(2)

        diff = dateTo.dateDiffInHours(
            dateTo = dateFrom,
            computeBeforeAndAfter = true
        )
        assertThat(diff).isEqualTo(2)
    }

    @Test
    fun `dateDiffInHours in order`() {
        // Saturday, October 26, 2024 6:32:37 PM
        val dateFrom = Date(1729967557000)
        // Saturday, October 26, 2024 8:32:37 PM
        val dateTo = Date(1729974757000)

        var diff = dateFrom.dateDiffInHours(
            dateTo = dateTo,
            computeBeforeAndAfter = false
        )
        assertThat(diff).isEqualTo(2)

        diff = dateTo.dateDiffInHours(
            dateTo = dateFrom,
            computeBeforeAndAfter = false
        )
        assertThat(diff).isEqualTo(-2)
    }

    @Test
    fun `dateDiffInMinutes on any order`() {
        // Saturday, October 26, 2024 6:32:37 PM
        val dateFrom = Date(1729967557000)
        // Saturday, October 26, 2024 6:34:37 PM
        val dateTo = Date(1729967677000)

        var diff = dateFrom.dateDiffInMinutes(
            dateTo = dateTo,
            computeBeforeAndAfter = true
        )
        assertThat(diff).isEqualTo(2)

        diff = dateTo.dateDiffInMinutes(
            dateTo = dateFrom,
            computeBeforeAndAfter = true
        )
        assertThat(diff).isEqualTo(2)
    }

    @Test
    fun `dateDiffInMinutes in order`() {
        // Saturday, October 26, 2024 6:32:37 PM
        val dateFrom = Date(1729967557000)
        // Saturday, October 26, 2024 6:34:37 PM
        val dateTo = Date(1729967677000)

        var diff = dateFrom.dateDiffInMinutes(
            dateTo = dateTo,
            computeBeforeAndAfter = false
        )
        assertThat(diff).isEqualTo(2)

        diff = dateTo.dateDiffInMinutes(
            dateTo = dateFrom,
            computeBeforeAndAfter = false
        )
        assertThat(diff).isEqualTo(-2)
    }

    @Test
    fun `dateDiffInSeconds on any order`() {
        // Saturday, October 26, 2024 6:34:37 PM
        val dateFrom = Date(1729967677000)
        // Saturday, October 26, 2024 6:34:39 PM
        val dateTo = Date(1729967679000)

        var diff = dateFrom.dateDiffInSeconds(
            dateTo = dateTo,
            computeBeforeAndAfter = true
        )
        assertThat(diff).isEqualTo(2)

        diff = dateTo.dateDiffInSeconds(
            dateTo = dateFrom,
            computeBeforeAndAfter = true
        )
        assertThat(diff).isEqualTo(2)
    }

    @Test
    fun `dateDiffInSeconds in order`() {
        // Saturday, October 26, 2024 6:34:37 PM
        val dateFrom = Date(1729967677000)
        // Saturday, October 26, 2024 6:34:39 PM
        val dateTo = Date(1729967679000)

        var diff = dateFrom.dateDiffInSeconds(
            dateTo = dateTo,
            computeBeforeAndAfter = false
        )
        assertThat(diff).isEqualTo(2)

        diff = dateTo.dateDiffInSeconds(
            dateTo = dateFrom,
            computeBeforeAndAfter = false
        )
        assertThat(diff).isEqualTo(-2)
    }

    @Test
    fun `dateDiffInMilliSeconds on any order`() {
        // Saturday, October 26, 2024 6:34:37 PM
        val dateFrom = Date(1729967677000)
        // Saturday, October 26, 2024 6:34:39 PM
        val dateTo = Date(1729967679000)

        var diff = dateFrom.dateDiffInMilliSeconds(
            dateTo = dateTo,
            computeBeforeAndAfter = true
        )
        assertThat(diff).isEqualTo(2000)

        diff = dateTo.dateDiffInMilliSeconds(
            dateTo = dateFrom,
            computeBeforeAndAfter = true
        )
        assertThat(diff).isEqualTo(2000)
    }

    @Test
    fun `dateDiffInMilliSeconds in order`() {
        // Saturday, October 26, 2024 6:34:37 PM
        val dateFrom = Date(1729967677000)
        // Saturday, October 26, 2024 6:34:39 PM
        val dateTo = Date(1729967679000)

        var diff = dateFrom.dateDiffInMilliSeconds(
            dateTo = dateTo,
            computeBeforeAndAfter = false
        )
        assertThat(diff).isEqualTo(2000)

        diff = dateTo.dateDiffInMilliSeconds(
            dateTo = dateFrom,
            computeBeforeAndAfter = false
        )
        assertThat(diff).isEqualTo(-2000)
    }

    @Test
    fun `toCalendar, valid time epoch`() {
        // Saturday, October 26, 2024 6:34:37 PM
        val date = Date(1729967677000)
        val calendar = date.toCalendar()

        assertThat(date.time).isEqualTo(calendar.time.time)
    }

    @Test
    fun `toFloat2, returns in integer string format if its formatted value has zero or no decimal parts`() {
        var data2f = 12345f.toFloat2(forceDecimal = false)
        assertThat(data2f).isEqualTo("12345")

        data2f = 12345.001f.toFloat2(forceDecimal = false)
        assertThat(data2f).isEqualTo("12345")
    }


    @Test
    fun `toFloat2, returns in float string format if its formatted value has decimal parts`() {
        var data2f = 12345.5f.toFloat2(forceDecimal = false)
        assertThat(data2f).isEqualTo("12345.50")

        data2f = 12345.001f.toFloat2(forceDecimal = false)
        assertThat(data2f).isEqualTo("12345")
    }

    @Test
    fun `toFloat2, forces to format with decimal places`() {
        var data2f = 12345.00f.toFloat2(forceDecimal = true)
        assertThat(data2f).isEqualTo("12345.00")

        data2f = 12345.001f.toFloat2(forceDecimal = true)
        assertThat(data2f).isEqualTo("12345.00")

        data2f = 12345.091f.toFloat2(forceDecimal = true)
        assertThat(data2f).isEqualTo("12345.09")
    }

    @Test
    fun `toProperCase, string to proper case`() {
        var data = "the QUICK brown fox jumPs over the lazy dog near the riverbank.   hellO kOtLiN. 123".toProperCase()
        assertThat(data).isEqualTo("The Quick Brown Fox Jumps Over The Lazy Dog Near The Riverbank.   Hello Kotlin. 123")

        data = "".toProperCase()
        assertThat(data).isEqualTo(data)
    }

    @Test
    fun toUrlEncoded() {
        val data = "Kotlin & Android: Coding made easy! Use symbols like ©, €, ™. Do you love emojis like \uD83D\uDE03?".toUrlEncoded()
        assertThat(data).isEqualTo("Kotlin+%26+Android%3A+Coding+made+easy%21+Use+symbols+like+%C2%A9%2C+%E2%82%AC%2C+%E2%84%A2.+Do+you+love+emojis+like+%F0%9F%98%83%3F")
    }

    @Test
    fun `haversine, compute distance of two coordinates, within half km tolerance`() {
        val coordinateInPasig = Pair(
            14.585664324,
            121.05666644
        )
        val coordinateInBoracay = Pair(
            11.96832946,
            121.922996308
        )
        val distanceInKm = haversine(
            lat1 = coordinateInPasig.first,
            lon1 = coordinateInPasig.second,
            lat2 = coordinateInBoracay.first,
            lon2 = coordinateInBoracay.second
        )
        val expectedDistanceInKm = 305.8
        val tolerance = 0.5
        assertThat(distanceInKm).isWithin(tolerance).of(expectedDistanceInKm)
    }

    @Test
    fun `haversine, compute distance of two coordinates, 5 km distance and half km tolerance`() {
        val coordinateInPasig = Pair(
            14.585664324,
            121.05666644
        )
        val coordinate5kmNorthOfPasig = Pair(
            14.6306304043,
            121.05666644
        )
        val distanceInKm = haversine(
            lat1 = coordinateInPasig.first,
            lon1 = coordinateInPasig.second,
            lat2 = coordinate5kmNorthOfPasig.first,
            lon2 = coordinate5kmNorthOfPasig.second
        )
        val expectedDistanceInKm = 5.0
        val tolerance = 0.5
        assertThat(distanceInKm).isWithin(tolerance).of(expectedDistanceInKm)
    }

}