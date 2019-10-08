package com.github.gserej.warthundersqbhelper;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class ParseJSON extends AsyncTask<String, Void, Void> implements OnDownloadComplete {

    private static final String TAG = "ParseJSON";
    private static String tagName;
    private static String mBaseURL;

    ParseJSON() {
        mBaseURL = MainActivity.getFullUrl();
    }

    static void setTagName(String tagName) {
        ParseJSON.tagName = tagName;
    }

    @Override
    protected Void doInBackground(String... params) {
        String destinationUri = Uri.parse(mBaseURL).toString();
        GetRawData getRawData = new GetRawData(this);
        getRawData.runInSameThread(destinationUri);
        return null;
    }

    @Override
    public void onDownloadComplete(String data, DownloadStatus status) {

        int idTemp;
        if (status == DownloadStatus.OK) {
            try {
                JSONObject jsonData = new JSONObject(data);
                JSONArray itemsArray = jsonData.getJSONArray("damage");
                for (int i = 0; i < itemsArray.length(); i++) {

                    JSONObject jsonLine = itemsArray.getJSONObject(i);
                    idTemp = jsonLine.getInt("id");
                    String message = jsonLine.getString("msg");

                    if (idTemp > MainActivity.getIdHigh()) {
                        MainActivity.setIdHigh(idTemp);

                        new RegExpCheckers(tagName);
                        if (RegExpCheckers.checkRegExp(message)) {
                            Log.d(TAG, "onDownloadComplete: " + message);
                        }
                    }

                }
            } catch (JSONException jsone) {
                jsone.printStackTrace();
                Log.e(TAG, "onDownloadComplete: Error processing Json data " + jsone.getMessage());
            }
        }
    }
}