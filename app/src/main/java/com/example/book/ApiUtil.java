package com.example.book;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class ApiUtil {


    private ApiUtil(){}
    private static final String API_KEY = "AIzaSyBBtOOS8_on5IBO3LS26KhUIw56Wfp8Kb4" ;
    private static HttpURLConnection sConnection;
    private static Scanner sScanner;
    private static final String QUERY_PARAMETER_KEY = "q";
    private static final String KEY = "key";


    public static final String BASE_API_URL = "https://www.googleapis.com/books/v1/volumes";


    //create the url
    public static URL buildUrl(String title){
//        String fullUrl = BASE_API_URL + "?q=" + title;

        URL url = null;
        Uri uri = Uri.parse(BASE_API_URL).buildUpon()
                .appendQueryParameter(QUERY_PARAMETER_KEY,title)
                .appendQueryParameter(KEY,API_KEY).build();
        try {
             url = new URL(uri.toString());
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return  url;
    }


    //connects to the google api
    public static String getJson(URL url)throws IOException {
        //reads data
        sConnection = (HttpURLConnection) url.openConnection();

        try {
            InputStream stream = sConnection.getInputStream();//allows to read any data whether pdf or not
            //scanner has advantage of buffering data to android utf format
            sScanner = new Scanner(stream);

            sScanner.useDelimiter("\\A");// backslash A is a pattern\\means we read everything from the input data

            boolean hasData = sScanner.hasNext();
            if (hasData){
                return sScanner.next();
            }else {
                return null;
            }
        }
        catch (Exception e) {
            Log.d("Error",e.toString());
                return null;
        }
        finally {
            sConnection.disconnect();
        }

    }

    public static ArrayList<Book> getBooksFromJson(String json){
        final String ID ="id";
        final String TITLE = "title";
        final String SUBTITLE = "subtitle";
        final String AUTHORS = "authors";
        final String PUBLISHER ="publisher";
        final String PUBLISHED_DATE ="publishedDate";
        final String ITEMS = "items";
        final String VOLUMEINFO = "volumeInfo";

        ArrayList<Book> books =new ArrayList<Book>();
        try {
            JSONObject jsonBooks = new JSONObject(json);
            JSONArray arrayBooks = jsonBooks.getJSONArray(ITEMS);

            //fetch no. of books
            int numberOfBooks = arrayBooks.length();

            for (int i =0; i<numberOfBooks;i++){
                JSONObject bookJSON = arrayBooks.getJSONObject(i);
                JSONObject volumeInfoJSON =
                        bookJSON.getJSONObject(VOLUMEINFO);

                int authorNum = volumeInfoJSON.getJSONArray(AUTHORS).length();
                String[] authors = new String[authorNum];
                for (int j=0; j<authorNum;j++) {
                    authors[j] = volumeInfoJSON.getJSONArray(AUTHORS).get(j).toString();
                }
                Book book = new Book(
                        bookJSON.getString(ID),
                        volumeInfoJSON.getString(TITLE),
                        (volumeInfoJSON.isNull(SUBTITLE)?"":volumeInfoJSON.getString(SUBTITLE)),
                        authors,
                        volumeInfoJSON.getString(PUBLISHER),
                        volumeInfoJSON.getString(PUBLISHED_DATE));

                        books.add(book);

            }
        }
        catch (JSONException e){
            e.printStackTrace();
        }

        return books;
    }
}
