package com.rancard.rndvusdk;

import android.content.Context;
import android.net.Uri;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.rancard.rndvusdk.interfaces.RendezvousClient;
import com.rancard.rndvusdk.interfaces.RendezvousRequestListener;
import com.rancard.rndvusdk.services.RendezvousService;
import com.rancard.rndvusdk.utils.Constants;

import org.json.JSONArray;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by: Robert Wilson.
 * Date: Feb 02, 2016
 * Time: 12:24 PM
 * Package: com.rancard.rndvusdk
 * Project: Rendezvous SDK
 */
public class Rendezvous implements RendezvousClient
{
    private String mClientId;
    private long mStoreId;
    private RendezvousEnvironment mRendezvousEnvironment;
    private String mApiKey;
    private String mApiSecret;
    private static Context mContext;
    private static Rendezvous mRendezvous;
    private RendezvousService mService;
    private RendezvousEndpoint mEndpoint;
    public static String msisdn = "";
    public static String email = "";
    public static String STOREID= "";
    public static String CLIENT_ID = "";
    public static String AdId = "";
    public static String friendsTopicTxt = "Friends in Loop";
    public static String friendsSubTopicTxt = "Get opinions of your friends and see their recommendations";
    public static String friendsNoTopicsTxt = "Other Friends";
    public static String friendsNoSubTopicsTxt = "Recommend new topics to these friends";
    public static String peopleTopicTxt = "People You May Know";
    public static String peopleSubTopicTxt = "To see recommendations from your friends, add them";
    public static String friendsBtn = "Get Opinion";
    public static String noFriendsBtn = "Recommend";
    public static String peopleBtn = "Add Friend";
    /*Resource id of drawable to be used for non-social recommendations*/
    public static int nonSocialRecommendationIcon = 0;
    /*Resource id of drawable to be usef for social recomendations. This defaults to the rndvu icon*/
    public static int socialRecommedationIcon = 0;
    /*Title to be shown on notifications received via sms*/
    public static String smsNotificationTitle = "0";
    /*Origin of SMSes to be read by sdk as sms notifications
    This is to prevent sdk from reading every text message received on mobile device
    For example, if smsNotificationTargetOrigin is set to +233200662782, only text messages from
    +233200662782 will be read and treated as sms notifications
    * */
    public static String smsNotificationTargetOrigin ="JoyOnline";

    public static String[] mqttReceiver =  {};
    public static String mqttBrokerUrl = "tcp://notifications.rancardmobility.com:80"; //tcp://iot.eclipse.org:1883";


    static {
        socialRecommedationIcon = R.drawable.ic_rndvu;
    }

    private OkHttpClient.Builder newClient;

    private Rendezvous(Context context, String clientId, long storeId, RendezvousEnvironment rendezvousEnvironment)
    {
        mContext = context;
        mRendezvousEnvironment = rendezvousEnvironment;
        mEndpoint = new RendezvousEndpoint(rendezvousEnvironment);
        mService = RendezvousService.getInstance(storeId, clientId);
    }

    private Rendezvous(Context context, String clientId, long storeId, String apiKey, RendezvousEnvironment rendezvousEnvironment)
    {
        mContext = context;
        mApiKey = apiKey;
        mRendezvousEnvironment = rendezvousEnvironment;
        mEndpoint = new RendezvousEndpoint(rendezvousEnvironment);
        mService = RendezvousService.getInstance(storeId, clientId);
    }


    public static void setTargetVariables(String msisdn, String storeId, String email, String clientId, String mqttBrokerUrl, String[] mqttPayload)
    {
        Rendezvous.msisdn = msisdn;
        Rendezvous.STOREID = storeId;
        Rendezvous.CLIENT_ID = clientId;
        Rendezvous.email = email;
        Rendezvous.mqttBrokerUrl = mqttBrokerUrl;
        Rendezvous.mqttReceiver = mqttPayload;
    }

    public static String getAdId(){
        AdvertisingIdClient.Info adInfo = null;

        try {
            adInfo = AdvertisingIdClient.getAdvertisingIdInfo(mContext);
        } catch (IOException e) {

        } catch (GooglePlayServicesNotAvailableException e) {

        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        }

        Rendezvous.AdId = adInfo.getId();
        return adInfo.getId();
    }

