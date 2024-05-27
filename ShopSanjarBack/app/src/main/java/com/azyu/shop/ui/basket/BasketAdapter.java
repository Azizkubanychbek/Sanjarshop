package com.azyu.shop.ui.basket;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.azyu.shop.R;
import com.azyu.shop.databinding.ItemOrderBinding;
import com.azyu.shop.models.ModelM;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BasketAdapter extends RecyclerView.Adapter<BasketAdapter.ViewHolder> {
    ItemOrderBinding binding;
    Context context;
    ArrayList<ModelM> nnList;

    public BasketAdapter(Context context, ArrayList<ModelM> nnList) {
        this.context = context;
        this.nnList = nnList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemOrderBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBind(nnList.get(position));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemOrderBinding binding;

        public ViewHolder(@NonNull ItemOrderBinding itemView) {
            super(itemView.getRoot());
            this.binding  = itemView;
        }

        public void onBind(ModelM modelM) {
            binding.productNameCard.setText(modelM.getTitle());
            binding.priceCard.setText(String.valueOf(modelM.getPrice()));

            Picasso.get()
                    .load(nnList.get(getAdapterPosition()).getImage())
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.ic_launcher_background)
                    .into(binding.imageCard);
        }
    }

    @Override
    public int getItemCount() {
        return nnList.size();
    }
}
