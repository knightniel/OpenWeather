package com.nielaclag.openweather.domain.usecase.remote.openweather

import com.nielaclag.openweather.common.constants.Constants
import com.nielaclag.openweather.common.helper.toJsonObject
import com.nielaclag.openweather.common.util.OpenWeatherError
import com.nielaclag.openweather.common.util.Resource
import com.nielaclag.openweather.data.mapper.toDomain
import com.nielaclag.openweather.domain.model.weather.LocationInfo
import com.nielaclag.openweather.domain.repository.remote.OpenWeatherRepository
import com.squareup.moshi.Moshi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

/**
 * Created by Niel on 10/24/2024.
 */
class GetLocationsByCoordinate @Inject constructor(
    private val repository: OpenWeatherRepository,
    private val moshi: Moshi
) {
    operator fun invoke(
        latitude: Double,
        longitude: Double,
        limit: Int
    ): Flow<Resource<Array<LocationInfo>>> = callbackFlow {
        send(Resource.Loading())
        delay(1000)
        try {
            val response = repository.getLocationsByCoordinates(
                appId = Constants.OPEN_WEATHER_API_KEY,
                latitude = latitude,
                longitude = longitude,
                limit = limit
            )
            if (response.isSuccessful) {
                val data = response.body()?.map { it.toDomain() }?.toTypedArray() ?: arrayOf()
                send(Resource.Success(code = response.code(), data = data))
            } else {
                try {
                    val responseError = response.errorBody()?.toJsonObject<OpenWeatherError>(moshi)
                    send(
                        Resource.Error(
                            code = response.code(),
                            message = responseError?.message ?: "An unexpected error occurred."
                        )
                    )
                } catch (_: Exception) {
                    send(
                        Resource.Error(
                            code = response.code(),
                            message = "An unexpected error occurred."
                        )
                    )
                }
            }
        } catch (e: HttpException) {
            send(
                Resource.Error(
                    code = e.code(),
                    message = e.message() ?: "An unexpected error occurred."
                )
            )
        } catch (e: IOException) {
            send(Resource.Error(message = "Couldn't reach server. Check your internet connection."))
        } catch (e: Exception) {
            send(Resource.Error(message = "An unexpected error occurred."))
        } finally {
            awaitClose {
            }
        }
    }
}