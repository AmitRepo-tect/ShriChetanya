package com.shrichetanya.data.remote

import com.shrichetanya.model.BookingRequest
import com.shrichetanya.model.DeliveryRequest
import com.shrichetanya.model.LoginRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Part
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val apiService: ApiService) {

    /*  suspend fun login(userName: String, password: String) =
          apiService.login(userName = userName, password = password)*/
    suspend fun login(loginRequest: LoginRequest) =
        apiService.login(loginRequest)

    suspend fun getClientList(userId: String) =
        apiService.getClientList(userId = userId)


    /*suspend fun saveBooking(
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
    ) = apiService.saveBooking(
        userId = userId,
        clinetId = clinetId,
        podNo = podNo,
        isDocument = isDocument,
        mode = mode,
        itemName = itemName,
        itemQuantity = itemQuantity,
        itemValue = itemValue,
        sourceAddress = sourceAddress,
        sourcePinCode = sourcePinCode,
        destinationAddress = destinationAddress,
        destinationPinCode = destinationPinCode,
        bookingName = bookingName,
        bookingMobileNo = bookingMobileNo
    )*/
    suspend fun saveBooking(
        bookingRequest: BookingRequest
    ) = apiService.saveBooking(
        bookingRequest
    )

/*    suspend fun deliverBooking(
        userId: String,
        podNo: String,
        receiverName: String,
        receiverMobileNo: String,
        receiverAddress: String,
    ) = apiService.deliverBooking(
        userId = userId,
        podNo = podNo,
        receiverName = receiverName,
        receiverMobileNo = receiverMobileNo,
        receiverAddress = receiverAddress
    )*/
suspend fun deliverBooking(
   deliveryRequest: DeliveryRequest
) = apiService.deliverBooking(
    deliveryRequest
)

    suspend fun uploadBookingFile(
        bookingId: RequestBody,
        userId: RequestBody,
        file: MultipartBody.Part
    ) = apiService.uploadBookingFile(
        bookingId = bookingId,
        userId = userId,
        file = file
    )

}