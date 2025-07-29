package com.shrichetanya.model

import java.io.Serializable

data class SaveBookingResponse(
    val result: SaveResultInfo,
    val bookingId: Int
):Serializable

data class SaveResultInfo(
    val code: Int,
    val msg: String?
):Serializable