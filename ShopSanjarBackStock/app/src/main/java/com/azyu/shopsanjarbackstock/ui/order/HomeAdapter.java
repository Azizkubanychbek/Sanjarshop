package com.azyu.shopsanjarbackstock.ui.order;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.azyu.shopsanjarbackstock.databinding.ItemOrderStockBinding;
import com.azyu.shopsanjarbackstock.models.Order;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {
    private ItemOrderStockBinding binding;
    private final Context context;
    private final ArrayList<Order> list;

    public HomeAdapter(Context context, ArrayList<Order> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemOrderStockBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemOrderStockBinding binding;

        public ViewHolder(@NonNull ItemOrderStockBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }

        public void onBind(Order order) {
            binding.nameUser.setText(order.getNameUser() != null ? order.getNameUser() : "");
            binding.nameProductCard.setText(order.getNameProduct() != null ? order.getNameProduct() : "");
            binding.priceCard.setText(order.getPriceProduct() != null ? order.getPriceProduct().toString() : "");
            binding.addressUser.setText(order.getAddressUser() != null ? order.getAddressUser() : "");
            binding.productsCounter.setText(String.valueOf(order.getProductCount()));
            Picasso.get().load(order.getImage()).into(binding.imageCard);
        }
    }
}
