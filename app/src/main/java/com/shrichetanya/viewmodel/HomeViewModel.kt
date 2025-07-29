package com.shrichetanya.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.shrichetanya.utils.NetworkResult
import com.shrichetanya.data.HomeRepository
import com.shrichetanya.model.BookingRequest
import com.shrichetanya.model.ClientResponse
import com.shrichetanya.model.DeliveryRequest
import com.shrichetanya.model.SaveBookingResponse
import com.shrichetanya.model.UploadFileResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import javax.inject.Inject

@HiltViewModel

class HomeViewModel @Inject constructor(
    private val repository: HomeRepository,
    application: Application
) : BaseViewModel(application) {

    val clientResponse: MutableLiveData<NetworkResult<ClientResponse>> = MutableLiveData()
    val saveBookingResponse: MutableLiveData<NetworkResult<SaveBookingResponse>> = MutableLiveData()
    val deliverBookingResponse: MutableLiveData<NetworkResult<SaveBookingResponse>> =
        MutableLiveData()
    val uploadFileResponse: MutableLiveData<NetworkResult<UploadFileResponse>> = MutableLiveData()

    fun getClientList(userId: String) = viewModelScope.launch {
        clientResponse.value = NetworkResult.Loading()
        repository.getClientList(userId).collect { values ->
            clientResponse.value = values
        }
    }

    /*fun saveBooking(
        userId: String,
        clinetId: String,
        podNo: String,
        isDocument: Boolean,
        mode: String,
        itemName: String,
        itemQuantity: String,
        itemValue: String,
        sourceAddress: String,
        sourcePinCode: String,
        destinationAddress: String,
        destinationPinCode: String,
        bookingName: String,
        bookingMobileNo: String
    ) = viewModelScope.launch {
        Log.i("param", "userId-"+ userId+
            ",clinetId-"+ clinetId+
            ",podNo-"+ podNo+
            ",isDocument-"+ isDocument+
            ",mode-"+ mode+
            ",itemName-"+ itemName+
            ",itemQuantity-"+ itemQuantity+
            ",itemValue-"+ itemValue+
            ",sourceAddress-"+ sourceAddress+
            ",sourcePinCode-"+ sourcePinCode+
            ",destinationAddress-"+ destinationAddress+
            ",destinationPinCode-"+ destinationPinCode+
            ",bookingName-"+ bookingName+
            ",bookingMobileNo-"+ bookingMobileNo)
        saveBookingResponse.value = NetworkResult.Loading()
        repository.saveBooking(
            userId,
            clinetId,
            podNo,
            isDocument,
            mode,
            itemName,
            itemQuantity,
            itemValue,
            sourceAddress,
            sourcePinCode,
            destinationAddress,
            destinationPinCode,
            bookingName,
            bookingMobileNo
        ).collect { values ->
            saveBookingResponse.value = values
        }
    }*/
    fun saveBooking(
      bookingRequest: BookingRequest
    ) = viewModelScope.launch {
        saveBookingResponse.value = NetworkResult.Loading()
        repository.saveBooking(
            bookingRequest
        ).collect { values ->
            saveBookingResponse.value = values
        }
    }
    fun deliverBooking(
       deliveryRequest: DeliveryRequest
    ) = viewModelScope.launch {
        deliverBookingResponse.value = NetworkResult.Loading()
        repository.deliverBooking(
            deliveryRequest
        ).collect { values ->
            deliverBookingResponse.value = values
        }
    }

    fun uploadBookingFile(
        bookingId: RequestBody,
        userId: RequestBody,
        file: MultipartBody.Part
    ) = viewModelScope.launch {
        uploadFileResponse.value = NetworkResult.Loading()
        repository.uploadBookingFile(
            bookingId,
            userId,
            file
        ).collect { values ->
            uploadFileResponse.value = values
        }
    }
}