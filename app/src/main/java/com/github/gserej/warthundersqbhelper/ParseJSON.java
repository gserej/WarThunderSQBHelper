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
    private List<Lines> linesList = null;
    private String mBaseURL;

    private final OnDataAvailable mCallBack;

    private boolean runningOnSameThread = false;
    private static String tagName;

    static void setTagName(String tagName) {
        ParseJSON.tagName = tagName;
    }

    interface OnDataAvailable {
        void onDataAvailable(List<Lines> data, DownloadStatus status);
    }

    ParseJSON(OnDataAvailable callBack, String baseURL) {
        mBaseURL = baseURL;
        mCallBack = callBack;
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

        Pattern r11 = Pattern.compile("."+tagName+".+destroyed");
        Pattern r12 = Pattern.compile("^(?!."+tagName+".).*has been wrecked$");

        Pattern r21 = Pattern.compile("destroyed.+."+tagName+".");
        Pattern r22 = Pattern.compile("^."+tagName+".+has been wrecked$");

        Pattern r31 = Pattern.compile("."+tagName+".+shot down");
        Pattern r32 = Pattern.compile("^(?!."+tagName+".).+has crashed.$");

        Pattern r41 = Pattern.compile("shot down.+"+tagName+".");
        Pattern r42 = Pattern.compile("^."+tagName+".+has crashed.$");

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

                        Matcher m11 = r11.matcher(message);
                        Matcher m12 = r12.matcher(message);

                        if (m11.find() || m12.find()) {
                            System.out.println("================= Found 1 ============= : ");
                            MainActivity.lowerEnemyTanks();
                            found = true;
                        }

                        Matcher m21 = r21.matcher(message);
                        Matcher m22 = r22.matcher(message);

                        if (m21.find() || m22.find()) {
                            System.out.println("================= Found 2 ============= : ");
                            MainActivity.lowerOurTanks();
                            found = true;
                        }

                        Matcher m31 = r31.matcher(message);
                        Matcher m32 = r32.matcher(message);

                        if (m31.find() || m32.find()) {
                            System.out.println("================= Found 3 ============= : ");
                            MainActivity.lowerEnemyPlanes();
                            found = true;
                        }

                        Matcher m41 = r41.matcher(message);
                        Matcher m42 = r42.matcher(message);

                        if (m41.find() || m42.find()) {
                            System.out.println("================= Found 4 ============= : ");
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


}