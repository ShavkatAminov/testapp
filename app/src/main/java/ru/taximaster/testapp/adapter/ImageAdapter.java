package ru.taximaster.testapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import ru.taximaster.testapp.R;
import ru.taximaster.testapp.services.Httphelper;


public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.MyViewHolder> {

    private List<String> imageList;
    private Context context;
    Httphelper httphelper = new Httphelper();


    public ImageAdapter(List<String> imageList, Context context) {
        this.imageList = imageList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        httphelper.setImage(holder.imageView, imageList.get(position), holder.progressBar);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                View view = LayoutInflater.from(context).inflate(R.layout.reciever_progress_dialog, null);
                builder.setView(view);
                ImageView imageView = (ImageView) view.findViewById(R.id.imageView_full);
                ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBar2);
                AlertDialog dialog = builder.create();
                httphelper.setImage(imageView, imageList.get(position), progressBar);
                dialog.show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public CardView cardView;
        public ProgressBar progressBar;

        public MyViewHolder(View view) {
            super(view);
            cardView = (CardView) view.findViewById(R.id.card_view);
            imageView = (ImageView) view.findViewById(R.id.imageView);
            progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        }
    }
}
