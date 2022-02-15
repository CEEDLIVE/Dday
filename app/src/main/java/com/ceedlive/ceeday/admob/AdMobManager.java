package com.ceedlive.ceeday.admob;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class AdMobManager {

    public static void setAds(AdView targetAdView) {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("8A807912B473B630ADD61488024D05EB") // This request is sent from a test device.
                .addTestDevice("5E52A824C274C8491B1CA21E1FD6E82F") // This request is sent from a test device.
                .addTestDevice("02BAA7172204A562C207F49284761F2A") // This request is sent from a test device.
                .addTestDevice("B0F93796F39191FA5B3D0749CFC15967") // This request is sent from a test device.
                .build();
        targetAdView.loadAd(adRequest);
    }

}