    private Rendezvous(Context context, String clientId, long storeId, String apiKey, String apiSecret, RendezvousEnvironment rendezvousEnvironment)
    {
        mContext = context;
        mApiKey = apiKey;
        mApiSecret = apiSecret;
        mRendezvousEnvironment = rendezvousEnvironment;
        mEndpoint = new RendezvousEndpoint(rendezvousEnvironment);
        mService = RendezvousService.getInstance(storeId, clientId);
    }

    public Rendezvous configure()
    {
        return this;
    }

    public Rendezvous setConnectTimeout(long milliseconds)
    {
        if ( mService == null ) {
            return null;
        }

        if ( newClient == null) {
            newClient = mService.getClient().newBuilder();
        }
        newClient.connectTimeout(milliseconds, TimeUnit.MILLISECONDS);

        return this;
    }

    public Rendezvous setReadTimeout(long milliseconds)
    {
        if ( mService == null ) {
            return null;
        }

        if ( newClient == null) {
            newClient = mService.getClient().newBuilder();
        }

        newClient.readTimeout(milliseconds, TimeUnit.MILLISECONDS);

        return this;
    }

    public void post(Uri url, HashMap<String, String> payload, HashMap<String, String> headers, RendezvousRequestListener requestListener)
    {
        if ( mService == null) {
            return;
        }

        if ( url == null ) {
            return;
        }

        if ( payload == null ) {
            return;
        }

        if ( requestListener == null) {
            return;
        }

        requestListener.onBefore();

        if ( newClient == null) {
            newClient = mService.getClient().newBuilder().connectTimeout(RendezvousService.CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS);
            newClient.readTimeout(RendezvousService.READ_TIMEOUT, TimeUnit.MILLISECONDS);
        }

        OkHttpClient client = newClient.build();

        FormBody.Builder builder = new FormBody.Builder();
        Set<String> paramsKeys = payload.keySet();
        for (String key : paramsKeys) {
            builder.add(key, payload.get(key));
        }

        try {
            RequestBody formBody = builder.build();

            Request.Builder requestBuilder = new Request.Builder();
            Request request;

            if ( headers != null ) {
                Set<String> headerKeys = headers.keySet();
                for (String key : headerKeys) {
                    requestBuilder.addHeader(key, headers.get(key));
                }

                request = requestBuilder
                        .post(formBody)
                        .url(url.toString())
                        .build();
            }
            else  {
                request = new Request.Builder()
                        .url(url.toString())
                        .post(formBody)
                        .build();
            }

            Response response = client.newCall(request).execute();
            requestListener.onResponse(RendezvousResponse.transform(response));
        }
        catch (Exception e) {
            requestListener.onError(e);
            e.printStackTrace();
        }

    }

    public void get(Uri url, HashMap<String, String> headers, RendezvousRequestListener requestListener)
    {
        if ( mService == null) {
            return;
        }

        if ( url == null ) {
            return;
        }

        if ( requestListener == null) {
            return;
        }

        requestListener.onBefore();

        if ( newClient == null) {
            newClient = mService.getClient().newBuilder().connectTimeout(RendezvousService.CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS);
            newClient.readTimeout(RendezvousService.READ_TIMEOUT, TimeUnit.MILLISECONDS);
        }

        OkHttpClient client = newClient.build();

        try {

            Request.Builder requestBuilder = new Request.Builder();
            Request request;

            if ( headers != null ) {
                Set<String> headerKeys = headers.keySet();
                for (String key : headerKeys) {
                    requestBuilder.addHeader(key, headers.get(key));
                }

                request = requestBuilder
                        .url(url.toString())
                        .build();
            }
            else  {
                request = new Request.Builder()
                        .url(url.toString())
                        .build();
            }

            Response response = client.newCall(request).execute();

            if (response != null) {
                requestListener.onResponse(RendezvousResponse.transform(response));
            }
        }
        catch (Exception e) {
            requestListener.onError(e);
            e.printStackTrace();
        }
    }

    public RendezvousEnvironment getEnvironment()
    {
        return mRendezvousEnvironment;
    }

    public String getRoute(RendezvousRoute rendezvousRoute)
    {
        if ( mEndpoint != null ) {
            return mEndpoint.getRoute(rendezvousRoute);
        }
        return null;
    }

    @Override
    public void getItems(long page, long limit, String returnFields, RendezvousRequestListener requestListener)
    {
        if ( mEndpoint != null && mService != null ) {
            Uri url = Uri.parse(mEndpoint.getRoute(RendezvousRoute.ITEMS, page, limit, returnFields));
            mService.getAsync(url, requestListener);
        }
    }

