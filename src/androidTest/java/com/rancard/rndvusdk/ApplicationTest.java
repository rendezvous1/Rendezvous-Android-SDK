package com.rancard.rndvusdk;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.rancard.rndvusdk.utils.Constants;
import com.rancard.rndvusdk.utils.RendezvousDeviceManager;

import java.util.HashMap;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application>
{
    public ApplicationTest()
    {
        super(Application.class);
    }

    public void testRendezvousSandboxRoute()
    {
        Rendezvous rendezvous = Rendezvous.getInstance(mContext, "", 123, RendezvousEnvironment.SANDBOX);
        //String url = rendezvous.getRoute(RendezvousRoute.SIGN_IN);
        HashMap<String, String> di = RendezvousDeviceManager.getDeviceInfo(mContext);
        assertEquals("SIGN IN URL SANDBOX", "Tablet", di.get(Constants.DEVISE_TYPE));
    }

    /*public void testRendezvousStagingRoute()
    {
        Rendezvous rendezvous = Rendezvous.getInstance(123, 123, RendezvousEnvironment.STAGING);
        String url = rendezvous.getRoute(RendezvousRoute.SIGN_IN);
        assertEquals("SIGN IN URL STAGING", "https://staging.rndvu.me/v1/payments/account/login", url);
    }

    public void testRendezvousProductionMirrorRoute()
    {
        Rendezvous rendezvous = Rendezvous.getInstance(123, 123, RendezvousEnvironment.PRODUCTION_MIRROR);
        String url = rendezvous.getRoute(RendezvousRoute.SIGN_IN);
        assertEquals("SIGN IN URL PRODUCTION_MIRROR", "http://api-test.rancardmobility.com/v1/payments/account/login", url);
    }

    public void testRendezvousProductionRoute()
    {
        Rendezvous rendezvous = Rendezvous.getInstance(123, 123, RendezvousEnvironment.PRODUCTION);
        String url = rendezvous.getRoute(RendezvousRoute.SIGN_IN);
        assertEquals("SIGN IN URL PRODUCTION", "https://api.rancardmobility.com/v1/payments/account/login", url);
    }*/

}