package com.example.tableobv.adapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tableobv.DbManager;
import com.example.tableobv.EditActivity;
import com.example.tableobv.MainActivity;
import com.example.tableobv.MyConstans;
import com.example.tableobv.NewPost;
import com.example.tableobv.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolderData> {
    private List<NewPost> arrayPost;
    private Context context;
    private OnItemClickCustom onItemClickCustom;
    private DbManager dbManager;

    public PostAdapter(List<NewPost> arrayPost, Context context, OnItemClickCustom onItemClickCustom) {
        this.arrayPost = arrayPost;
        this.context = context;
        this.onItemClickCustom = onItemClickCustom;
        this.dbManager = dbManager;
    }

    @NonNull
    @Override
    public ViewHolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_ads, parent, false);
        return new ViewHolderData(view, onItemClickCustom);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderData holder, int position) {
        holder.setData(arrayPost.get(position));
    }

    @Override
    public int getItemCount() {
        return arrayPost.size();
    }

    public class ViewHolderData extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private TextView TvPriceTel, TvDesc, TvTit;
        private ImageView imAds;
        private LinearLayout edit_layout;
        private ImageButton del_button, editButton;

        private OnItemClickCustom onItemClickCustom;


        public ViewHolderData(@NonNull View itemView, OnItemClickCustom onItemClickCustom ) {
            super(itemView);
            TvTit = itemView.findViewById(R.id.tvTitle);
            TvPriceTel = itemView.findViewById(R.id.tvPriceTel);
            TvDesc = itemView.findViewById(R.id.tvDesc);
            imAds = itemView.findViewById(R.id.imAds);
            edit_layout = itemView.findViewById(R.id.edit_layout);
            del_button = itemView.findViewById(R.id.imDelAds);
            editButton = itemView.findViewById(R.id.imEditAds);
            itemView.setOnClickListener(this);
            this.onItemClickCustom = onItemClickCustom;
        }
        public void setData(NewPost newPost)
        {
            if(newPost.getUid().equals(MainActivity.MAUTH))
            {
                edit_layout.setVisibility(View.VISIBLE);
            }
            else
            {
                edit_layout.setVisibility(View.GONE);
            }
            Picasso.get().load(newPost.getImageId()).into(imAds);
            TvTit.setText(newPost.getTitle());
            String price_tel = "Цена : " + newPost.getPrice() + " Тел : " + newPost.getTel();
            TvPriceTel.setText(price_tel);
            String textDesc = null;
            if(newPost.getDisc().length() > 50)
            {
                textDesc = newPost.getDisc().substring(0,50) + "...";
            }
            else
            {
                textDesc = newPost.getDisc();
            }

            TvDesc.setText(textDesc);

            del_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteDialog(newPost,getAdapterPosition());
                }
            });
            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    Intent i = new Intent(context, EditActivity.class);
                    i.putExtra(MyConstans.IMAGE_ID,newPost.getImageId());
                    i.putExtra(MyConstans.TITLE,newPost.getTitle());
                    i.putExtra(MyConstans.PRICE,newPost.getPrice());
                    i.putExtra(MyConstans.TEL,newPost.getTel());
                    i.putExtra(MyConstans.DESC,newPost.getDisc());
                    i.putExtra(MyConstans.KEY,newPost.getKey());
                    i.putExtra(MyConstans.UID,newPost.getUid());
                    i.putExtra(MyConstans.TIME,newPost.getTime());
                    i.putExtra(MyConstans.CAT,newPost.getCat());
                    i.putExtra(MyConstans.EDIT_STATE,true);
                    context.startActivity(i);
                }
            });
        }

        @Override
        public void onClick(View v) {
            onItemClickCustom.onItemSelected(getAdapterPosition());
        }
    }

    private void deleteDialog(NewPost newPost, int position)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.del);
        builder.setMessage(R.string.del_mes);
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

            }
        });

        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dbManager.deleteItem(newPost);
                arrayPost.remove(position);
                notifyItemRemoved(position);

            }
        });

        builder.show();
    }
    public interface OnItemClickCustom
    {
        void onItemSelected(int position);
    }

    public void updateAdapter(List<NewPost> listData)
    {
        arrayPost.clear();
        arrayPost.addAll(listData);
        notifyDataSetChanged();
    }
    public void setDbManager(DbManager dbManager)
    {
        this.dbManager = dbManager;
    }
}