    @Override
    public void getItems(long page, long limit, long categoryId, String returnFields, RendezvousRequestListener requestListener)
    {
        if ( mEndpoint != null && mService != null ) {
            Uri url = Uri.parse(mEndpoint.getRoute(RendezvousRoute.ITEMS_BY_CATEGORY, page, limit, categoryId, returnFields));
            mService.getAsync(url, requestListener);
        }
    }

    @Override
    public void getItemsByTags(long page, long limit, String tags, String returnFields, RendezvousRequestListener requestListener)
    {
        if ( mEndpoint != null && mService != null ) {
            Uri url = Uri.parse(mEndpoint.getRoute(RendezvousRoute.ITEMS_BY_TAGS, page, limit, tags, returnFields));
            mService.getAsync(url, requestListener);
        }
    }

    @Override
    public void getSearchItems(long page, long limit, String storeId, String clientId, String query, String returnFields, RendezvousRequestListener requestListener)
    {
        if ( mEndpoint != null && mService != null ) {
            Uri url = Uri.parse(mEndpoint.getRoute(RendezvousRoute.SEARCH, storeId, clientId, query, page, limit, returnFields));
            mService.getAsync(url, requestListener);
        }
    }

    @Override
    public void getItems(int page, int limit, String urll, RendezvousRequestListener requestListener)
    {
        if ( mEndpoint != null && mService != null ) {
            Uri url = Uri.parse(mEndpoint.getRoute(RendezvousRoute.ITEMS_BY_ALBUM, page, limit, urll));
            mService.getAsync(url, requestListener);
        }
    }

    @Override
    public void getCategories(String storeId, RendezvousRequestListener requestListener)
    {
        if ( mEndpoint != null && mService != null ) {
            Uri url = Uri.parse(String.format(mEndpoint.getRoute(RendezvousRoute.CATEGORIES), storeId));
            mService.getAsync(url, requestListener);
        }
    }

    @Override
    public void getCategoriesWithGenres(RendezvousRequestListener requestListener)
    {
        if ( mEndpoint != null && mService != null ) {
            Uri url = Uri.parse(mEndpoint.getRoute(RendezvousRoute.CATEGORIES_WITH_GENRES));
            mService.getAsync(url, requestListener);
        }
    }

    @Override
    public void getTags(long storeId, RendezvousRequestListener requestListener)
    {
        if ( mEndpoint != null && mService != null ) {
            Uri url = Uri.parse(mEndpoint.getRoute(RendezvousRoute.TAGS, storeId));
            mService.getAsync(url, requestListener);
        }
    }

    @Override
    public void tagImages(String property, String msisdn, String email, long page, long limit, boolean abbreviate, long count, String[] tags, String clientId, String storeId, RendezvousRequestListener requestListener)
    {
        if ( mEndpoint != null && mService != null ) {
            String _url = String.format(mEndpoint.getRoute(RendezvousRoute.TAG_IMAGES));
            Uri url = Uri.parse(_url);
            HashMap<String, String> payload = new HashMap<>();
            String value = "";
            JSONArray arr = new JSONArray();
            if ( tags != null ) {

                for (String c : tags) {

                    arr.put(c);

                }
                payload.put(Constants.TAGS, value);
            }
            mService.post(url, String.valueOf(arr), payload, requestListener);
        }
    }

