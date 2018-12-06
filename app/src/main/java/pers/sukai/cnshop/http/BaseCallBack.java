package pers.sukai.cnshop.http;

import com.google.gson.internal.$Gson$Types;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * create by sukaidev on 2018/12/6.
 * 自定义CallBack.
 */

public abstract class BaseCallBack<T> {

    Type type;

    static Type getSupperclassTypeParameter(Class<?> subclass)
    {
        Type superclass =  subclass.getGenericSuperclass();
        if (superclass instanceof Class)
        {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterizedType = (ParameterizedType) superclass;
        return $Gson$Types.canonicalize(parameterizedType.getActualTypeArguments()[0]);
    }

    public BaseCallBack(){
        type = getSupperclassTypeParameter(getClass());
    }

    public abstract  void onRequestBefore(Request request);

    public abstract void onFailure(Call call, IOException e);

    public abstract void onSuccess(Response response, T t);

    public abstract void onError(Response response, int Code,Exception e);

    public abstract void onResponse(Response response);

}