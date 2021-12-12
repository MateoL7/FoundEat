package com.example.foundeat.util;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class HTTPSWebUtilDomi {

    public String GETrequest(String url) {
        try {
            URL page = new URL(url);
            HttpsURLConnection connection = (HttpsURLConnection) page.openConnection();
            InputStream is = connection.getInputStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }
            is.close();
            baos.close();
            connection.disconnect();
            return new String(baos.toByteArray(), "UTF-8");
        }catch (IOException ex){
            ex.printStackTrace();
            return "";
        }
    }

    public String POSTrequest(String url, String json) {
        try {
            URL page = new URL(url);
            HttpsURLConnection connection = (HttpsURLConnection) page.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("accept", "application/json");
            connection.setDoInput(true);
            connection.setDoOutput(true);

            OutputStream os = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

            writer.write(json);
            writer.flush();

            InputStream is = connection.getInputStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }
            is.close();
            baos.close();
            os.close();
            connection.disconnect();
            return new String(baos.toByteArray(), "UTF-8");
        }catch (IOException ex){
            ex.printStackTrace();
            return "";
        }
    }


    public static final String FCM_KEY = "AAAAr31yV9w:APA91bGTKyNJFFoi0XgqX3jI1j8_XNAjjPv1eOpevVBpdY2S_g65GHm_xNea95Dhww8VRs9dwg0Te2lXDfssMRGR68QA2xiJsIWJapl0BLHI9f3WZ0AyKAAkDyUOIUa2RbkY6AdwhVkA";
    
    public String POSTtoFCM(String json) {
        try {
            URL page = new URL("https://fcm.googleapis.com/fcm/send");
            HttpsURLConnection connection = (HttpsURLConnection) page.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("accept", "application/json");
            connection.setRequestProperty("Authorization", "key="+FCM_KEY);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            OutputStream os = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

            writer.write(json);
            writer.flush();

            InputStream is = connection.getInputStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }
            is.close();
            baos.close();
            os.close();
            connection.disconnect();
            return new String(baos.toByteArray(), "UTF-8");
        }catch (IOException ex){
            ex.printStackTrace();
            return "";
        }
    }

    public String PUTrequest(String url, String json) {
        try {
            URL page = new URL(url);
            HttpsURLConnection connection = (HttpsURLConnection) page.openConnection();
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            OutputStream os = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

            writer.write(json);
            writer.flush();

            InputStream is = connection.getInputStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }
            is.close();
            baos.close();
            os.close();
            connection.disconnect();
            return new String(baos.toByteArray(), "UTF-8");
        }catch (IOException ex){
            ex.printStackTrace();
            return "";
        }

    }

    public String DELETErequest(String url) {
        try {
            URL page = new URL(url);
            HttpsURLConnection connection = (HttpsURLConnection) page.openConnection();
            connection.setRequestMethod("DELETE");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            InputStream is = connection.getInputStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = is.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }
            is.close();
            baos.close();
            connection.disconnect();

            return new String(baos.toByteArray(), "UTF-8");
        }catch (IOException ex){
            ex.printStackTrace();
            return "";
        }

    }

}
