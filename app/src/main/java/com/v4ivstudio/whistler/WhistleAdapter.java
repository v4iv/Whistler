package com.v4ivstudio.whistler;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Vaibhav Sharma on 5/9/2016.
 **/
public class WhistleAdapter extends RecyclerView.Adapter<WhistleAdapter.ViewHolder> {
    List<Whistle> whistles = new ArrayList<>();
    String whistleID, published;
    Date cur_date, pub_date;

    public WhistleAdapter(List<Whistle> whistles) {
        this.whistles = whistles;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_whistles, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Whistle whistles = this.whistles.get(position);
        // Whistle ID
        whistleID = "";
        whistleID += whistles.getId();

        // Whistler
        holder.nameTextView.setText(whistles.getWhistler());

        // Category
        String[] type = {"Confession", "Personal", "Family", "School", "Work", "Sexual", "Military", "Food", "Sports"};
        if (whistles.getCategory().equals("CNF"))
            holder.sectionTextView.setText(type[0].toUpperCase());
        else if (whistles.getCategory().equals("PSN"))
            holder.sectionTextView.setText(type[1].toUpperCase());
        else if (whistles.getCategory().equals("FAM"))
            holder.sectionTextView.setText(type[2].toUpperCase());
        else if (whistles.getCategory().equals("SCL"))
            holder.sectionTextView.setText(type[3].toUpperCase());
        else if (whistles.getCategory().equals("WRK"))
            holder.sectionTextView.setText(type[4].toUpperCase());
        else if (whistles.getCategory().equals("SEX"))
            holder.sectionTextView.setText(type[5].toUpperCase());
        else if (whistles.getCategory().equals("MIL"))
            holder.sectionTextView.setText(type[6].toUpperCase());
        else if (whistles.getCategory().equals("FUD"))
            holder.sectionTextView.setText(type[7].toUpperCase());
        else if (whistles.getCategory().equals("SPT"))
            holder.sectionTextView.setText(type[8].toUpperCase());

        // NSFW
        holder.textView.setText(whistles.getStatement());
        if (whistles.isNsfw())
            holder.nsfwTextView.setText(R.string.nsfw);
        else
            holder.nsfwTextView.setText("");

        // Published Date
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        SimpleDateFormat pubDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        pubDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        try {
            pub_date = simpleDateFormat.parse(whistles.getPublished());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        published = pubDateFormat.format(pub_date);
        holder.timeTextView.setText(published);
    }

    @Override
    public int getItemCount() {
        return whistles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView nameTextView;
        private TextView sectionTextView;
        private TextView textView;
        private TextView nsfwTextView;
        private TextView timeTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTextView = (TextView) itemView.findViewById(R.id.nameView);
            sectionTextView = (TextView) itemView.findViewById(R.id.typeView);
            textView = (TextView) itemView.findViewById(R.id.textView);
            nsfwTextView = (TextView) itemView.findViewById(R.id.nsfwView);
            timeTextView = (TextView) itemView.findViewById(R.id.timeView);
        }

    }
}
