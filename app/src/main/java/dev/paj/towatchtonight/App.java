package dev.paj.towatchtonight;

import android.app.Application;
import android.content.res.Resources;

import dev.paj.towatchtonight.util.component.AppContextComponent;
import dev.paj.towatchtonight.util.component.DaggerAppContextComponent;
import dev.paj.towatchtonight.util.component.DaggerNetComponent;
import dev.paj.towatchtonight.util.component.NetComponent;
import dev.paj.towatchtonight.util.module.AppContextModule;
import dev.paj.towatchtonight.util.module.AppModule;
import dev.paj.towatchtonight.util.module.NetModule;


public class App extends Application {

    private NetComponent netComponent;
    private Resources resources;
    private AppContextComponent appContextComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        resources = getResources();
        netComponent = DaggerNetComponent.builder()
                .appModule(new AppModule(this))
                .netModule(new NetModule(resources.getString(R.string.base_api_url), BuildConfig.API_KEY))
                .build();
        appContextComponent = DaggerAppContextComponent.builder()
                .appContextModule(new AppContextModule(this))
                .build();
    }

    public NetComponent getNetComponent() {
        return netComponent;
    }
    public AppContextComponent getAppContextComponent() {
        return appContextComponent;
    }

}
