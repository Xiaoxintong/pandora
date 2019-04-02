package tech.linjiang.pandora;

import android.app.Activity;
import android.app.Application;
import android.support.v4.content.FileProvider;

import tech.linjiang.pandora.crash.CrashHandler;
import tech.linjiang.pandora.database.Databases;
import tech.linjiang.pandora.function.IFunc;
import tech.linjiang.pandora.history.HistoryRecorder;
import tech.linjiang.pandora.inspector.attribute.AttrFactory;
import tech.linjiang.pandora.network.OkHttpInterceptor;
import tech.linjiang.pandora.preference.SharedPref;
import tech.linjiang.pandora.util.SensorDetector;
import tech.linjiang.pandora.util.Utils;

/**
 * Created by linjiang on 29/05/2018.
 */
public final class Pandora extends FileProvider implements SensorDetector.Callback {

    private static Pandora INSTANCE;

    public Pandora() {
        if (INSTANCE != null) {
            throw new RuntimeException();
        }
    }

    @Override
    public boolean onCreate() {
        return super.onCreate();
    }

    public static Pandora init(Application app) {
        INSTANCE = this;
        Utils.init(app);
        this.initOther(app);
        return INSTANCE;
    }

    private void initOther(Application app) {
        funcController = new FuncController(app);
        sensorDetector = new SensorDetector(this);
        interceptor = new OkHttpInterceptor();
        databases = new Databases();
        sharedPref = new SharedPref();
        attrFactory = new AttrFactory();
        crashHandler = new CrashHandler();
        historyRecorder = new HistoryRecorder(app);
    }

    public static Pandora get() {
        if (INSTANCE == null) {
            throw new RuntimeException("need to call Pandora#init in Application#onCreate firstly.");
        }
        return INSTANCE;
    }

    private OkHttpInterceptor interceptor;
    private Databases databases;
    private SharedPref sharedPref;
    private AttrFactory attrFactory;
    private CrashHandler crashHandler;
    private HistoryRecorder historyRecorder;
    private FuncController funcController;
    private SensorDetector sensorDetector;

    public OkHttpInterceptor getInterceptor() {
        return interceptor;
    }

    public Databases getDatabases() {
        return databases;
    }

    public SharedPref getSharedPref() {
        return sharedPref;
    }

    public AttrFactory getAttrFactory() {
        return attrFactory;
    }

    /**
     * @hide
     */
    public Activity getTopActivity() {
        return historyRecorder.getTopActivity();
    }

    /**
     * Add a custom entry to the panel.
     * also see @{@link tech.linjiang.pandora.function.IFunc}
     *
     * @param func
     */
    public void addFunction(IFunc func) {
        funcController.addFunc(func);
    }

    /**
     * Open the panel.
     */
    public void open() {
        funcController.open();
    }

    /**
     * Close the panel.
     */
    public void close() {
        funcController.close();
    }

    /**
     * Disable the Shake feature.
     */
    public void disableShakeSwitch() {
        sensorDetector.unRegister();
    }

    @Override
    public void shakeValid() {
        open();
    }
}