    @Override
    public void logActivity(final String email, final String clientId, final String userType, final String userId, final String storeId, final RendezvousLogActivity activity, final RendezvousRequestListener requestListener)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if ( mEndpoint != null && mService != null ) {
                    Uri url = Uri.parse(mEndpoint.getRoute(RendezvousRoute.LOG_ACTIVITY));
                    Map<String, String> payload = new HashMap<>();
                    payload.put(Constants.ACTION, activity.getActivity());
                    payload.put(Constants.CLIENT_iD, clientId);
                    payload.put(Constants.STORE_ID, storeId);
                    payload.put(Constants.USER_TYPE, userType);
                    payload.put(Constants.USER_ID, userId);
                    payload.put(Constants.EMAIL, email);
                    mService.post(url, payload, requestListener);
                }
            }
        }).start();

    }

    @Override
    public void logActivity(final long itemId, final String email, final String clientId, final String userType, final String userId, final String storeId, final RendezvousLogActivity activity, final RendezvousRequestListener requestListener)
    {

        new Thread(new Runnable() {
            @Override
            public void run() {
                if ( mEndpoint != null && mService != null ) {
                    Uri url = Uri.parse(mEndpoint.getRoute(RendezvousRoute.LOG_ACTIVITY));
                    Map<String, String> payload = new HashMap<>();
                    payload.put(Constants.ITEM_ID, String.valueOf(itemId));
                    payload.put(Constants.CLIENT_iD, clientId);
                    payload.put(Constants.STORE_ID, storeId);
                    payload.put(Constants.USER_TYPE, userType);
                    payload.put(Constants.USER_ID, userId);
                    payload.put(Constants.EMAIL, email);
                    payload.put(Constants.ACTION, activity.getActivity());
                    mService.post(url, payload, requestListener);
                }
            }
        }).start();


    }

    @Override
    public void createComment(long itemId, long parentId, String text, String authorType, String authorReference, String clientId, String storeId, RendezvousRequestListener requestListener)
    {
        if ( mEndpoint != null && mService != null ) {
            String _url = String.format(mEndpoint.getRoute(RendezvousRoute.POST_ITEM_COMMENT), itemId, clientId, storeId);
            Uri url = Uri.parse(_url);
            Map<String, String> payload = new HashMap<>();
            payload.put(Constants.ITEM_ID, String.valueOf(itemId));
            if(parentId != 0) {
                payload.put(Constants.PARENT_ID, String.valueOf(parentId));
            }
            payload.put(Constants.TEXT, text);
            payload.put(Constants.AUTHOR_TYPE, authorType);
            payload.put(Constants.AUTHOR_REFERENCE, authorReference);
            mService.post(url, payload, requestListener);
        }
    }

    @Override
    public void getComments(long itemId, long parentId, long page, long limit, String mClientId, String storeId,  RendezvousRequestListener requestListener)
    {
        if ( mEndpoint != null && mService != null ) {
            String _url = String.format(mEndpoint.getRoute(RendezvousRoute.GET_ITEM_COMMENTS), itemId, mClientId, storeId, page, limit);
            Uri url = Uri.parse(_url);
            mService.getAsync(url, requestListener);
        }
    }

    @Override
    public void getNotifications(String clientId, String storeId, String email, long page, long limit, boolean abbreviate, boolean showRecommendation, RendezvousRequestListener requestListener){

        if ( mEndpoint != null && mService != null ) {
            String _url = String.format(mEndpoint.getRoute(RendezvousRoute.GET_NOTIFICATION_ITEMS), clientId, storeId, page, limit,email, showRecommendation, abbreviate);
            Uri url = Uri.parse(_url);
            mService.getAsync(url, requestListener);
        }

    }

    @Override
    public void getComment(long itemId, long commentId, boolean showMinimalData, RendezvousRequestListener requestListener)
    {
        if ( mEndpoint != null && mService != null ) {
            String _url = String.format(mEndpoint.getRoute(RendezvousRoute.GET_COMMENT), itemId, commentId, showMinimalData);
            Uri url = Uri.parse(_url);
            mService.getAsync(url, requestListener);
        }
    }

    @Override
    public void signUp(String clientId, String msisdn, String password, String email, Context context, String fullname, String avatarUrl, String serviceId, String storeId, String gcmId, RendezvousRequestListener requestListener)
    {


        String _url = String.format(mEndpoint.getRoute(RendezvousRoute.SIGN_UP));
        Uri url = Uri.parse(_url);
        HashMap<String, String> payload = new HashMap<>();
        payload.put(Constants.CLIENT_ID, String.valueOf(clientId));
        payload.put(Constants.PASSWORD, String.valueOf(password));
        payload.put(Constants.NAME, String.valueOf(fullname));
        payload.put(Constants.STORE_ID, String.valueOf(storeId));
        payload.put(Constants.EMAIL, String.valueOf(email));
        mService.post(url, payload, requestListener);

//        if ( mEndpoint != null && mService != null ) {
//            String _url = String.format(mEndpoint.getRoute(RendezvousRoute.SIGN_UP));
//            Uri url = Uri.parse(_url);
//            Map<String, String> payload = new HashMap<>();
//            payload.put(Constants.CLIENT_ID, String.valueOf(clientId));
//            payload.put(Constants.MSISDN, String.valueOf(msisdn));
//            payload.put(Constants.PASSWORD, String.valueOf(password));
//            payload.put(Constants.NAME, String.valueOf(fullname));
//            payload.put(Constants.STORE_ID, String.valueOf(storeId));
//            payload.put(Constants.EMAIL, String.valueOf(email));
//            mService.post(url, payload, requestListener);
//        }
    }


    @Override
    public void getFriends(String msisdn, int page, String storeId, int limit, Boolean friendsOnly, String topics, String clientId, RendezvousRequestListener requestListener) {


        if ( mEndpoint != null && mService != null ) {
            String _url = String.format(mEndpoint.getRoute(RendezvousRoute.GET_FRIENDS), msisdn, clientId, storeId,friendsOnly, page,limit);
            Uri url = Uri.parse(_url);
            mService.getAsync(url, requestListener);
        }

    }

    @Override
    public void getTopics(String msisdn, boolean countOnly, String storeId, String clientId, RendezvousRequestListener requestListener){

        if ( mEndpoint != null && mService != null ) {
            String _url = String.format(mEndpoint.getRoute(RendezvousRoute.GET_TOPICS), countOnly, storeId, msisdn, clientId);
            Uri url = Uri.parse(_url);
            mService.getAsync(url, requestListener);
        }

    }

    @Override
    public void sendTopics(String msisdn, String receiver,String limit, String tags, String storeId, String clientId, RendezvousRequestListener requestListener) {

        if ( mEndpoint != null && mService != null ) {
            String _url = String.format(mEndpoint.getRoute(RendezvousRoute.SEND_TOPICS));
            Uri url = Uri.parse(_url);
            Map<String, String> payload = new HashMap<>();
            payload.put(Constants.CLIENT_ID, String.valueOf(clientId));
            payload.put(Constants.MSISDN, String.valueOf(receiver));
            payload.put(Constants.SENDER, String.valueOf(msisdn));
            payload.put(Constants.NODATA, "true");
            payload.put(Constants.SENDEMAIL, "true");
            payload.put(Constants.TAGS, tags);
            payload.put(Constants.STORE_ID, String.valueOf(storeId));
            payload.put(Constants.LIMIT, limit);
            mService.post(url, payload, requestListener);
        }

    }


    @Override
    public void getPeopleYouMayKnow(String msisdn,int page, int limit, String clientId, String storeId, RendezvousRequestListener requestListener) {

        if ( mEndpoint != null && mService != null ) {
            String _url = String.format(mEndpoint.getRoute(RendezvousRoute.GET_PEOPLE_YOU_MAY_KNOW), msisdn, clientId, storeId, page, limit);
            Uri url = Uri.parse(_url);
            mService.getAsync(url, requestListener);
        }
    }





    @Override
    public void signIn(String clientId, String storeId, String password, String email,RendezvousRequestListener requestListener)
    {
        String _url = String.format(mEndpoint.getRoute(RendezvousRoute.SIGN_UP));
        Uri url = Uri.parse(_url);
        HashMap<String, String> payload = new HashMap<>();
        payload.put(Constants.CLIENT_ID, String.valueOf(clientId));
        payload.put(Constants.PASSWORD, String.valueOf(password));
        payload.put(Constants.STORE_ID, String.valueOf(storeId));
        payload.put(Constants.EMAIL, String.valueOf(email));
        mService.post(url, payload, requestListener);
    }

    public static Rendezvous getInstance(Context context, String clientId, long storeId, RendezvousEnvironment rendezvousEnvironment)
    {
        if ( mRendezvous == null ) {
            mRendezvous = new Rendezvous(context, clientId, storeId, rendezvousEnvironment);
        }
        return mRendezvous;
    }

    public static Rendezvous getInstance(Context context, String clientId, long storeId, String apiKey, RendezvousEnvironment rendezvousEnvironment)
    {
        if ( mRendezvous == null ) {
            mRendezvous = new Rendezvous(context, clientId, storeId, apiKey, rendezvousEnvironment);
        }
        return mRendezvous;
    }

    public static Rendezvous getInstance(Context context, String clientId, long storeId, String apiKey, String apiSecret, RendezvousEnvironment rendezvousEnvironment)
    {
        if ( mRendezvous == null ) {
            mRendezvous = new Rendezvous(context, clientId, storeId, apiKey, apiSecret, rendezvousEnvironment);
        }
        return mRendezvous;
    }





}
