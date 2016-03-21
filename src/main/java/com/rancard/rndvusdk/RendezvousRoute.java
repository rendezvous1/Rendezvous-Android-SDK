package com.rancard.rndvusdk;

/**
 * Created by: Robert Wilson.
 * Date: Feb 03, 2016
 * Time: 8:11 PM
 * Package: com.rancard.rndvusdk
 * Project: Rendezvous-Android-SDK-Minimal
 */
public enum RendezvousRoute
{

    SIGN_UP("/users/signUp"),
    ITEMS("/store/items?page=%d&limit=%d&return=%s"),
    ITEMS_BY_CATEGORY("/store/items?page=%d&limit=%d&categoryId=%d&return=%s"),
    ITEMS_BY_TAGS("/store/items/by/tags?page=%d&limit=%d&tags=%s&return=%s"),
    SEARCH("/store/%s/search?clientId=%s&query=%s&page=%d&limit=%d&return=%s"),
    ITEMS_BY_ALBUM("/feed/url?page=%d&limit=%d&url=%s&responseType=json"),
    CATEGORIES("/store/%s/categories?showCategoriesOnly=true"),
    CATEGORIES_WITH_GENRES("/store/%s/categories?showCategoriesOnly=true"),
    TAGS("/tags?storeId=%d"),
    TAG_IMAGES("/items/by/tags?property=return=previewUrl&clientId=74nc4r6rn6vu&storeId=135"),
    LOG_ACTIVITY("/social/user/activity"),
    GRAPH_CONTACTS("/user/contacts"),
    POST_ITEM_COMMENT("/items/%d/comments?clientId=%s&storeId=%s"),
    GET_ITEM_COMMENTS("/items/%d/comments?clientId=%s&storeId=%s&page=%d&limit=%d"),
    GET_NOTIFICATION_ITEMS("/user/notifications/inbox?clientId=%s&storeId=%s&page=%d&limit=%d&email=%s&showRecommendations=%b&abbreviate=%b"),
    GET_COMMENT("/items/%d/comments/%d?showMinimalData=%b"), // /items/{itemId}/comments/{commentId}
    SHARE("/rndvu/share"),
    GET_FRIENDS("/rndvu/user/%s/knows?client_id=%s&storeId=%s&friendsOnly=%b&page=%d&limit=%d"),
    GET_PEOPLE_YOU_MAY_KNOW("/rndvu/user/%s/may/know?client_id=%s&storeId=%s&page=%d&limit=%d"),
    GET_TOPICS("/v1/rndvu/user/topics?countOnly=%b&storeId=%s&msisdns=%s&client_id=%s"),
    SEND_TOPICS("/v1/rndvu/user/topics/trending");


    private String mRoute;

    RendezvousRoute(String route)
    {
        mRoute = route;
    }

    public String getRoute()
    {
        return mRoute;
    }
}
