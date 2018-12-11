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

    // 这是请求数据的返回类型，包含常见的（Bean，List等）
    Type type;


    /**
     * 通过反射得到想要的返回类型
     * @param subclass
     * @return Type
     */
    private static Type getSupperclassTypeParameter(Class<?> subclass)
    {
        Type superclass =  subclass.getGenericSuperclass();
        if (superclass instanceof Class)
        {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterizedType = (ParameterizedType) superclass;
        return $Gson$Types.canonicalize(parameterizedType.getActualTypeArguments()[0]);
    }

    protected BaseCallBack(){
        type = getSupperclassTypeParameter(getClass());
    }

    // 请求前，一般用来显示loading
    public abstract  void onRequestBefore(Request request);

    // 请求失败时回调
    public abstract void onFailure(Call call, IOException e);

    public abstract void onSuccess(Response response, T t);

    // 请求http成功但出现错误回调
    public abstract void onError(Response response, int Code,Exception e);

    // 服务器返回回调
    public abstract void onResponse(Response response);

}