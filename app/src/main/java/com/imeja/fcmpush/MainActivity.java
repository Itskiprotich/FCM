package com.imeja.fcmpush;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.imeja.fcm.model.Message;
import com.imeja.fcm.model.FCMMessage;
import com.imeja.fcm.serials.FCMHelper;

import java.io.IOException;

import static com.imeja.fcmpush.constants.Constants.FCM_KEY;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getToken();
    }

    private void getToken() {
        try {
            FirebaseInstanceId.getInstance().getInstanceId()
                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                        @Override
                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                            if (!task.isSuccessful()) {
                                Log.e(TAG, "getInstanceId failed", task.getException());
                                return;
                            }

                            // Get new Instance ID token
                            String token = task.getResult().getToken();

                            // Log and toast
                            //   FirebaseToken.setTokenId(token);
                            // uploadToken();
                            Log.e(TAG, "token \n" + token);
                        }
                    });
        } catch (Exception e) {
            Log.e(TAG, "Error:" + e.getMessage());
        }
    }

    public void handleTest(View view) {
        String token = "cm0txV7sQau_SlKKvQ77vt:APA91bH3LFTGL2kBdmYsDsEaANMaXfQoOGtxJ2uspKqRm5nP4CO28N5GelAj_C_roW7Sb1oNeXcGhlD3WAwgI2CfG0-s5-90hJuQqolCEOU8df6cT-fLRhUi9WoSA3lUK3DXvx56jkwf";

        Message chat = new Message();
        chat.title = "Japheth Kiprotich";
        chat.token = token;
        chat.message = "Good Morning";

        final FCMMessage message = new FCMMessage();
        message.setTo(token);
        message.setData(chat);

        FCMHelper.sendMessage(FCM_KEY, message).enqueue(new okhttp3.Callback() {
            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                Log.e("REQUEST TO DRIVER", message.getData().toString());
            }

            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                e.printStackTrace();
            }
        });
    }
}
