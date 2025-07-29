package com.shrichetanya.data


import com.shrichetanya.utils.NetworkResult
import com.shrichetanya.data.remote.RemoteDataSource
import com.shrichetanya.model.BaseApiResponse
import com.shrichetanya.model.BookingRequest
import com.shrichetanya.model.ClientResponse
import com.shrichetanya.model.DeliveryRequest
import com.shrichetanya.model.LoginRequest
import com.shrichetanya.model.LoginResponse
import com.shrichetanya.model.SaveBookingResponse
import com.shrichetanya.model.UploadFileResponse
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import javax.inject.Inject


@ActivityRetainedScoped
class HomeRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : BaseApiResponse() {

   /* fun login(userName: String, password: String): Flow<NetworkResult<LoginResponse>> {
        return flow {
            emit(safeApiCall { remoteDataSource.login(userName, password) })
        }.flowOn(Dispatchers.IO)
    }*/
   fun login(loginRequest: LoginRequest): Flow<NetworkResult<LoginResponse>> {
       return flow {
           emit(safeApiCall { remoteDataSource.login(loginRequest) })
       }.flowOn(Dispatchers.IO)
   }

    fun getClientList(userId: String): Flow<NetworkResult<ClientResponse>> {
        return flow {
            emit(safeApiCall { remoteDataSource.getClientList(userId) })
        }.flowOn(Dispatchers.IO)
    }

  /*  fun saveBooking(
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
    ): Flow<NetworkResult<SaveBookingResponse>> {
        return flow {
            emit(safeApiCall {
                remoteDataSource.saveBooking(
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
                )
            })
        }.flowOn(Dispatchers.IO)
    }*/
  fun saveBooking(
    bookingRequest: BookingRequest
  ): Flow<NetworkResult<SaveBookingResponse>> {
      return flow {
          emit(safeApiCall {
              remoteDataSource.saveBooking(
                  bookingRequest
              )
          })
      }.flowOn(Dispatchers.IO)
  }

   /* fun deliverBooking(
        userId: String,
        podNo: String,
        receiverName: String,
        receiverMobileNo: String,
        receiverAddress: String,
    ): Flow<NetworkResult<SaveBookingResponse>> {
        return flow {
            emit(safeApiCall {
                remoteDataSource.deliverBooking(
                    userId, podNo,
                    receiverName,
                    receiverMobileNo,
                    receiverAddress
                )
            })
        }.flowOn(Dispatchers.IO)
    }*/
   fun deliverBooking(
      deliveryRequest: DeliveryRequest
   ): Flow<NetworkResult<SaveBookingResponse>> {
       return flow {
           emit(safeApiCall {
               remoteDataSource.deliverBooking(
                   deliveryRequest
               )
           })
       }.flowOn(Dispatchers.IO)
   }

    fun uploadBookingFile(
        bookingId: RequestBody,
        userId: RequestBody,
        file: MultipartBody.Part
    ): Flow<NetworkResult<UploadFileResponse>> {
        return flow {
            emit(safeApiCall {
                remoteDataSource.uploadBookingFile(
                    bookingId, userId,
                    file
                )
            })
        }.flowOn(Dispatchers.IO)
    }
}