package com.github.gserej.warthundersqbhelper;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class RegExpCheckersTest {

    private static String message;
    private static final String tagName = "ABCde";

    @BeforeClass
    public static void init() {
        new RegExpCheckers(tagName);
    }

    @Test
    public void isHostileTankDown() {

        message = "*" + tagName + "* Player1 (Sturmpanzer II) destroyed -QWERT- Player2 (Tiger H1)";
        Assert.assertTrue(RegExpCheckers.isHostileTankDown(message));

        message = "*" + tagName + "* Player1 (SB2U-3) destroyed AAA";
        Assert.assertFalse(RegExpCheckers.isHostileTankDown(message));

        message = "=XYZ= Player2 (SB2U-3) destroyed AAA";
        Assert.assertFalse(RegExpCheckers.isHostileTankDown(message));

        message = "*" + tagName + "* Player1 (Sturmpanzer II) destroyed Player2 (Tiger H1)";
        Assert.assertTrue(RegExpCheckers.isHostileTankDown(message));

        message = "." + tagName + ". Player1 (Sturmpanzer II) destroyed ." + tagName + ". Player2 (Tiger H1)";
        Assert.assertFalse(RegExpCheckers.isHostileTankDown(message));

        message = "*" + tagName + "* Player1 (Sturmpanzer II) destroyed " + tagName + " Player2 (Tiger H1)";
        Assert.assertFalse(RegExpCheckers.isHostileTankDown(message));

        message = "=XYZ= Player2 (␗M8) has been wrecked";
        Assert.assertTrue(RegExpCheckers.isHostileTankDown(message));

        message = "Player2 (␗M8) has been wrecked";
        Assert.assertTrue(RegExpCheckers.isHostileTankDown(message));
    }

    @Test
    public void isFriendlyTankDown() {

        message = "=XYZ= Player2 (␗M8) destroyed =" + tagName + "= Player1 (Tiger H1)";
        Assert.assertTrue(RegExpCheckers.isFriendlyTankDown(message));

        message = "=XYZ= Player2 (␗M8) destroyed " + tagName + " Player1 (Tiger H1)";
        Assert.assertTrue(RegExpCheckers.isFriendlyTankDown(message));

        message = "*" + tagName + "* Player1(␗M8) has been wrecked";
        Assert.assertTrue(RegExpCheckers.isFriendlyTankDown(message));

        message = tagName + " Player1(␗M8) has been wrecked";
        Assert.assertTrue(RegExpCheckers.isFriendlyTankDown(message));

        message = "=" + tagName + "= Player1 (Sturmpanzer II) destroyed =" + tagName + "= Player2 (Tiger H1)";
        Assert.assertTrue(RegExpCheckers.isFriendlyTankDown(message));

        message = tagName + " Player1 (Sturmpanzer II) destroyed " + tagName + " Player2 (Tiger H1)";
        Assert.assertTrue(RegExpCheckers.isFriendlyTankDown(message));

        message = tagName + " Player1 (Sturmpanzer II) destroyed " + tagName + " Player2 (Tiger H1)";
        Assert.assertTrue(RegExpCheckers.isFriendlyTankDown(message));

        message = "-XYZZY- Player1 (Sturmpanzer II) destroyed -XYZZY- Player2 (Tiger H1)";
        Assert.assertFalse(RegExpCheckers.isFriendlyTankDown(message));

        message = "=XYZ= Player2 (SB2U-3) destroyed AAA";
        Assert.assertFalse(RegExpCheckers.isFriendlyTankDown(message));

        message = "=" + tagName + "= Player1 (SB2U-3) destroyed AAA";
        Assert.assertFalse(RegExpCheckers.isFriendlyTankDown(message));
    }

    @Test
    public void isHostilePlaneDown() {

        message = "*" + tagName + "* Player1 (MiG-15bis ISH) shot down =XYZ= Player2 (Su-2)";
        Assert.assertTrue(RegExpCheckers.isHostilePlaneDown(message));

        message = "AAA shot down =XYZ= Player2 (MiG-15bis ISH)";
        Assert.assertTrue(RegExpCheckers.isHostilePlaneDown(message));

        message = "AAA shot down =XYZ= Player2 (MiG-15bis ISH)";
        Assert.assertTrue(RegExpCheckers.isHostilePlaneDown(message));

        message = "=XYZ= Player2(MiG-15bis ISH) has crashed.";
        Assert.assertTrue(RegExpCheckers.isHostilePlaneDown(message));
    }

    @Test
    public void isFriendlyPlaneDown() {

        message = "=AAAAA= Player2 (SB2U-3) shot down *" + tagName + "* Player1 (Su-2)";
        Assert.assertTrue(RegExpCheckers.isFriendlyPlaneDown(message));

        message = "=" + tagName + "= Player1 (Pe-8) has crashed.";
        Assert.assertTrue(RegExpCheckers.isFriendlyPlaneDown(message));

        message = "AAA shot down =XYZ= Player2 (Pe-8)";
        Assert.assertFalse(RegExpCheckers.isFriendlyPlaneDown(message));

        message = "AAA shot down *" + tagName + "* Player1 (Su-2)";
        Assert.assertTrue(RegExpCheckers.isFriendlyPlaneDown(message));
    }
}