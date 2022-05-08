package com.example.disasteye;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;


import java.util.ArrayList;

public class News extends ArrayAdapter<String> {
    Context context;
    ArrayList<String> news_headline  = new ArrayList<>();
    ArrayList<String> news_link = new ArrayList<>();

    public News(Context context, ArrayList<String> news_headline, ArrayList<String> news_link) {
        super(context, R.layout.listviewtextcolor, R.id.newTitle, news_headline);
        this.context = context;
        this.news_headline = news_headline;
        this.news_link = news_link;
    }



    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Assign the convertView in a View object
        View singleItem = convertView;
        ViewHolder holder = null;
        if(singleItem == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            singleItem = layoutInflater.inflate(R.layout.listviewitem, parent, false);
            holder = new ViewHolder(singleItem);
            singleItem.setTag(holder);
        }

        else{

            holder = (ViewHolder) singleItem.getTag();
        }

        holder.newsTitle.setText(news_headline.get(position));
        singleItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getContext(), "You clicked:"+ programName[position], Toast.LENGTH_SHORT).show();
                Intent openLinksIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(news_link.get(position)));
                context.startActivity(openLinksIntent);
            }
        });
        return singleItem;
    }
}