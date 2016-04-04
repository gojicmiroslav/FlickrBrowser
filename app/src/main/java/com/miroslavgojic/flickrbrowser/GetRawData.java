package com.miroslavgojic.flickrbrowser;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 3/26/2016.
 */

enum DownloadStatus { IDLE, PROCESSING, NOT_INITIALISED, FAILED_OR_EMPTY, OK }

// For accessing any URL
public class GetRawData {
    private String LOG_TAG = GetRawData.class.getSimpleName();
    private String mRawUrl;
    private String mData;
    private DownloadStatus mDownloadStatus;

    public GetRawData(String mRawUrl) {
        this.mRawUrl = mRawUrl;
        this.mDownloadStatus = DownloadStatus.IDLE;
    }

    public void reset(){
        this.mDownloadStatus = DownloadStatus.IDLE;
        this.mRawUrl = null;
        this.mData = null;
    }

    public String getmData() {
        return mData;
    }

    public DownloadStatus getmDownloadStatus() {
        return mDownloadStatus;
    }

    public void setmRawUrl(String mRawUrl) {
        this.mRawUrl = mRawUrl;
    }

    public void execute(){
        this.mDownloadStatus = DownloadStatus.PROCESSING;
        DownloadRawData downloadRawData = new DownloadRawData();
        downloadRawData.execute(mRawUrl);
    }

    public class DownloadRawData extends AsyncTask<String, Void, String>{

        // executes after we download raw data
        @Override
        protected void onPostExecute(String webData) {
            // webData - data thats returned from donwloading procces
            mData = webData;
            Log.v(LOG_TAG, "Data returned was: " + mData);

            //Setup downloading status
            if(mData == null){
                if(mRawUrl == null){
                    // not valid URL or empty URL
                    mDownloadStatus = DownloadStatus.NOT_INITIALISED;
                } else {
                    // error accessing that URL for some reason
                    mDownloadStatus = DownloadStatus.FAILED_OR_EMPTY;
                }
            } else {
                // success
                mDownloadStatus = DownloadStatus.OK;
            }
        }

        @Override
        protected String doInBackground(String... params) {
            // create connection
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            if(params == null) {
                return null;
            }

            try {
                URL url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                if(inputStream == null){
                    return null;
                }
                StringBuffer buffer = new StringBuffer();

                // start reading
                reader = new BufferedReader(new InputStreamReader(inputStream));
                // temporary line to store each line of text
                String line;
                while((line = reader.readLine()) != null){
                    // append data to what we already have
                    // \n - only for nice formating, not for proccesing
                    buffer.append(line + "\n");
                }

                return buffer.toString();

            } catch (IOException e){
                Log.e(LOG_TAG, "Error", e);
                return null;
            } finally {
                if( urlConnection != null){
                    urlConnection.disconnect();
                }

                if( reader != null){
                    try{
                        reader.close();
                    } catch (final IOException e){
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
        }
    }
}
