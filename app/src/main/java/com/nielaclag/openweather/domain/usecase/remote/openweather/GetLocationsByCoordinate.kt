package com.nielaclag.openweather.domain.usecase.remote.openweather

import com.nielaclag.openweather.common.constants.Constants
import com.nielaclag.openweather.common.util.DataResponse
import com.nielaclag.openweather.common.util.Resource
import com.nielaclag.openweather.data.mapper.toDomain
import com.nielaclag.openweather.domain.model.weather.LocationInfo
import com.nielaclag.openweather.domain.repository.remote.OpenWeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.io.IOException
import javax.inject.Inject

/**
 * Created by Niel on 10/24/2024.
 */
class GetLocationsByCoordinate @Inject constructor(
    private val repository: OpenWeatherRepository
) {
    operator fun invoke(
        latitude: Double,
        longitude: Double,
        limit: Int
    ): Flow<Resource<Array<LocationInfo>>> = callbackFlow {
        send(Resource.Loading())
        try {
            val dataResponse = repository.getLocationsByCoordinate(
                appId = Constants.OPEN_WEATHER_API_KEY,
                latitude = latitude,
                longitude = longitude,
                limit = limit
            )
            if (dataResponse is DataResponse.Success) {
                send(
                    Resource.Success(
                        code = dataResponse.statusCode,
                        message = dataResponse.message,
                        data = dataResponse.data?.map { it.toDomain() }?.toTypedArray() ?: arrayOf()
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