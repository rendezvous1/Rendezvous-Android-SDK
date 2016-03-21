package com.rancard.rndvusdk.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by: Robert Wilson.
 * Date: Feb 18, 2016
 * Time: 10:28 AM
 * Package: com.multimedia.joyonline.utils
 * Project: JoyOnline-Android
 */
public class Utils {

    public static final String CLIENT_ID = "qB0ugjfMB1sV7A"; // SANDBOX=74nc4r6rn6vu | PRODUCTION=qB0ugjfMB1sV7A
    public static final long STORE_ID = 158L; // SANDBOX=135L | PRODUCTION=158L
    public static final long DEFAULT_LIMIT = 30L;
    public static final long DEFAULT_PAGE = 1L;


    public static List<String> extractUrlsFromText(String text) {
        List<String> urls = new ArrayList<String>();
        Pattern pattern = Pattern.compile("\\(?\\b(http://|www[.])[-A-Za-z0-9+&amp;@#/%?=~_()|!:,.;" +
                "]*[-A-Za-z0-9+&amp;@#/%=~_()|]");
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            String url = matcher.group();
            if (url.startsWith("(") && url.endsWith(")")) {
                url = url.substring(1, url.length() - 1);
            }
            urls.add(url);
        }
        return urls;
    }
}