package com.azyu.shop.ui.payment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.azyu.shop.R;
import com.azyu.shop.databinding.FragmentPaymentBinding;
import com.azyu.shop.models.Order;
import com.azyu.shop.remotedata.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentFragment extends Fragment {

    private FragmentPaymentBinding binding;
    private NavController navController;
    private List<Order> payedList;
    private SharedPreferences preferences;

    public PaymentFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPaymentBinding.inflate(inflater, container, false);
        preferences = requireActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE);

        if (getArguments() != null) {
            payedList = getArguments().getParcelableArrayList("payed");
        }

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host);

        binding.btnBack.setOnClickListener(v1 -> navController.navigate(R.id.action_navigation_payment_to_navigation_home));

        binding.cardMbank.setOnClickListener(v2 -> openApp("mbank-app-scheme://"));
        binding.paypal.setOnClickListener(v3 -> openApp("paypal://"));
        binding.visaMastercard.setOnClickListener(v4 -> openApp("visa-app-scheme://"));
        binding.o.setOnClickListener(v5 -> openApp("dengi-app-scheme://"));

        binding.btnFinallyPay.setOnClickListener(v7 -> {
            binding.progressBar.setVisibility(View.VISIBLE);
            if (payedList != null && !payedList.isEmpty()) {
                for (Order order : payedList) {
                    processOrder(order);
                }
            } else {
                binding.progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(requireActivity(), "Items were not chosen", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openApp(String uriScheme) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uriScheme));
        if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store"));
            startActivity(webIntent);
        }
    }

    private void processOrder(Order order) {
        Call<Order> apiCall = RetrofitClient.getInstance().getApi().createNewOrder(order);
        apiCall.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                binding.progressBar.setVisibility(View.INVISIBLE);
                if (response.isSuccessful() && response.body() != null) {
                    SharedPreferences.Editor prefPayed = preferences.edit();
                    prefPayed.putBoolean("Order", true);
                    prefPayed.apply();
                    binding.tvAnswer.setText("Your order is accepted. Delivery takes from 5 to 12 days");
                    Toast.makeText(requireActivity(), "Order placed successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(requireActivity(), "Order is not available now", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Order> call, Throwable throwable) {
                binding.progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(requireActivity(), "Failed to place order", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
