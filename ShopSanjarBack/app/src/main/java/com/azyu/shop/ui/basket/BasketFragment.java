package com.azyu.shop.ui.basket;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.azyu.shop.R;
import com.azyu.shop.databinding.FragmentBasketBinding;
import com.azyu.shop.models.ModelM;
import com.azyu.shop.models.Order;

import java.util.ArrayList;

public class BasketFragment extends Fragment {

    private FragmentBasketBinding binding;
    private ArrayList<ModelM> basketProducts;
    private BasketAdapter basketAdapter;
    private NavController navController;
    private String userAddress, userName;
    private ArrayList<Order> orders;
    private String tokenN;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentBasketBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        if (getArguments() != null) {
            basketProducts = getArguments().getParcelableArrayList("basket");
            tokenN = getArguments().getString("key_token");
        }

        if (basketProducts != null && !basketProducts.isEmpty()) {
            binding.placeHolder.setVisibility(View.GONE);
            basketAdapter = new BasketAdapter(requireActivity(), basketProducts);
            binding.rvBasket.setAdapter(basketAdapter);
        } else {
            binding.placeHolder.setVisibility(View.VISIBLE);
            binding.btnMakeOrder.setVisibility(View.GONE);
        }

        setUpOnBackPressed();
        return root;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnMakeOrder.setOnClickListener(v -> {
            if (!binding.editUserAddress.getText().toString().isEmpty() &&
                    !binding.editUserName.getText().toString().isEmpty() &&
                    !binding.placeCounter.getText().toString().isEmpty()) {
                userAddress = binding.editUserAddress.getText().toString();
                userName = binding.editUserName.getText().toString();
                int counterProd = Integer.parseInt(binding.placeCounter.getText().toString());
                orders = new ArrayList<>();
                for (ModelM product : basketProducts) {
                    orders.add(new Order(
                            userName,
                            userAddress,
                            product.getImage(),
                            product.getTitle(),
                            String.valueOf(product.getPrice()),
                            counterProd
                    ));
                }
                binding.tvAnswer.setText("Your order is in processing, delivery number is 154.");
                binding.btnMakeOrder.setVisibility(View.INVISIBLE);
                binding.btnPay.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(requireActivity(), "Enter your data", Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnPay.setOnClickListener(v -> {
            Bundle bundleOrders = new Bundle();
            bundleOrders.putParcelableArrayList("payed", orders);
            navController = Navigation.findNavController(requireActivity(), R.id.nav_host);
            navController.navigate(R.id.action_navigation_basket_to_navigation_payment, bundleOrders);
        });

        binding.btnBack.setOnClickListener(v -> {
            navController = Navigation.findNavController(requireActivity(), R.id.nav_host);
            navController.navigate(R.id.action_navigation_basket_to_navigation_home);
        });
    }

    private void setUpOnBackPressed() {
        requireActivity().getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (isEnabled()) {
                    requireActivity().finish();
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
