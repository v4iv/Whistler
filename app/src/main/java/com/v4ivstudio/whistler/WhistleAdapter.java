package com.v4ivstudio.whistler;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vaibhav Sharma on 5/9/2016.
 **/
public class WhistleAdapter extends RecyclerView.Adapter<WhistleAdapter.ViewHolder> {
    List<Whistle> whistles = new ArrayList<>();
    String whistleID;

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
        whistleID = "";
        whistleID += whistles.getId();
        holder.nameTextView.setText(whistles.getWhistler());
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
        holder.textView.setText(whistles.getStatement());
        if (whistles.isNsfw())
            holder.nsfwTextView.setText(R.string.nsfw);
        else
            holder.nsfwTextView.setText("");
        holder.timeTextView.setText(whistles.getPublished());
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
