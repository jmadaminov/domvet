package com.anymobile.domvet.ui.main.mycalls.historycalls

import androidx.lifecycle.viewModelScope
import com.anymobile.domvet.util.Constants
import com.anymobile.domvet.util.ResultWrapper
import com.anymobile.domvet.domain.model.PassengerPost
import com.anymobile.domvet.domain.usecases.GetHistoryPassengerPost
import com.anymobile.domvet.ui.BaseViewModel
import com.anymobile.domvet.util.AppPreferences
import com.anymobile.domvet.util.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import splitties.experimental.ExperimentalSplittiesApi
import javax.inject.Inject

class HistoryCallsViewModel @Inject constructor(val getHistoryPassengerPost: GetHistoryPassengerPost) :
    BaseViewModel() {

    val historyPostsResponse = SingleLiveEvent<ResultWrapper<List<PassengerPost>>>()
    val cancelOrderReponse = SingleLiveEvent<ResultWrapper<String>>()
    val updateOrderReponse = SingleLiveEvent<ResultWrapper<String>>()
    var currentPage = 0

    @ExperimentalSplittiesApi
    fun getHistoryPosts(page: Int) {
        currentPage = page
        historyPostsResponse.value = ResultWrapper.InProgress
        viewModelScope.launch(Dispatchers.IO) {
            val response = getHistoryPassengerPost.execute(hashMapOf(
                Pair(Constants.TXT_TOKEN, AppPreferences.token),
                Pair(Constants.TXT_LANG, AppPreferences.language),
                Pair(Constants.TXT_PAGE, page)))

            withContext(Dispatchers.Main) {
                historyPostsResponse.value = response
            }

        }

    }

}