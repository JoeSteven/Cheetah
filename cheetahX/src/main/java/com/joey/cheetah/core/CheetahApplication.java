package com.joey.cheetah.core;

import android.app.Application;

import com.joey.cheetah.core.initialization.InitializationManager;

/**
 * description - 应用
 * author - Joe.
 * create on 16/7/13.
 * change
 * change on .
 */
public abstract class CheetahApplication extends Application {
    private static CheetahApplication mApp;

    @Override
    public void onCreate() {
        mApp = this;
        InitializationManager manager = createInitializationManager();
        manager.init(isMainProcess());
        manager.beforeApplicationCreate();
        super.onCreate();
        manager.afterApplicationCreate();

    }

    protected abstract InitializationManager createInitializationManager();


    public static CheetahApplication getInstance(){
        return mApp;
    }

    protected abstract boolean isMainProcess();

//    protected AppModule getApplicationModule() {
//        return new AppModule(this);
//    }

    //    private AppComponent component;

//    public static AppComponent getAppComponent(Context context) {
//        CheetahApplication app = (CheetahApplication) context.getApplicationContext();
//        if (app.component == null) {
//            app.component = DaggerAppComponent.builder()
//                    .appModule(app.getApplicationModule())
//                    .build();
//        }
//        return app.component;
//    }
//
//    public static void clearAppComponent(Context context) {
//        CheetahApplication app = (CheetahApplication) context.getApplicationContext();
//        app.component = null;
//    }
}
