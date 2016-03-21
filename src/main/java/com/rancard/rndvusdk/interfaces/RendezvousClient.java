package com.rancard.rndvusdk.interfaces;

import android.content.Context;

import com.rancard.rndvusdk.RendezvousLogActivity;

/**
 * Created by: Robert Wilson.
 * Date: Feb 02, 2016
 * Time: 4:02 PM
 * Package: com.rancard.rndvusdk.interfaces
 * Project: Rendezvous SDK
 */
public interface RendezvousClient
{


    void logActivity(long itemId,
                     String msisdn,
                     String clientId,
                     String userType,
                     String userId,
                     String storeId,
                     RendezvousLogActivity activity,
                     RendezvousRequestListener requestListener);

    void logActivity(final String email,
                     final String clientId,
                     final String userType,
                     final String userId,
                     final String storeId,
                     final RendezvousLogActivity activity,
                     final RendezvousRequestListener requestListener);

    void createComment(
            long itemId,
            long parentId,
            String text,
            String authorType,
            String authorReference,
            String clientId,
            String storeId,
            RendezvousRequestListener requestListener);

    void getComments (
            long itemId,
            long parentId,
            long page,
            long limit,
            String clientId,
            String storeId,
            RendezvousRequestListener requestListener);

    void getComment (
            long itemId,
            long commentId,
            boolean showMinimalData,
            RendezvousRequestListener requestListener);

    void getItems(
            long page,
            long limit,
            String returnFields,
            RendezvousRequestListener requestListener);

    void getItems(
            long page,
            long limit,
            long categoryId,
            String returnFields,
            RendezvousRequestListener requestListener);

    void getTags(
            long storeId,
            RendezvousRequestListener requestListener);

    void tagImages(
            String property,
            String msisdn,
            String email,
            long page,
            long limit,
            boolean abbreviate,
            long count,
            String[] tags,
            String clientId,
            String storeId,
            RendezvousRequestListener requestListener);

    void getNotifications(
            String clientId,
            String storeId,
            String email,
            long page,
            long limit,
            boolean abbreviate,
            boolean showRecommendation,
            RendezvousRequestListener requestListener);

   void getCategories(String storeId,
                      RendezvousRequestListener requestListener);

    void getCategoriesWithGenres(
            RendezvousRequestListener requestListener
    );

    void signUp(
            String clientId,
            String msisdn,
            String password,
            String email,
            Context context,
            String fullname,
            String avatarUrl,
            String serviceId,
            String storeId,
            String gcmId,
            RendezvousRequestListener requestListener
    );


    void getItemsByTags(long page,
                        long limit,
                        String tags,
                        String returnFields,
                        RendezvousRequestListener requestListener);

    void getFriends(String msisdn,
                           int page,
                           String storeId,
                           int limit,
                           Boolean friendsOnly,
                           String topics,
                           String clientId,
                           RendezvousRequestListener requestListener);

    void getTopics(String msisdn,
                          boolean countOnly,
                          String storeId,
                          String clientId,
                          RendezvousRequestListener requestListener);

    void signIn(String clientId,
                       String storeId,
                       String password,
                       String email,
                       RendezvousRequestListener requestListener);

    void getPeopleYouMayKnow(String msisdn,
                                    int page,
                                    int limit,
                                    String clientId,
                                    String storeId,
                                    RendezvousRequestListener requestListener);

    public void getItems(int page,
                         int limit,
                         String urll,
                         RendezvousRequestListener requestListener);

    void sendTopics(String msisdn,
                    String receiver,
                    String limit,
                    String tags,
                    String storeId,
                    String clientId,
                    RendezvousRequestListener requestListener);

    void getSearchItems(long page,
                        long limit,
                        String storeId,
                        String clientId,
                        String query,
                        String returnFields,
                        RendezvousRequestListener requestListener);

}
