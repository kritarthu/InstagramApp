package com.walmart.instatrend;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class PostAdapter extends ArrayAdapter<PostBean> {
    public PostAdapter(Context context, List<PostBean> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        PostBean post = getItem(position);
        if(convertView==null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_post, parent, false);
        }
        TextView caption = (TextView)convertView.findViewById(R.id.tvCaption);
        TextView user = (TextView)convertView.findViewById(R.id.tvUser);
        ImageView image = (ImageView)convertView.findViewById(R.id.ivImage);


        caption.setText(post.caption);
        user.setText(post.username);
        image.setImageResource(0);
        Picasso.with(getContext()).load(post.imageUrl).error(R.drawable.progress_animation).placeholder(R.drawable.progress_animation).into(image);
        return convertView;
    }
}
