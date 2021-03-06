package com.example.book;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BooksAdapter extends RecyclerView.Adapter< BooksAdapter.BookViewHolder>{

    ArrayList<Book> books;
    public BooksAdapter(ArrayList<Book> books){
        this.books = books;
    }

    //called when a new viewHolder is required and it inflates the layout to be use
    @Override
    public BookViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.book_list_item,parent,false);

        return new BookViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BookViewHolder holder, int position) {
        Book book = books.get(position);
        holder.bind(book);
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    //define views and bind data
    public class BookViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tvTitle;
        TextView tvAuthors;
        TextView tvDate;
        TextView tvPublisher;
        public BookViewHolder( View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvAuthors= (TextView) itemView.findViewById(R.id.tvAuthors);
            tvDate = (TextView) itemView.findViewById(R.id.tvPublishedDate);
            tvPublisher =(TextView) itemView.findViewById(R.id.tvPublisher);
            itemView.setOnClickListener(this);
        }
        public void bind (Book book){
            tvTitle.setText(book.title);

            //populates the author space in an unbind format
//            int i = 0;
////            for (String author:book.authors){
////                authors +=author;
////                i++;
////                if (i<book.authors.length){
////                    authors+=", ";
////                }
////            }
            //data binding will require for authors string call
            String authors ="";
            tvAuthors.setText(book.authors);
            tvDate.setText(book.publishedDate);
            tvPublisher.setText(book.publisher);
        }

        @Override
        public void onClick(View v) {

            //get position of book clicked on
            int Position = getAdapterPosition();
            Log.d("Click", String.valueOf(Position));
            Book selectedBook = books.get(Position);

            Intent intent = new Intent(v.getContext(),BookDetail.class);
            intent.putExtra("Book",selectedBook);
            v.getContext().startActivity(intent);
        }
    }
}
