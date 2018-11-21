package usi.memotion2.remote.database.controllers;


import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import usi.memotion2.remote.database.RemoteStorageController;

import java.util.concurrent.CountDownLatch;

/**
 * Created by usi on 17/01/17.
 */
public class SwitchDriveController implements RemoteStorageController {
    private String serverAddress;
    private String accessToken;
    private String password;
    private CountDownLatch doneSignal;
    private int httpResponse;
    private OnTransferCompleted callback;
    private OnUpload onUpload;



    public SwitchDriveController(String serverAddress, String accessToken, String password) {
        this.serverAddress = serverAddress;
        this.accessToken = accessToken;
        this.password = password;
//        doneSignal = new CountDownLatch(1);

    }

    public void setInterface(OnUpload onUpload){
        this.onUpload = onUpload;
    }

    @Override
    public int upload(String fileName, String data) {
//        doneSignal = new CountDownLatch(1);
        new DataUploadTask(serverAddress, accessToken).execute(fileName, data);
//        try {
//            doneSignal.await();
//        } catch (InterruptedException e) {
////            e.printStackTrace();
//            httpResponse = -1;
//        }
        return httpResponse;
    }

    private class DataUploadTask extends AsyncTask<String, Void, Integer> {
        private String serverAddress;
        private String accessToken;
        private String fileName;

        public DataUploadTask(String serverAddress, String accessToken) {
            this.serverAddress = serverAddress;
            this.accessToken = accessToken;
        }

        @Override
        protected Integer doInBackground(String... params) {
            int httpStatus;

            try {
                String[] data = new String[2];

                int i = 0;
                for(String par: params) {
                    data[i] = par;
                    i++;
                }
                fileName = data[0];
                URL url = new URL(serverAddress + data[0]);

                Authenticator.setDefault(new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(accessToken, password.toCharArray());
                    }
                });
                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                conn.setRequestMethod("PUT");
                conn.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestProperty("Accept", "*/*");
                conn.getOutputStream().write(data[1].getBytes());
                conn.getOutputStream().flush();
                conn.getOutputStream().close();
                httpStatus = conn.getResponseCode();
            } catch (Exception e) {
                e.printStackTrace();
                httpStatus = -1;
            }

//            httpResponse = httpStatus;
//            doneSignal.countDown();
            return httpStatus;
        }

        @Override
        protected void onPostExecute(Integer result) {
            int httpResponse = result;
            if(!String.valueOf(httpResponse).startsWith("2")){
                onUpload.sendResponse("Upload of data remotely has failed! Please make sure you have internet connection and the right password! If not, do contact us.");
                Log.v("DATA UPLOAD SERVICE","UPLOAD FAILED");
            } else {
                Log.v("DATA UPLOAD SERVICE","UPLOAD SUCCESSFUL");
                onUpload.sendResponse("Upload of data remotely was successful!");
            }

            //callback.onTransferCompleted(fileName, result);
//            doneSignal.countDown();
        }
    }

    public interface OnTransferCompleted {
        void onTransferCompleted(String fileName, int status);
    }

    public interface OnUpload{
        void sendResponse(String message);

    }

    public void setCallback(OnTransferCompleted callback) {
        this.callback = callback;
    }
}




