package com.example.priyanka.mapsdemo;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;



public class DownloadURL {

    public String readUrl(String myUrl) throws IOException
    {
        String data = "";
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;
        String line = "";
        //변수선언

        try {
            URL url = new URL(myUrl);
            urlConnection=(HttpURLConnection) url.openConnection();
            urlConnection.connect();
            //URLConnection 작업

            inputStream = urlConnection.getInputStream();
            //inputStream 으로 읽어오고
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            //BufferedReader로 변환
            StringBuffer sb = new StringBuffer();




            while((line = br.readLine()) != null)
            {

                sb.append(line);
            }

            data = sb.toString();
            // StringBuffer 로 읽어 들인걸 스트링으로 변환

            br.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            inputStream.close();
            urlConnection.disconnect();
        }



        return data;

    }
}
