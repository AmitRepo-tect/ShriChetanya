package com.shrichetanya.model

import java.io.Serializable

data class LoginResponse(
    val result: ResultInfo,
    val name: String,
    val userId: Int,
    val emailId: String,
    val authKey: String
) : Serializable

data class ResultInfo(
    val code: Int,
    val msg: String
) : Serializable