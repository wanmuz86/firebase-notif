package com.itrainasia.firebasenotification;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

public class FirebaseConfig {
    FirebaseRemoteConfig mFirebaseRemoteConfig;
    FirebaseRemoteConfigSettings configSettings;
    long cacheExpiration = 43200;
    String activeVersion = "";

    //Initialize the config
    public FirebaseConfig() {
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();
        mFirebaseRemoteConfig.setConfigSettings(configSettings);
        mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults);
    }

    public String fetchVersion(Context context){
        String currentVersion = mFirebaseRemoteConfig.getString("resource_version");
        Log.d("debug before",currentVersion);
        mFirebaseRemoteConfig.fetch(getCacheExpiration())
                .addOnCompleteListener((Activity) context, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
// If is successful, activated fetched
                        if (task.isSuccessful()) {

                            mFirebaseRemoteConfig.activateFetched();
                        } else {
                            Log.d("","");
                        }
                        activeVersion = mFirebaseRemoteConfig.getString("resource_version");
                        Log.d("debug after",activeVersion);
                    }
                });
        return activeVersion;
    }

    public long getCacheExpiration() {
// If is developer mode, cache expiration set to 0, in order to test
        if (mFirebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
            cacheExpiration = 0;
        }
        return cacheExpiration;
    }
}
