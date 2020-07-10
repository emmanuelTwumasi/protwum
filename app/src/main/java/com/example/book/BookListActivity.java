package com.example.book;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

public class BookListActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private TextView mTvError;
    private ProgressBar mLoadingProgress;
    private RecyclerView mRvBooks;
    private URL mBookUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLoadingProgress = (ProgressBar) findViewById(R.id.pb_loading);
        mRvBooks =(RecyclerView) findViewById(R.id.rv_books);
        Intent intent=getIntent();
        String query = intent.getStringExtra("Query");

        try {
            if (query==null||query.isEmpty()){
            //looks for books with the title
                mBookUrl = ApiUtil.buildUrl("java");
            }else {
                //create a url from the string passed
                mBookUrl = new URL(query);
            }
//            String jsonResult = ApiUtil.getJason(bookUrl);
            new BooksQueryTask().execute(mBookUrl);

        } catch (Exception e) {
            Log.d("error", e.getMessage());

        }
        //create the layoutManager for the books (linear in this case, scrolling vertically
        //set the layout manager for our recycler
        LinearLayoutManager booksLayoutManager =new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mRvBooks.setLayoutManager(booksLayoutManager);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.book_list_menu, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_advance_search:
                Intent intent = new Intent(this,SearchActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        try {
            URL bookUrl = ApiUtil.buildUrl(query);
            new BooksQueryTask().execute(bookUrl);
        }
        catch (Exception e) {
            Log.d("error", e.getMessage());
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
    //asyncTask
    public class BooksQueryTask extends AsyncTask<URL,Void,String>{

        @Override
        protected String doInBackground(URL... urls) {
            URL searchURL = urls[0];
            String result = null;

            try {
                result = ApiUtil.getJson(searchURL);
            }
            catch (IOException e) {
                Log.d("error", Objects.requireNonNull(e.getMessage()));
            }
            return result;
        }

        //after execution
        @Override
        protected void onPostExecute(String result) {

            mTvError = findViewById(R.id.tv_error);
            mLoadingProgress.setVisibility(View.INVISIBLE);

            if (result == null){
                mRvBooks.setVisibility(View.INVISIBLE);
                mTvError.setVisibility(View.VISIBLE);
            }else{
                mRvBooks.setVisibility(View.VISIBLE);

                ArrayList<Book> books = ApiUtil.getBooksFromJson(result);
                String resultString = "";
//            for (Book book:books){
//                resultString =resultString + book.title+"\n"+book.publishedDate+"\n\n";
//            }

                BooksAdapter adapter = new BooksAdapter(books);
                mRvBooks.setAdapter(adapter);
            }

        }

        //before execution set progress bar to visible
        protected  void onPreExecute(){
            super.onPreExecute();
            mLoadingProgress.setVisibility(View.VISIBLE);
        }
    }
}