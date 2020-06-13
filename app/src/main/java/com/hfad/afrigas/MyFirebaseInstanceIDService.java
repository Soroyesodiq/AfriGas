package com.hfad.afrigas;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "mFirebaseIIDService";
    private static final String SUBSCRIBE_TO = "userABC";
    //private String deviceToken = "null";

    /*
        public MyFirebaseInstanceIDService() {
        }

        @Override
        public IBinder onBind(Intent intent) {
            // TODO: Return the communication channel to the service.
            throw new UnsupportedOperationException("Not yet implemented");
        }*/

    @Override
    public void onTokenRefresh() {
        /*
          This method is invoked whenever the token refreshes
          OPTIONAL: If you want to send messages to this application instance
          or manage this apps subscriptions on the server side,
          you can send this token to your server.
        */
        String token = FirebaseInstanceId.getInstance().getToken();

        // Once the token is generated, subscribe to topic with the userId
        //FirebaseMessaging.getInstance().subscribeToTopic(SUBSCRIBE_TO);
        //FirebaseMessaging.getInstance().subscribeToTopic(token);
        //Log.i(TAG, "onTokenRefresh completed with token: " + token);
        //Toast.makeText(MyFirebaseInstanceIDService.this, token, Toast.LENGTH_SHORT).show();
    }

}
