package pers.sukai.cnshop.http;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * create by sukaidev on 2018/12/6.
 * OkHttp简单封装.
 */
public class OkHttpHelper {

    private Gson gson;
    private Handler handler;
    private static OkHttpClient okHttpClient;
    private static OkHttpHelper helper;

    static {
        helper = new OkHttpHelper();
    }

    enum HttpMethodType {
        GET,
        POST
    }

    public static OkHttpHelper getInstance() {
        return helper;
    }

    private OkHttpHelper() {
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();

        gson = new Gson();
        handler = new Handler(Looper.getMainLooper());
    }

    public void get(String url, BaseCallBack callback) {
        Request request = buildeRequest(url, HttpMethodType.GET, null);
        doRequest(request, callback);
    }

    public void post(String url, Map<String, String> params, BaseCallBack callback) {
        Request request = buildeRequest(url, HttpMethodType.POST, params);
        doRequest(request, callback);
    }

    public void doRequest(Request request, final BaseCallBack callback) {

        callback.onRequestBefore(request);

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                callback.onResponse(response);

                if (response.isSuccessful()) {

                    String resultStr = response.body().string();
                    if (callback.type == String.class) {
                        callbackSuccess(callback, response, resultStr);
                    } else {
                        try {
                            Object object = gson.fromJson(resultStr, callback.type);
                            callbackSuccess(callback, response, object);
                        } catch (JsonIOException e) {
                            callbackError(callback, response, e);
                        }
                    }
                } else {
                    callback.onError(response, response.code(), null);
                }
            }
        });
    }

    private Request buildeRequest(String url, HttpMethodType methodType, Map<String, String> params) {

        Request.Builder builder = new Request.Builder();

        builder.url(url);

        if (methodType == HttpMethodType.GET) {
            builder.get();
        } else if (methodType == HttpMethodType.POST) {
            RequestBody body = builderFormData(params);
            builder.post(body);
        }

        return builder.build();
    }

    private RequestBody builderFormData(Map<String, String> params) {
        FormBody.Builder builder = new FormBody.Builder();
        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.add(entry.getKey(), entry.getValue());
            }
        }
        return builder.build();
    }

    private void callbackSuccess(final BaseCallBack callback, final Response response, final Object obeject) {

        handler.post(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess(response, obeject);
            }
        });
    }

    private void callbackError(final BaseCallBack callback, final Response response, final Exception e) {

        handler.post(new Runnable() {
            @Override
            public void run() {
                callback.onError(response, response.code(), e);
            }
        });
    }

}

