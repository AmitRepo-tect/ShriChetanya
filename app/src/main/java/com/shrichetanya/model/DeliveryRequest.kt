package com.shrichetanya.model

data class DeliveryRequest(
    val userId: String,
    val podNo: String,
    val receiverName: String,
    val receiverMobileNo: String,
    val receiverAddress: String
)
