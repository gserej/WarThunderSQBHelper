package com.github.gserej.warthundersqbhelper;

import org.junit.Assert;
import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegExpCheckersTest {

    private static final String tagName = "ABCde";
    private static final Pattern r11_en = Pattern.compile(".?" + tagName + ".+destroyed");
    private static final Pattern r12_en = Pattern.compile("^(?!.?" + tagName + ").*has been wrecked$");
    private static final Pattern r21_en = Pattern.compile("destroyed.+" + tagName + ".");
    private static final Pattern r22_en = Pattern.compile("^.?" + tagName + ".+has been wrecked$");
    private static final Pattern r31_en = Pattern.compile(".?" + tagName + ".+shot down");
    private static final Pattern r32_en = Pattern.compile("^(?!.?" + tagName + ").+has crashed.$");
    private static final Pattern r41_en = Pattern.compile("shot down.+" + tagName + ".");
    private static final Pattern r42_en = Pattern.compile("^.?" + tagName + ".+has crashed.$");

    private static String message;


    @Test
    public void hostileTankDownEN() {

        message = "ABCde Player1 (Sturmpanzer II) destroyed QWERT Player2 (Tiger H1)";
        Matcher m11_en = r11_en.matcher(message);
        Assert.assertTrue(m11_en.find());

        message = "*ABCde* Player1 (Sturmpanzer II) destroyed -QWERT- Player2 (Tiger H1)";
        m11_en = r11_en.matcher(message);
        Assert.assertTrue(m11_en.find());

        message = "=XYZ= Player2 (␗M8) has been wrecked";
        Matcher m12_en = r12_en.matcher(message);
        Assert.assertTrue(m12_en.find());

    }

    @Test
    public void friendlyTankDownEN() {

        message = "=XYZ= Player2 (␗M8) destroyed =ABCde= Player1(Tiger H1)";
        Matcher m21_en = r21_en.matcher(message);
        Assert.assertTrue(m21_en.find());

        message = "*ABCde* Player1(␗M8) has been wrecked";
        Matcher m22_en = r22_en.matcher(message);
        Assert.assertTrue(m22_en.find());


    }

    @Test
    public void hostilePlaneDownEN() {

        message = "*ABCde* Player1 (MiG-15bis ISH) shot down =XYZ= Player2 (Tiger H1)";
        Matcher m31_en = r31_en.matcher(message);
        Assert.assertTrue(m31_en.find());

        message = "=XYZ= Player2(␗M8) has crashed.";
        Matcher m32_en = r32_en.matcher(message);
        Assert.assertTrue(m32_en.find());

    }

    @Test
    public void friendlyPlaneDownEN() {

        message = "=AAAAA= Player2 (SB2U-3) shot down *ABCde* Player1 (Su-2)";
        Matcher m41_en = r41_en.matcher(message);
        Assert.assertTrue(m41_en.find());

        message = "=ABCde= Player1 (Pe-8) has crashed.";
        Matcher m42_en = r42_en.matcher(message);
        Assert.assertTrue(m42_en.find());

    }
}