package com.zw.composetemplate.core

interface MessageListenerInterface {
    // creating an interface method for messages received.
    fun messageReceived(message: String?)
}