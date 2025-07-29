package com.shrichetanya.model

data class BookingRequest(
    val userId: String? = null,
    val clinetId: String? = null,
    val podNo: String? = null,
    val isDocument: Boolean = false,
    val mode: String? = null,
    val itemName: String? = null,
    val itemQuantity: String? = null,
    val itemValue: String? = null,
    val sourceAddress: String? = null,
    val sourcePinCode: String? = null,
    val destinationAddress: String? = null,
    val destinationPinCode: String? = null,
    val bookingName: String? = null,
    val bookingMobileNo: String? = null
)
