package com.anymobile.domvet.remote

import com.anymobile.domvet.data.repository.PlaceRemote
import com.anymobile.domvet.domain.model.Place
import com.anymobile.domvet.util.ErrorWrapper
import com.anymobile.domvet.util.ResultWrapper
import javax.inject.Inject

/**
 * Remote implementation for retrieving Bufferoo instances. This class implements the
 * [BufferooRemote] from the Data layer as it is that layers responsibility for defining the
 * operations in which data store implementation layers can carry out.
 */
class PlaceRemoteImpl @Inject constructor(private val apiService: ApiService) : PlaceRemote {

    override suspend fun getPlacesAutocomplete(token: String,
                                               lang: String,
                                               queryString: String): ResultWrapper<List<Place>> {
        return try {
            val response = apiService.getPlacesFeed(token, lang, queryString)
            if (response.code == 1) {
                val places = arrayListOf<Place>()
                response.data!!.forEach {
                    places.add(it)
                }
                ResultWrapper.Success(places)
            } else ErrorWrapper.ResponseError(response.code, response.message)
        } catch (e: Exception) {
            ErrorWrapper.SystemError(e)
        }
    }


}