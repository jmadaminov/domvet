package com.badcompany.pitak.ui.main.mytrips.historytrips

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.badcompany.core.Constants
import com.badcompany.core.ResultWrapper
import com.badcompany.domain.domainmodel.DriverPost
import com.badcompany.pitak.ui.BaseViewModel
import com.badcompany.pitak.util.AppPreferences
import com.badcompany.pitak.util.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import splitties.experimental.ExperimentalSplittiesApi
import javax.inject.Inject

class HistoryTripsViewModel @Inject constructor(/*val getOrdersByStatus: GetOrderByStatus*/) : BaseViewModel() {

    val activeOrdersResponse = SingleLiveEvent<ResultWrapper<List<DriverPost>>>()
    val cancelOrderReponse = SingleLiveEvent < ResultWrapper<String>>()
    val updateOrderReponse = SingleLiveEvent < ResultWrapper<String>>()

    @ExperimentalSplittiesApi
    fun getActiveOrders() {
        activeOrdersResponse.value = ResultWrapper.InProgress
        viewModelScope.launch(Dispatchers.IO) {
//            val response = getOrdersByStatus.execute(hashMapOf(
//                Pair(Constants.TXT_TOKEN, AppPreferences.token),
//                Pair(Constants.TXT_ORDER_STATUS, Constants.TXT_ORDER_STATUS_ACTIVE)))
//
//            withContext(Dispatchers.Main) {
//                activeOrdersResponse.value = response
//            }

        }

    }

}