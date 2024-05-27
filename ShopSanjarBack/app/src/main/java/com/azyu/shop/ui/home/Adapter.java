package com.azyu.shop.ui.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.azyu.shop.R;
import com.azyu.shop.databinding.ItemProductBinding;
import com.azyu.shop.models.ModelM;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    ItemProductBinding binding;
    Context context;
    List<ModelM> list;
    NavController navController;
    ArrayList<ModelM> setList;
    ArrayList<ModelM> selectedList = new ArrayList<>();
    ArrayList<ModelM> selectedIntoBasketList = new ArrayList<>();

    public Adapter(Context context, List<ModelM> list) {
        this.context = context;
        this.list = list;
        setList = new ArrayList<>();
    }
    public Adapter() {
        setList = new ArrayList<>();
    }

    public ArrayList<ModelM> getSelectedIntoBasketList() {
        return selectedIntoBasketList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemProductBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    @SuppressLint("NotifyDataSetChanged")
    public void setList(List<ModelM> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public class ViewHolder  extends RecyclerView.ViewHolder{
        ItemProductBinding binding;
        public ViewHolder(@NonNull ItemProductBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
        public void onBind(ModelM modelM) {
            binding.productNameCard.setText(modelM.getTitle());
            binding.priceCard.setText(String.valueOf(modelM.getPrice()));
            binding.descriptionCard.setText(modelM.getDescription());
            Picasso.get().load(modelM.getImage()).into(binding.imageCard);

            binding.btnZoom.setOnClickListener(v -> {
                selectedList.add(modelM);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("see more", selectedList);
                navController = Navigation.findNavController((Activity) itemView.getContext(), R.id.nav_host);
                navController.navigate(R.id.navigation_description, bundle);
                Log.e("TAG", "pass data ! !");
            });

            itemView.setOnClickListener(v1 -> {
                if (binding.itemFavCheck.getVisibility() == View.INVISIBLE) {
                    binding.itemFavCheck.setVisibility(View.VISIBLE);
                    selectedIntoBasketList.add(modelM);
                } else {
                    binding.itemFavCheck.setVisibility(View.INVISIBLE);
                    selectedIntoBasketList.remove(modelM);
                }
            });
        }
    }
}
