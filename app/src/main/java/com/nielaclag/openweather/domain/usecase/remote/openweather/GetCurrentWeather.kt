package com.nielaclag.openweather.domain.usecase.remote.openweather

import com.nielaclag.openweather.common.constants.Constants
import com.nielaclag.openweather.common.util.DataResponse
import com.nielaclag.openweather.common.util.Resource
import com.nielaclag.openweather.data.mapper.toDomain
import com.nielaclag.openweather.domain.model.weather.Weather
import com.nielaclag.openweather.domain.repository.remote.OpenWeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.io.IOException
import javax.inject.Inject

/**
 * Created by Niel on 10/21/2024.
 */
class GetCurrentWeather @Inject constructor(
    private val repository: OpenWeatherRepository
) {
    operator fun invoke(
        latitude: Double,
        longitude: Double,
        units: String?
    ): Flow<Resource<Weather>> = callbackFlow {
        send(Resource.Loading())
        try {
            val dataResponse = repository.getCurrentWeather(
                appId = Constants.OPEN_WEATHER_API_KEY,
                latitude = latitude,
                longitude = longitude,
                units = units
            )
            if (dataResponse is DataResponse.Success) {
                send(
                    Resource.Success(
                        code = dataResponse.statusCode,
                        message = dataResponse.message,
                        data = dataResponse.data?.toDomain()
                    )
                )
            } else {
                send(
                    Resource.Error(
                        code = dataResponse.statusCode,
                        message = dataResponse.message ?: "An unexpected error occurred."
                    )
                )
            }
        } catch (e: IOException) {
            send(
                Resource.Error(
                    message = e.message ?: "Couldn't reach server. Check your internet connection."
                )
            )
        } catch (e: Exception) {
            send(
                Resource.Error(
                    message = "An unexpected error occurred."
                )
            )
        } finally {
            close()
        }
    }
}