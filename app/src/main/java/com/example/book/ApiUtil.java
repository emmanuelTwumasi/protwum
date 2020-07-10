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
    private static final String TITLE= "intitle";
    private static final String AUTHOR = "inauthor";
    private static final String PUBLISHER = "inpublisher";
    private static final String ISBN = "inisbn";


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

    public static URL buildURL(String title, String author, String publisher, String isbn ){
        URL url =null;// null a start
        StringBuilder sb= new StringBuilder();
        if (!title.isEmpty() )sb.append(TITLE+ title +"+");
        if (!author.isEmpty() )sb.append(AUTHOR + author+"+");
        if (!publisher.isEmpty() )sb.append(PUBLISHER+publisher+"+");
        if (!isbn.isEmpty() )sb.append(ISBN+isbn+"+");
        sb.setLength(sb.length()-1);
        String query = sb.toString();
        //build uri
        Uri uri = Uri.parse(BASE_API_URL).buildUpon()
                .appendQueryParameter(QUERY_PARAMETER_KEY,query)
                .appendQueryParameter(KEY,API_KEY).build();
        //build url from uri created
        try{
                url = new URL(uri.toString());
    }
        catch(Exception e){
            e.printStackTrace();

        }
    return url;
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
        final String DESCRIPTION = "description";
        final String IMAGELINKS ="imageLinks";
        final String THUMBNAIL ="thumbnail";
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
                JSONObject imageLinksJSON = null;//return nothing for books without image in their json file
               if (volumeInfoJSON.has(IMAGELINKS) )
               {
                  imageLinksJSON = volumeInfoJSON.getJSONObject(IMAGELINKS);
               }
                int authorNum;
                //check whether there's author or not, if yes read but no returns 0
                try{
                    authorNum = volumeInfoJSON.getJSONArray(AUTHORS).length();
                }catch(Exception e){
                    authorNum =0;
                }
                String[] authors = new String[authorNum];
                for (int j=0; j<authorNum;j++) {
                    authors[j] = volumeInfoJSON.getJSONArray(AUTHORS).get(j).toString();
                }
                Book book = new Book(
                        bookJSON.getString(ID),
                        volumeInfoJSON.getString(TITLE),
                        (volumeInfoJSON.isNull(SUBTITLE)?"":volumeInfoJSON.getString(SUBTITLE)),
                        authors,
                        (volumeInfoJSON.isNull(PUBLISHER)?"":volumeInfoJSON.getString(PUBLISHER)),
                        (volumeInfoJSON.isNull(PUBLISHED_DATE)?"":volumeInfoJSON.getString(PUBLISHED_DATE)),
                        (volumeInfoJSON.isNull(DESCRIPTION)?"":volumeInfoJSON.getString(DESCRIPTION)),
                        ((imageLinksJSON==null)?"":imageLinksJSON.getString(THUMBNAIL)));
                        books.add(book);
            }
        }
        catch (JSONException e){
            e.printStackTrace();
        }

        return books;
    }

}
