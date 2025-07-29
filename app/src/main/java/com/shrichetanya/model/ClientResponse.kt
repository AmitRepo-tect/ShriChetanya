package com.shrichetanya.model

data class ClientResponse(
    val result: Result,
    val client: List<Client>
)

data class Result(
    val code: Int,
    val msg: String,
    val res: Any? // Use appropriate type if not always null
)

data class Client(
    val id: Int,
    val name: String
)
