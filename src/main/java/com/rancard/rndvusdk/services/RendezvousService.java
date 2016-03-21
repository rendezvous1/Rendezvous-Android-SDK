package com.rancard.rndvusdk.services;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.rancard.rndvusdk.RendezvousResponse;
import com.rancard.rndvusdk.interfaces.RendezvousRequestListener;
import com.rancard.rndvusdk.utils.Constants;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.FormBody.Builder;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**
 * Created by: Robert Wilson.
 * Date: Feb 02, 2016
 * Time: 1:25 PM
 * Package: com.rancard.rndvusdk.services
 * Project: Rendezvous SDK
 */
public class RendezvousService
{
    private final OkHttpClient mClient;
    private static RendezvousService mService;
    private long mStoreId;
    private String mClientId;
    public static final String TAG = RendezvousService.class.getSimpleName();
    public static final long CONNECTION_TIMEOUT = 20000;
    public static final long WRITE_TIMEOUT = 20000;
    public static final long READ_TIMEOUT = 20000;

    private RendezvousService(long storeId, String clientId)
    {
        mStoreId = storeId;
        mClientId = clientId;
        mClient = new OkHttpClient.Builder().connectTimeout(CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.MILLISECONDS)
                .build();
    }

    public OkHttpClient getClient()
    {
        return mClient;
    }

