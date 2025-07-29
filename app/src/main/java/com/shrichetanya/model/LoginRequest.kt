package com.shrichetanya.model
import java.io.Serializable
data class LoginRequest(
    val userName: String,
    val password: String
):Serializable
