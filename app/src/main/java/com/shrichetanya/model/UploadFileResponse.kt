package com.shrichetanya.model

data class UploadFileResponse(
    val result: ResultData,
    val path: String
)

data class ResultData(
    val code: Int,
    val msg: String?
)
