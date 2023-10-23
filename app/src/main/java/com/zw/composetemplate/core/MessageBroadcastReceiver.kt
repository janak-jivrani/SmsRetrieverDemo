package com.zw.composetemplate.core

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsMessage

class MessageBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // getting bundle data on below line from intent.
        val data = intent.extras
        // creating an object on below line.
        val pdus = data!!["pdus"] as Array<*>?
        // running for loop to read the sms on below line.
        for (i in pdus!!.indices) {
            // getting sms message on below line.
            val smsMessage = SmsMessage.createFromPdu(
                pdus[i] as ByteArray
            )
            // extracting the sms from sms message and setting it to string on below line.
            val message = ("Sender : " + smsMessage.displayOriginatingAddress
                    + "Message: " + smsMessage.messageBody)
            // adding the message to listener on below line.
            mListener?.messageReceived(message)
        }
    }

    companion object {
        // creating a variable for a message listener interface on below line.
        private var mListener: MessageListenerInterface? = null

        // on below line we are binding the listener.
        fun bindListener(listener: MessageListenerInterface?) {
            mListener = listener
        }
    }
}