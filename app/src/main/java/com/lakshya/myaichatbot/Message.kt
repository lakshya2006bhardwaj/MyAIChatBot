package com.lakshya.myaichatbot

data class Message(
    val id: Long,
    val text: String,
    val isUser: Boolean
)