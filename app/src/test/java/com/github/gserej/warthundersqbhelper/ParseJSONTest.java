package com.github.gserej.warthundersqbhelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

public class ParseJSONTest {

    @Test
    public void parseDataTest() {

        String data = "{\"events\":[\n" +
                "\n" +
                "],\n" +
                "\"damage\":[\n" +
                "{ \"id\": 577, \"msg\": \"TestPlayer1 (Abrams) destroyed TestPlayer2 (T-44)\", \"sender\": \"\", \"enemy\": false, \"mode\": \"\" },\n" +
                "{ \"id\": 578, \"msg\": \"TestPlayer3 (XP-38G) has crashed.\", \"sender\": \"\", \"enemy\": false, \"mode\": \"\" },\n" +
                "{ \"id\": 579, \"msg\": \"TestPlayer4! kd?NET_PLAYER_DISCONNECT_FROM_GAME\", \"sender\": \"\", \"enemy\": false, \"mode\": \"\" }\n" +
                "]}";
        int idTemp;
        String message;
        try {
            JSONObject jsonData = new JSONObject(data);
            JSONArray itemsArray = jsonData.getJSONArray("damage");

            JSONObject jsonLine = itemsArray.getJSONObject(0);
            idTemp = jsonLine.getInt("id");
            Assert.assertEquals(577, idTemp);
            message = jsonLine.getString("msg");
            Assert.assertEquals("TestPlayer1 (Abrams) destroyed TestPlayer2 (T-44)", message);

            jsonLine = itemsArray.getJSONObject(1);
            idTemp = jsonLine.getInt("id");
            Assert.assertEquals(578, idTemp);
            message = jsonLine.getString("msg");
            Assert.assertEquals("TestPlayer3 (XP-38G) has crashed.", message);

            jsonLine = itemsArray.getJSONObject(2);
            idTemp = jsonLine.getInt("id");
            Assert.assertEquals(579, idTemp);
            message = jsonLine.getString("msg");
            Assert.assertEquals("TestPlayer4! kd?NET_PLAYER_DISCONNECT_FROM_GAME", message);

        } catch (JSONException jsone) {
            jsone.printStackTrace();

        }

    }
}