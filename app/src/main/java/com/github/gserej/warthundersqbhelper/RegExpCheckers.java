package com.github.gserej.warthundersqbhelper;

import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class RegExpCheckers {

    private static final String TAG = "RegExpCheckers";
    private static Pattern r11_en;
    private static Pattern r12_en;
    private static Pattern r21_en;
    private static Pattern r22_en;
    private static Pattern r31_en;
    private static Pattern r32_en;
    private static Pattern r41_en;
    private static Pattern r42_en;
    private static Pattern r11_pl;
    private static Pattern r12_pl;
    private static Pattern r21_pl;
    private static Pattern r22_pl;
    private static Pattern r31_pl;
    private static Pattern r32_pl;
    private static Pattern r41_pl;
    private static Pattern r42_pl;

    RegExpCheckers(String tagName) {

        //hostile tank killed or crashed
        r11_en = Pattern.compile(".?" + tagName + ".+destroyed");
        r12_en = Pattern.compile("^(?!.?" + tagName + ".).*has been wrecked$");

        //friendly tank killed or crashed
        r21_en = Pattern.compile("destroyed.+" + tagName + ".");
        r22_en = Pattern.compile("^.?" + tagName + ".+has been wrecked$");

        //hostile plane shot down or crashed
        r31_en = Pattern.compile("." + tagName + ".+shot down");
        r32_en = Pattern.compile("^(?!.?" + tagName + ").+has crashed.$");

        //friendly plane shot down or crashed
        r41_en = Pattern.compile("shot down.+" + tagName + ".");
        r42_en = Pattern.compile("^.?" + tagName + ".+has crashed.$");


        r11_pl = Pattern.compile(".?" + tagName + ".+zniszczył");
        r12_pl = Pattern.compile("^(?!.?" + tagName + ".).*został rozbity$");

        r21_pl = Pattern.compile("zniszczył.+" + tagName + ".");
        r22_pl = Pattern.compile("^.?" + tagName + ".+został rozbity$");

        r31_pl = Pattern.compile("." + tagName + ".+zestrzelił");
        r32_pl = Pattern.compile("^(?!.?" + tagName + ").+rozbił się$");

        r41_pl = Pattern.compile("zestrzelił.+" + tagName + ".");
        r42_pl = Pattern.compile("^.?" + tagName + ".+rozbił się$");

    }

    static boolean checkRegExp(String message) {

        boolean found = false;

        Matcher m11_en = r11_en.matcher(message);
        Matcher m12_en = r12_en.matcher(message);

        Matcher m11_pl = r11_pl.matcher(message);
        Matcher m12_pl = r12_pl.matcher(message);

        if (m11_en.find() || m12_en.find() || m11_pl.find() || m12_pl.find()) {
            Log.d(TAG, "RegExpCheckers: ================= Found 1 ============= ");
            MainActivity.lowerHostileTanks();
            found = true;
        }

        Matcher m21_en = r21_en.matcher(message);
        Matcher m22_en = r22_en.matcher(message);

        Matcher m21_pl = r21_pl.matcher(message);
        Matcher m22_pl = r22_pl.matcher(message);

        if (m21_en.find() || m22_en.find() || m21_pl.find() || m22_pl.find()) {
            Log.d(TAG, "RegExpCheckers: ================= Found 2 ============= ");
            MainActivity.lowerFriendlyTanks();
            found = true;
        }

        Matcher m31_en = r31_en.matcher(message);
        Matcher m32_en = r32_en.matcher(message);

        Matcher m31_pl = r31_pl.matcher(message);
        Matcher m32_pl = r32_pl.matcher(message);

        if (m31_en.find() || m32_en.find() || m31_pl.find() || m32_pl.find()) {
            Log.d(TAG, "RegExpCheckers: ================= Found 3 ============= ");
            MainActivity.lowerHostilePlanes();
            found = true;
        }

        Matcher m41_en = r41_en.matcher(message);
        Matcher m42_en = r42_en.matcher(message);

        Matcher m41_pl = r41_pl.matcher(message);
        Matcher m42_pl = r42_pl.matcher(message);

        if (m41_en.find() || m42_en.find() || m41_pl.find() || m42_pl.find()) {
            Log.d(TAG, "RegExpCheckers: ================= Found 4 ============= ");
            MainActivity.lowerFriendlyPlanes();
            found = true;
        }

        return found;
    }


}
