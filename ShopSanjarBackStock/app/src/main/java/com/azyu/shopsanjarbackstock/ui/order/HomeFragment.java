package com.azyu.shopsanjarbackstock.ui.order;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.azyu.shopsanjarbackstock.databinding.FragmentHomeBinding;
import com.azyu.shopsanjarbackstock.models.Order;
import com.azyu.shopsanjarbackstock.remotedata.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private HomeAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Call<List<Order>> apiCall = RetrofitClient.getInstance().getApi().getAllOrders();
        apiCall.enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ArrayList<Order> list = new ArrayList<>(response.body());
                    if (list.isEmpty()) {
                        Toast.makeText(requireActivity(), "No orders available", Toast.LENGTH_SHORT).show();
                    } else {
                        adapter = new HomeAdapter(requireActivity(), list);
                        binding.rvMainListOrders.setAdapter(adapter);
                    }
                } else {
                    Toast.makeText(requireActivity(), "Failed to get data", Toast.LENGTH_SHORT).show();
                    Log.e("API_RESPONSE_ERROR", "Response code: " + response.code() + ", message: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                Toast.makeText(requireActivity(), "Failed to fetch data", Toast.LENGTH_SHORT).show();
                Log.e("API_CALL_FAILURE", t.getMessage(), t);
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
