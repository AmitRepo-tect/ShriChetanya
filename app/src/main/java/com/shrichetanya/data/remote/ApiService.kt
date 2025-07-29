package com.shrichetanya.data.remote

import com.shrichetanya.model.BookingRequest
import com.shrichetanya.utils.Constants
import com.shrichetanya.model.ClientResponse
import com.shrichetanya.model.DeliveryRequest
import com.shrichetanya.model.LoginRequest
import com.shrichetanya.model.LoginResponse
import com.shrichetanya.model.SaveBookingResponse
import com.shrichetanya.model.UploadFileResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query


interface ApiService {

    @POST(Constants.GET_CLIENT_LIST)
    suspend fun getClientList(@Query("userId") userId: String): Response<ClientResponse>

   /* @POST(Constants.LOGIN)
    suspend fun login(
        @Query("userName") userName: String,
        @Query("password") password: String
    ): Response<LoginResponse>*/
   @POST(Constants.LOGIN)
   suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

   /* @POST(Constants.SAVE_BOOKING)
    suspend fun saveBooking(
        @Query("userId") userId: String,
        @Query("clinetId") clinetId: String,
        @Query("podNo") podNo: String,
        @Query("isDocument") isDocument: Boolean,
        @Query("mode") mode: String,
        @Query("itemName") itemName: String,
        @Query("itemQuantity") itemQuantity: String,
        @Query("itemValue") itemValue: String,
        @Query("sourceAddress") sourceAddress: String,
        @Query("sourcePinCode") sourcePinCode: String,
        @Query("destinationAddress") destinationAddress: String,
        @Query("destinationPinCode") destinationPinCode: String,
        @Query("bookingName") bookingName: String,
        @Query("bookingMobileNo") bookingMobileNo: String
    ): Response<SaveBookingResponse>*/
    @POST(Constants.SAVE_BOOKING)
    suspend fun saveBooking(@Body bookingRequest: BookingRequest): Response<SaveBookingResponse>
   /* @POST(Constants.DELIVER_BOOKING)
    suspend fun deliverBooking(
        @Query("userId") userId: String,
        @Query("podNo") podNo: String,
        @Query("receiverName") receiverName: String,
        @Query("receiverMobileNo") receiverMobileNo: String,
        @Query("receiverAddress") receiverAddress: String,
    ): Response<SaveBookingResponse>*/
    @POST(Constants.DELIVER_BOOKING)
    suspend fun deliverBooking(
        @Body deliveryRequest: DeliveryRequest
    ): Response<SaveBookingResponse>

    @Multipart
    @POST(Constants.UPLOAD_BOOKING_FILE)
    suspend fun uploadBookingFile(
        @Part("bookingId") bookingId: RequestBody,
        @Part("userId") userId: RequestBody,
        @Part file: MultipartBody.Part
    ): Response<UploadFileResponse>
}