    public void getAsync(Uri url, final RendezvousRequestListener requestListener)
    {

        if (requestListener == null) {
            return;
        }

        requestListener.onBefore();

        Log.d(TAG, getBuiltURL(url));

        try {

            Request request = new Request.Builder()
                    .url(getBuiltURL(url))
                    .build();


            mClient.newCall(request).enqueue(new Callback()
            {
                @Override
                public void onFailure(Call call, IOException e)
                {
                    requestListener.onError(e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException
                {

                    try {
                        RendezvousResponse r = RendezvousResponse.transform(response);
                        requestListener.onResponse(r);
                    }
                    catch (Exception e) {
                        requestListener.onError(e);
                        e.printStackTrace();
                    }

                }
            });
        }
        catch (Exception e) {
            requestListener.onError(e);
            e.printStackTrace();
        }
    }

    @NonNull
    private String getBuiltURL(Uri url)
    {
        StringBuilder urlBuilder = new StringBuilder(url.toString());

        if ( urlBuilder.toString().contains("?") ) {
            urlBuilder.append("&");
            urlBuilder.append(Constants.CLIENT_ID);
            urlBuilder.append("=");
            urlBuilder.append(mClientId);
            urlBuilder.append("&");
            urlBuilder.append(Constants.CLIENT_iD);
            urlBuilder.append("=");
            urlBuilder.append(mClientId);
            urlBuilder.append("&");
            urlBuilder.append(Constants.STORE_ID);
            urlBuilder.append("=");
            urlBuilder.append(mStoreId);
        }
        else {
            urlBuilder.append("?");
            urlBuilder.append(Constants.CLIENT_ID);
            urlBuilder.append("=");
            urlBuilder.append(mClientId);
            urlBuilder.append("&");
            urlBuilder.append(Constants.STORE_ID);
            urlBuilder.append("=");
            urlBuilder.append(mStoreId);
        }
        return urlBuilder.toString();
    }

    public void get(Uri url, final RendezvousRequestListener requestListener)
    {
        if (requestListener == null) {
            return;
        }

        requestListener.onBefore();

        Log.d(TAG, getBuiltURL(url));

        try {
            Request request = new Request.Builder()
                    .url(getBuiltURL(url))
                    .build();

            Response response = mClient.newCall(request).execute();

            if (response != null) {
                requestListener.onResponse(RendezvousResponse.transform(response));
            }
        }
        catch (Exception e) {
            requestListener.onError(e);
            e.printStackTrace();
        }
    }


    public void postNoURLParams(Uri url, Map<String, String> payload, final RendezvousRequestListener requestListener)
    {
        if (requestListener == null) {
            return;
        }

        requestListener.onBefore();

        Log.d(TAG, url.toString());

        try {
            RequestBody formBody = toRequestBody(payload);
            Request request = new Request.Builder()
                    .url(url.toString())
                    .post(formBody)
                    .build();

            Log.d(TAG, request.body().contentType().subtype());
            Log.d(TAG, request.url().toString());
            Response response = mClient.newCall(request).execute();
            requestListener.onResponse(RendezvousResponse.transform(response));
        }
        catch (Exception e) {
            requestListener.onError(e);
            e.printStackTrace();
        }
    }


    public void post(Uri url, Map<String, String> payload, final RendezvousRequestListener requestListener)
    {
        if (requestListener == null) {
            return;
        }

        requestListener.onBefore();

        Log.d(TAG, url.toString());


        try {
            RequestBody formBody = toRequestBody(payload);
            Request request = new Request.Builder()
                    .url(url.toString())
                    .post(formBody)
                    .build();

            Log.d(TAG, bodyToString(request));
            Log.d(TAG, request.url().url().toString());

            Response response = mClient.newCall(request).execute();
            requestListener.onResponse(RendezvousResponse.transform(response));
        }
        catch (Exception e) {
            requestListener.onError(e);
            e.printStackTrace();
        }
    }


    public void put(Uri url, Map<String, String> payload, final RendezvousRequestListener requestListener)
    {
        if (requestListener == null) {
            return;
        }

        requestListener.onBefore();

        Log.d(TAG, getBuiltURL(url));

        try {

            RequestBody formBody = toRequestBody(payload);
            Request request = new Request.Builder()
                    .url(url.toString())
                    .put(formBody)
                    .build();

            Response response = mClient.newCall(request).execute();
            requestListener.onResponse(RendezvousResponse.transform(response));
        }
        catch (Exception e) {
            requestListener.onError(e);
            e.printStackTrace();
        }
    }

    public void post(Uri url, String jsonPayload, Map<String, String> payload, final RendezvousRequestListener requestListener)
    {
        if ( requestListener == null ) {
            return;
        }

        requestListener.onBefore();

        //Log.d(TAG, getBuiltURL(url));

        try {


            MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
            Request request = new Request.Builder()
                    .url(url.toString())
                    .post(RequestBody.create(mediaType, jsonPayload))
                    .build();
            mClient.newBuilder().readTimeout(0, TimeUnit.SECONDS);
            Response response = mClient.newCall(request).execute();

            if (response != null) {
                requestListener.onResponse(RendezvousResponse.transform(response));
            }
        }
        catch (Exception e) {
            requestListener.onError(e);
            e.printStackTrace();
        }
    }

    public void put(Uri url, String jsonPayload, final RendezvousRequestListener requestListener)
    {
        if ( requestListener == null ) {
            return;
        }

        requestListener.onBefore();

        Log.d(TAG, getBuiltURL(url));

        try {

            JSONObject jsonObject = new JSONObject(jsonPayload);
            jsonObject.put(Constants.CLIENT_ID, mClient);
            jsonObject.put(Constants.STORE_ID, mStoreId);

            MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
            Request request = new Request.Builder()
                    .url(url.toString())
                    .put(RequestBody.create(mediaType, jsonObject.toString()))
                    .build();
            Response response = mClient.newCall(request).execute();

            if (response != null) {
                requestListener.onResponse(RendezvousResponse.transform(response));
            }
        }
        catch (Exception e) {
            requestListener.onError(e);
            e.printStackTrace();
        }
    }

    private RequestBody toRequestBody(Map<String, String> map)
    {
        Builder builder = new FormBody.Builder();
        Set<String> paramsKeys = map.keySet();
        for (String key : paramsKeys) {
            builder.add(key, map.get(key));
        }
        return builder.build();
    }

    private RequestBody toRequestParams(Map<String, String> map)
    {
        Builder builder = new FormBody.Builder();
        Set<String> paramsKeys = map.keySet();
        for (String key : paramsKeys) {
            builder.add(key, map.get(key));
        }
        return builder.build();
    }

    public static RendezvousService getInstance(long storeId, String clientId)
    {
        if (mService == null) {
            mService = new RendezvousService(storeId, clientId);
        }
        return mService;
    }


    private static String bodyToString(final Request request){

        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            copy.body().writeTo(buffer);
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }
}
