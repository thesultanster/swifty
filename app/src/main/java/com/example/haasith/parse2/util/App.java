package com.example.haasith.parse2.util;

import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.moxtra.sdk.MXAccountManager;
import com.moxtra.sdk.MXSDKException;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;


/**
 * Created by sultankhan on 10/14/15.
 */
public class App extends MultiDexApplication {

    public ParseInstallation installation;


    private static final String TAG = "MoxieChatApplication";

    @Override
    public void onCreate()  {
        super.onCreate();



        // Enable Local Datastore
        Parse.enableLocalDatastore(this);

        try {
            ParseInstallation.getCurrentInstallation().save();
        } catch (ParseException e) {
            e.printStackTrace();
        }


        try{
            MXAccountManager.createInstance(this, "ovKEfVA_SQQ", "pODySaTS3Xs", true);
        } catch (MXSDKException.InvalidParameter invalidParameter) {
            Log.e(TAG, "Error when creating MXAccountManager instance.", invalidParameter);
        }


    }
}
