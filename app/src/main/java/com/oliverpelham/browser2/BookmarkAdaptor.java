package com.oliverpelham.browser2;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.oliverpelham.browser2.data.Bookmark;

import java.util.List;

public class BookmarkAdaptor extends BaseAdapter {

    private List<Bookmark> itemList;
    private LayoutInflater inflater;

    public BookmarkAdaptor(Activity context, List<Bookmark> itemList){
        super();
        this.itemList = itemList;
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder {
        TextView textViewTitle;
        TextView textViewSubtitle;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if(convertView == null){
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.bookmark_item, null);

            holder.textViewTitle = (TextView) convertView.findViewById(R.id.bookmark_view_title);
            holder.textViewSubtitle = (TextView) convertView.findViewById(R.id.bookmark_view_subtitle);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Bookmark bookmark = (Bookmark) itemList.get(position);
        holder.textViewTitle.setText(bookmark.getTitle());
        holder.textViewSubtitle.setText(bookmark.getUrl());

        return convertView;
    }
}
