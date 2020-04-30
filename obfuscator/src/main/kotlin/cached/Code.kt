package cached

fun classify(method: String) = "class foo{$method}"

val CACHED_APPLICATION = "" +
        "package com.ono.test_app;\n" +
        "\n" +
        "import android.app.Application;\n" +
        "import android.content.Context;\n" +
        "import android.content.SharedPreferences;\n" +
        "import androidx.preference.PreferenceManager;\n" +
        "import org.json.JSONObject;\n" +
        "import java.io.InputStream;\n" +
        "import java.util.Iterator;\n" +
        "\n" +
        "public class MyApplication extends Application {\n" +
        "\n" +
        "    static SecureSharedPreferences pref = null;\n" +
        "\n" +
        "    @Override\n" +
        "    public void onCreate() {\n" +
        "        encryption();\n" +
        "        super.onCreate();\n" +
        "    }\n" +
        "\n" +
        "    private void encryption() {\n" +
        "        SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(this);\n" +
        "        pref = new SecureSharedPreferences(p);\n" +
        "        loadJSONFromAsset(this);\n" +
        "        try {\n" +
        "            JSONObject jsonObject = new JSONObject(loadJSONFromAsset(this));\n" +
        "            Iterator<String> keys = jsonObject.keys();\n" +
        "            while (keys.hasNext()) {\n" +
        "                String key = keys.next();\n" +
        "                pref.put(key, jsonObject.get(key).toString());\n" +
        "            }\n" +
        "        } catch (Exception e) {\n" +
        "            e.printStackTrace();\n" +
        "        }\n" +
        "    }\n" +
        "\n" +
        "    public String loadJSONFromAsset(Context context) {\n" +
        "        String json = null;\n" +
        "        try {\n" +
        "            InputStream is = context.getAssets().open(\"encryption.json\");\n" +
        "            int size = is.available();\n" +
        "            byte[] buffer = new byte[size];\n" +
        "            is.read(buffer);\n" +
        "            is.close();\n" +
        "            json = new String(buffer, \"UTF-8\");\n" +
        "        } catch (Exception e) {\n" +
        "            e.printStackTrace();\n" +
        "        }\n" +
        "        return json;\n" +
        "    }\n" +
        "}\n"

val FUNCTION_ENCRPTION = " private void encryption() {\n" +
        "        SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(this);\n" +
        "        pref = new SecureSharedPreferences(p);\n" +
        "\n" +
        "        loadJSONFromAsset(this);\n" +
        "        try {\n" +
        "            JSONObject jsonObject = new JSONObject(loadJSONFromAsset(this));\n" +
        "            Iterator<String> keys = jsonObject.keys();\n" +
        "            while (keys.hasNext()) {\n" +
        "                String key = keys.next();\n" +
        "                pref.put(key, jsonObject.get(key).toString());\n" +
        "            }\n" +
        "        } catch (Exception e) {\n" +
        "            e.printStackTrace();\n" +
        "        }\n" +
        "    }"

val FUNCTION_LOAD_JSON = "    public String loadJSONFromAsset(Context context) {\n" +
        "        String json = null;\n" +
        "        try {\n" +
        "            InputStream is = context.getAssets().open(\"encryption.json\");\n" +
        "            int size = is.available();\n" +
        "            byte[] buffer = new byte[size];\n" +
        "            is.read(buffer);\n" +
        "            is.close();\n" +
        "            json = new String(buffer, \"UTF-8\");\n" +
        "        } catch (Exception e) {\n" +
        "            e.printStackTrace();\n" +
        "        }\n" +
        "        return json;\n" +
        "    }"

val CLASS_APPLICATION = "import android.app.Application;\n" +
        "\n" +
        "public class MyApplication extends Application {\n" +
        "    static SecureSharedPreferences pref = null;\n" +
        "}\n"

