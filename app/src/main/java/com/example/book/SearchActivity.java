package com.example.book;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.net.URL;

public class SearchActivity extends AppCompatActivity {

    private Button mButton;
    private EditText mEtTitle;
    private EditText mEtAuthor;
    private EditText mEtPublisher;
    private EditText mEtIsbn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        mButton = findViewById(R.id.btnSearch);
        mEtTitle = findViewById(R.id.etTitle);
        mEtAuthor = findViewById(R.id.etAuthor);
        mEtPublisher = findViewById(R.id.etPublisher);
        mEtIsbn = findViewById(R.id.etISBN);
     mButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             String title = mEtTitle.getText().toString().trim();
             String author =mEtAuthor.getText().toString().trim();
             String publisher =mEtPublisher.getText().toString().trim();
             String isbn =mEtIsbn.getText().toString().trim();

             if(title.isEmpty() && author.isEmpty() && publisher.isEmpty() && isbn.isEmpty())
             {
                     String message = getString(R.string.no_search_data);
                 Toast.makeText(SearchActivity.this, message, Toast.LENGTH_SHORT).show();
             }else {

                 URL queryURL = ApiUtil.buildURL(title,author,publisher,isbn);
                 Intent intent =new Intent(getApplicationContext(),BookListActivity.class);
                 intent.putExtra("Query",queryURL);
                 startActivity(intent);
             }
         }
     });
    }
}