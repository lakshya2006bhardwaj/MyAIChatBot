package com.lakshya.myaichatbot

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AiChatViewModel : ViewModel() {
    val messages = mutableStateListOf<Message>(
        Message(1, "Hello! I am your AI Assistant. How can I help you today?", false)
    )

    private val _isTyping = MutableStateFlow(false)
    val isTyping: StateFlow<Boolean> = _isTyping

    fun sendMessage(text: String) {
        if (text.isBlank()) return

        // Add user message
        messages.add(Message(System.currentTimeMillis(), text, true))

        // Trigger simulated AI Response
        viewModelScope.launch {
            _isTyping.value = true
            delay(1500)
            val aiResponse = "This is an automated mock response to: \"$text\". Integration is working flawlessly!"
            messages.add(Message(System.currentTimeMillis(), aiResponse, false))
            _isTyping.value = false
        }
    }
}