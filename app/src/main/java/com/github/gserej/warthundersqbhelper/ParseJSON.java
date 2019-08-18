package com.github.gserej.warthundersqbhelper;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ParseJSON extends AsyncTask<String, Void, List<Lines>> implements GetRawData.OnDownloadComplete {

    private static final String TAG = "ParseJSON";
    private static String tagName;
    private final OnDataAvailable mCallBack;
    private List<Lines> linesList = null;
    private String mBaseURL;
    private boolean runningOnSameThread = false;

    ParseJSON(OnDataAvailable callBack, String baseURL) {
        mBaseURL = baseURL;
        mCallBack = callBack;
    }

    static void setTagName(String tagName) {
        ParseJSON.tagName = tagName;
    }

    @Override
    protected List<Lines> doInBackground(String... params) {
        String destinationUri = Uri.parse(mBaseURL).toString();
        GetRawData getRawData = new GetRawData(this);
        getRawData.runInSameThread(destinationUri);
        return linesList;
    }

    @Override
    protected void onPostExecute(List<Lines> linesList) {
        if (mCallBack != null) {
            mCallBack.onDataAvailable(linesList, DownloadStatus.OK);
        }
    }

    @Override
    public void onDownloadComplete(String data, DownloadStatus status) {

        int idTemp;


        //enemy tank killed or crashed
        Pattern r11_en = Pattern.compile("." + tagName + ".+destroyed");
        Pattern r12_en = Pattern.compile("^(?!." + tagName + ".).*has been wrecked$");

        //ally tank killed or crashed
        Pattern r21_en = Pattern.compile("destroyed.+." + tagName + ".");
        Pattern r22_en = Pattern.compile("^." + tagName + ".+has been wrecked$");

        //enemy plane shot down or crashed
        Pattern r31_en = Pattern.compile("." + tagName + ".+shot down");
        Pattern r32_en = Pattern.compile("^(?!." + tagName + ".).+has crashed.$");

        //ally plane shot down or crashed
        Pattern r41_en = Pattern.compile("shot down.+" + tagName + ".");
        Pattern r42_en = Pattern.compile("^." + tagName + ".+has crashed.$");


        Pattern r11_pl = Pattern.compile("." + tagName + ".+zniszczył");
        Pattern r12_pl = Pattern.compile("^(?!." + tagName + ".).*został rozbity$");

        Pattern r21_pl = Pattern.compile("zniszczył.+." + tagName + ".");
        Pattern r22_pl = Pattern.compile("^." + tagName + ".+został rozbity$");

        Pattern r31_pl = Pattern.compile("." + tagName + ".+zestrzelił");
        Pattern r32_pl = Pattern.compile("^(?!." + tagName + ".).+rozbił się$");

        Pattern r41_pl = Pattern.compile("zestrzelił.+" + tagName + ".");
        Pattern r42_pl = Pattern.compile("^." + tagName + ".+rozbił się$");

        boolean found;
//        Log.d(TAG, "onDownloadComplete: starts. Status = " + status);
        if (status == DownloadStatus.OK) {
            linesList = new ArrayList<>();
            try {

                JSONObject jsonData = new JSONObject(data);
                JSONArray itemsArray = jsonData.getJSONArray("damage");
                for (int i = 0; i < itemsArray.length(); i++) {
                    found = false;
                    JSONObject jsonLine = itemsArray.getJSONObject(i);
                    String id = jsonLine.getString("id");
                    String message = jsonLine.getString("msg");
                    idTemp = Integer.parseInt(id);

                    if (idTemp > MainActivity.getIdHigh()) {
                        MainActivity.setIdHigh(idTemp);

                        Matcher m11_en = r11_en.matcher(message);
                        Matcher m12_en = r12_en.matcher(message);

                        Matcher m11_pl = r11_pl.matcher(message);
                        Matcher m12_pl = r12_pl.matcher(message);

                        if (m11_en.find() || m12_en.find() || m11_pl.find() || m12_pl.find()) {
                            Log.d(TAG, "onDownloadComplete: ================= Found 1 ============= ");
                            MainActivity.lowerEnemyTanks();
                            found = true;
                        }

                        Matcher m21_en = r21_en.matcher(message);
                        Matcher m22_en = r22_en.matcher(message);

                        Matcher m21_pl = r21_pl.matcher(message);
                        Matcher m22_pl = r22_pl.matcher(message);

                        if (m21_en.find() || m22_en.find() || m21_pl.find() || m22_pl.find()) {
                            Log.d(TAG, "onDownloadComplete: ================= Found 2 ============= ");
                            MainActivity.lowerOurTanks();
                            found = true;
                        }

                        Matcher m31_en = r31_en.matcher(message);
                        Matcher m32_en = r32_en.matcher(message);

                        Matcher m31_pl = r31_pl.matcher(message);
                        Matcher m32_pl = r32_pl.matcher(message);

                        if (m31_en.find() || m32_en.find() || m31_pl.find() || m32_pl.find()) {
                            Log.d(TAG, "onDownloadComplete: ================= Found 3 ============= ");
                            MainActivity.lowerEnemyPlanes();
                            found = true;
                        }

                        Matcher m41_en = r41_en.matcher(message);
                        Matcher m42_en = r42_en.matcher(message);

                        Matcher m41_pl = r41_pl.matcher(message);
                        Matcher m42_pl = r42_pl.matcher(message);

                        if (m41_en.find() || m42_en.find() || m41_pl.find() || m42_pl.find()) {
                            Log.d(TAG, "onDownloadComplete: ================= Found 4 ============= ");
                            MainActivity.lowerOurPlanes();
                            found = true;
                        }

                        if (found) {
//                        Lines lineObject = new Lines(id, message);
//                        linesList.add(lineObject);
//                        Log.d(TAG, "onDownloadComplete: " + lineObject.toString());
                            Log.d(TAG, "onDownloadComplete: " + message);
                        }
                    }

                }
            } catch (JSONException jsone) {
                jsone.printStackTrace();
                Log.e(TAG, "onDownloadComplete: Error processing Json data " + jsone.getMessage());
                status = DownloadStatus.FAILED_OR_EMPTY;
            }
        }
        if (runningOnSameThread && mCallBack != null) {
            mCallBack.onDataAvailable(linesList, status);
        }
    }

    interface OnDataAvailable {
        void onDataAvailable(List<Lines> data, DownloadStatus status);
    }
}