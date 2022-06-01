package com.z.fileselectorlib.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.z.fileselectorlib.R;

import java.util.ArrayList;

public class NavigationAdapter extends RecyclerView.Adapter {
    private Context context;
    private ArrayList<String>RelativePathList;

    public NavigationAdapter(Context context, ArrayList<String> relativePathList) {
        this.context = context;
        RelativePathList = relativePathList;
    }
    public void UpdatePathList(ArrayList<String>relativePathList){
        RelativePathList=relativePathList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item_view = View.inflate(context, R.layout.navigation_item, null);
        return new ViewHolder(item_view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder= (ViewHolder) holder;
        viewHolder.path.setText(RelativePathList.get(position));
        viewHolder.setPosition(position);
        if (position==(getItemCount()-1))viewHolder.next.setVisibility(View.INVISIBLE);
        else viewHolder.next.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return RelativePathList.size();
    }

    private OnRecycleItemClickListener recycleItemClickListener;

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView path;
        private ImageView next;
        private int position;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            path = itemView.findViewById(R.id.navi_path);
            next=itemView.findViewById(R.id.next);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recycleItemClickListener.onClick(itemView,position);
                }
            });

        }

        public void setPosition(int position) {
            this.position = position;
        }
    }
    public void setRecycleItemClickListener(OnRecycleItemClickListener recycleItemClickListener) {
        this.recycleItemClickListener = recycleItemClickListener;
    }

    public interface OnRecycleItemClickListener {
        void onClick(View view, int position);
    }
}
