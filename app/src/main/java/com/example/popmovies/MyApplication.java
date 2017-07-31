package com.example.popmovies;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by fangxiangwang on 7/29/17.
 */
//Reference: https://code.tutsplus.com/tutorials/debugging-android-apps-with-facebooks-stetho--cms-24205
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // Create an InitializerBuilder
        Stetho.InitializerBuilder initializerBuilder =
                Stetho.newInitializerBuilder(this);

// Enable Chrome DevTools
        initializerBuilder.enableWebKitInspector(
                Stetho.defaultInspectorModulesProvider(this)
        );

// Enable command line interface
        initializerBuilder.enableDumpapp(
                Stetho.defaultDumperPluginsProvider(this)
        );

// Use the InitializerBuilder to generate an Initializer
        Stetho.Initializer initializer = initializerBuilder.build();

// Initialize Stetho with the Initializer
        Stetho.initialize(initializer);
    }
}
