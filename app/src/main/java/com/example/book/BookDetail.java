package com.example.book;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.book.databinding.ActivityBookDetailBinding;

public class BookDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityBookDetailBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_book_detail);
        Book book= getIntent().getParcelableExtra("Book");
        binding.setBook(book);
    }
}