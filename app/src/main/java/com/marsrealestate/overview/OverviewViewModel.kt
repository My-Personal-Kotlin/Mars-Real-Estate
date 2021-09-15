package com.marsrealestate.overview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.marsrealestate.network.MarsApi
import com.marsrealestate.network.MarsApiFilter
import com.marsrealestate.network.MarsProperty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


enum class MarsApiStatus { LOADING, ERROR, DONE }
/**
 * The [ViewModel] that is attached to the [OverviewFragment].
 */
class OverviewViewModel : ViewModel() {

    // The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<MarsApiStatus>()
    val status: LiveData<MarsApiStatus>
        get() = _status

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    // Internally, we use a MutableLiveData, because we will be updating the List of MarsProperty
    // with new values
    private val _properties = MutableLiveData<List<MarsProperty>>()
    val properties: LiveData<List<MarsProperty>>
        get() = _properties
//
//    // Internally, we use a MutableLiveData to handle navigation to the selected property
//    private val _navigateToSelectedProperty = MutableLiveData<MarsProperty>()
//
//    // The external immutable LiveData for the navigation property
//    val navigateToSelectedProperty: LiveData<MarsProperty>
//        get() = _navigateToSelectedProperty
//


    /**
     * Call getMarsRealEstateProperties() on init so we can display status immediately.
     */
    init {
        getMarsRealEstateProperties()
    }

    /**
     * Gets filtered Mars real estate property information from the Mars API Retrofit service and
     * updates the [MarsProperty] [List] and [MarsApiStatus] [LiveData]. The Retrofit service
     * returns a coroutine Deferred, which we await to get the result of the transaction.
     * @param filter the [MarsApiFilter] that is sent as part of the web server request
     */
    private fun getMarsRealEstateProperties() {
//        MarsApi.retrofitService.getProperties().enqueue(object : retrofit2.Callback<List<MarsProperty>> {
//            override fun onFailure(call: Call<List<MarsProperty>>, t: Throwable) {
//                _response.value = "Failure: " + t.message
//            }
//
//             override fun onResponse(call: Call<List<MarsProperty>>, response: Response<List<MarsProperty>>) {
//                _response.value = "Success: ${response.body()?.get(0)} Mars Property rerived !"
//            }
//        })
        coroutineScope.launch {
            _status.value = MarsApiStatus.LOADING
            try {
                var listResult = MarsApi.retrofitService.getProperties() // run on background thread
                _status.value = MarsApiStatus.DONE
                if(listResult.size > 0){
                    _properties.value = listResult
                }
            }catch (t:Throwable){
                _status.value = MarsApiStatus.ERROR
                _properties.value = ArrayList()
            }


        }
//        viewModelScope.launch {
//            _response.value = MarsApiStatus.LOADING
//            try {
//              //  _properties.value = MarsApi.retrofitService.getProperties(filter.value)
//                _response.value = MarsApiStatus.DONE
//            } catch (e: Exception) {
//                _response.value = MarsApiStatus.ERROR
//             //   _properties.value = ArrayList()
//            }
//        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
    //
//    /**
//     */
//
//    /**
//     * When the property is clicked, set the [_navigateToSelectedProperty] [MutableLiveData]
//     * @param marsProperty The [MarsProperty] that was clicked on.
//     */
//    fun displayPropertyDetails(marsProperty: MarsProperty) {
//        _navigateToSelectedProperty.value = marsProperty
//    }
//
//    /**
//     * After the navigation has taken place, make sure navigateToSelectedProperty is set to null
//     */
//    fun displayPropertyDetailsComplete() {
//        _navigateToSelectedProperty.value = null
//    }

    /**
     * Updates the data set filter for the web services by querying the data with the new filter
     * by calling [getMarsRealEstateProperties]
     * @param filter the [MarsApiFilter] that is sent as part of the web server request
//     */
//    fun updateFilter(filter: MarsApiFilter) {
//        getMarsRealEstateProperties(filter)
//    }
}
