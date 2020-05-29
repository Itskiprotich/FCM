package com.imeja.fcmpush;

import android.app.Application;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.imeja.fcmpush.model.FirebaseToken;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class BaseApp extends Application {

    private static final int SCHEMA_VERSION = 0;
    private Realm realmInstance;
    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);

        RealmConfiguration config = new RealmConfiguration.Builder()
                .schemaVersion(SCHEMA_VERSION)
                .deleteRealmIfMigrationNeeded()
                .build();

        FirebaseToken token = new FirebaseToken(FirebaseInstanceId.getInstance().getToken());
        FirebaseMessaging.getInstance().subscribeToTopic("imeja");
        FirebaseMessaging.getInstance().subscribeToTopic("developers");
        Realm.setDefaultConfiguration(config);

        realmInstance = Realm.getDefaultInstance();
        realmInstance.beginTransaction();
        realmInstance.delete(FirebaseToken.class);
        realmInstance.copyToRealm(token);
        realmInstance.commitTransaction();
    }
}
