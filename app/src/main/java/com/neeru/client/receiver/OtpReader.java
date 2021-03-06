package com.neeru.client.receiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.text.TextUtils;

import com.neeru.client.callbacks.OTPListener;

import java.util.List;


/**
 * Created by brajendra on 29/07/17.
 * BroadcastReceiver OtpReader for receiving and processing the SMS messages.
 */
public class OtpReader extends BroadcastReceiver {

    /**
     * Constant TAG for logging key.
     */
    private final String TAG = "OtpReader";

    /**
     * The bound OTP Listener that will be trigerred on receiving message.
     */
    private OTPListener otpListener;

    /**
     * The Sender number string.
     */
    private List<String> receiverString;

    /**
     * Binds the sender string and listener for callback.
     *
     * @param listener
     * @param sender
     */


    public void bind(OTPListener listener, List<String> sender) {
        otpListener = listener;
        receiverString = sender;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final Bundle bundle = intent.getExtras();
        if (bundle != null) {

            final Object[] pdusArr = (Object[]) bundle.get("pdus");

            for (int i = 0; i < pdusArr.length; i++) {

                SmsMessage currentMessage;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    String format = bundle.getString("format");
                    currentMessage = SmsMessage.createFromPdu((byte[]) pdusArr[i], format);
                } else {
                    currentMessage = SmsMessage.createFromPdu((byte[]) pdusArr[i]);
                }

                String senderNum = currentMessage.getDisplayOriginatingAddress();
                String message = currentMessage.getDisplayMessageBody();
                Log.i(TAG, "senderNum: " + senderNum + " message: " + message);


                if(receiverString == null){
                    return;
                }



                for (String str : receiverString) {
                    if (!TextUtils.isEmpty(str) && senderNum.contains(str)) { //If message received is from required number.
                        //If bound a listener interface, callback the overriden method.
                        if (otpListener != null) {
                            otpListener.otpReceived(message);
                        }

                        break;
                    }
                }
            }


        }
    }
}