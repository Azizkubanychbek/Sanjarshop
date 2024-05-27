package com.azyu.shop.ui.description;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.azyu.shop.R;
import com.azyu.shop.databinding.ItemDescriptionBinding;
import com.azyu.shop.models.ModelM;
import com.bumptech.glide.Glide;

import java.util.List;

public class DescAdapter extends RecyclerView.Adapter<DescAdapter.ViewHolder> {

    ItemDescriptionBinding binding;
    Context context;
    List<ModelM> listD;

    public DescAdapter(Context context, List<ModelM> listD) {
        this.context = context;
        this.listD = listD;
    }

    public void setListD(List<ModelM> listD) {
        this.listD = listD;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemDescriptionBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBind(listD.get(position));
    }

    @Override
    public int getItemCount() {
        return listD.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemDescriptionBinding binding;
        public ViewHolder(@NonNull ItemDescriptionBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        public void onBind(ModelM modelM) {
            binding.titleCard.setText(modelM.getTitle());
            binding.priceCard.setText(String.valueOf(modelM.getPrice()));
            binding.descriptionCard.setText(modelM.getDescription());
            Glide.with(itemView.getContext()).load(listD.get(getAdapterPosition()).getImage())
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.error)
                    .into(binding.imageCard);
        }
    }
}
