package cheetah.core.utils;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.LruCache;


import java.util.Map;
import java.util.Set;

/**
 * Description: A helper for SharedPreferences
 * <P>init, in Application.onCreate
 * SharedPrefHelper.setDefaultFileName("your_app_sp")
 * SharedPrefHelper.setMaxHelpersCount(count)
 * <p>
 * <p>put value:
 * // SharedPrefHelper.from(context, "another_sp")
 * SharedPrefHelper.from(context)
 * .put("string", "test string")
 * .put("int", 1)
 * .put("boolean", true)
 * .put("float", 0.1f)
 * .put("long", 100000L)
 * .putEnd("6", "test another string");
 * OR
 * SharedPrefHelper.from(context)
 * .put("string", "test string")
 * .put("int", 1)
 * .put("boolean", true)
 * .put("float", 0.1f)
 * .put("long", 100000L)
 * .end()
 * <p>get value:
 * SharedPrefHelper.from(context).getString(key, defaultValue)
 * ...
 * <p>
 * author:Joey
 * date:2018/7/26
 */

public class SharedPrefHelper {
    private static LruCache<String, SharedPrefHelper> mHelpers;
    private final SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private static String sDefaultFileName = "default_app_sp";
    private static int sMaxHelpersCount = 3;

    /**
     * set a default file name for your application
     * invoke this method in Application.onCreate()
     *
     * @param fileName default file name
     */
    public static void setDefaultFileName(String fileName) {
        sDefaultFileName = fileName;
    }

    public static void setMaxHelpersCount(int count) {
        if (count > 0) {
            sMaxHelpersCount = count;
        }
    }

    /**
     * get SharedPreferencesHelper with default file name
     *
     * @param context suggest Application context
     * @return SharedPreferencesHelper
     */
    public static SharedPrefHelper from(Context context) {
        return from(context, sDefaultFileName);
    }

    /**
     * get SharedPreferencesHelper with specified file name
     *
     * @param context  suggest Application context
     * @param fileName file name
     * @return SharedPreferencesHelper
     */
    public static SharedPrefHelper from(Context context, String fileName) {
        if (context == null)
            throw new NullPointerException("null context!!");
        if (mHelpers == null) {
            mHelpers = new LruCache<>(sMaxHelpersCount);
        }
        SharedPrefHelper helper = mHelpers.get(fileName);
        if (helper == null) {
            helper = new SharedPrefHelper(context, fileName);
            mHelpers.put(fileName, helper);
        }
        return helper;
    }

    private SharedPrefHelper(Context context, String dataName) {
        mSharedPreferences = context.getSharedPreferences(dataName, Context.MODE_PRIVATE);
    }

    /**
     * put K-V data into an editor
     * invoke end() or putEnd(k,v) as last method in the call chain in order to  apply data
     *
     * @return SharedPreferencesHelper
     */
    @SuppressWarnings("unchecked")
    public SharedPrefHelper put(String key, Object object) {
        ensureEditor();
        if (object instanceof String) {
            mEditor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            mEditor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            mEditor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            mEditor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            mEditor.putLong(key, (Long) object);
        } else if (object instanceof Set) {
            mEditor.putStringSet(key, (Set<String>) object);
        } else {
            // other type will be handle as String
            mEditor.putString(key, String.valueOf(object));
        }
        return this;
    }

    /**
     * put k-v data into an editor, and apply.
     */
    public void apply(String key, Object object) {
        put(key, object);
        apply();
    }

    public SharedPrefHelper remove(String key) {
        ensureEditor();
        mEditor.remove(key);
        return this;
    }

    public SharedPrefHelper clear() {
        ensureEditor();
        mEditor.clear();
        apply();
        return this;
    }

    public boolean contains(String key) {
        return mSharedPreferences.contains(key);
    }

    public SharedPreferences getSharePreferences() {
        return mSharedPreferences;
    }

    /**
     * apply editor.
     */
    public void apply() {
        if (mEditor != null) {
            mEditor.apply();
        }
    }

    private void ensureEditor() {
        if (mEditor == null) {
            mEditor = mSharedPreferences.edit();
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T get(String key, Type type, Object defaultValue) {
        return (T) getValue(key, type, defaultValue);
    }

    public String getString(String key, String defaultValue) {
        return get(key, Type.STRING, defaultValue);
    }

    public int getInt(String key, int defaultValue) {
        return get(key, Type.INTEGER, defaultValue);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return get(key, Type.BOOLEAN, defaultValue);
    }

    public float getFloat(String key, float defaultValue) {
        return get(key, Type.FLOAT, defaultValue);
    }

    public long getLong(String key, long defaultValue) {
        return get(key, Type.LONG, defaultValue);
    }

    public Set<String> getStringSet(String key, Set<String> defaultValue) {
        return get(key, Type.STRING_SET, defaultValue);
    }

    public Map<String, ?> getAll() {
        return get("", Type.ALL, null);
    }

    @SuppressWarnings("unchecked")
    private Object getValue(String key, Type type, Object defaultValue) {
        switch (type) {
            case STRING:
                return mSharedPreferences.getString(key, (String) defaultValue);
            case INTEGER:
                return mSharedPreferences.getInt(key, (Integer) defaultValue);
            case BOOLEAN:
                return mSharedPreferences.getBoolean(key, (Boolean) defaultValue);
            case FLOAT:
                return mSharedPreferences.getFloat(key, (Float) defaultValue);
            case LONG:
                return mSharedPreferences.getLong(key, (Long) defaultValue);
            case STRING_SET:
                return mSharedPreferences.getStringSet(key, (Set<String>) defaultValue);
            case ALL:
                return mSharedPreferences.getAll();
            default:
                return defaultValue;
        }
    }

    enum Type {
        STRING,
        INTEGER,
        BOOLEAN,
        FLOAT,
        LONG,
        STRING_SET,
        ALL,
    }
}

