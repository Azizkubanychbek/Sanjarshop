package com.azyu.shop.reg;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
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
import com.azyu.shop.databinding.FragmentRegisterBinding;
import com.azyu.shop.models.User;
import com.azyu.shop.remotedata.RetrofitClient;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;
    private NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.btnReg.setOnClickListener(v -> {
            binding.progressBar.setVisibility(View.VISIBLE);
            if (!isEmptyEditTextReg()) {
                registerToTable();
            } else {
                binding.progressBar.setVisibility(View.INVISIBLE);
            }
        });
        binding.tvLoginNow.setOnClickListener(v -> {
            navController = Navigation.findNavController(requireActivity(), R.id.nav_host);
            navController.navigate(R.id.action_navigation_register_to_navigation_login);
        });
    }

    private void registerToTable() {
        User newUser = new User(binding.nameUser.getText().toString(),
                binding.email.getText().toString(),
                binding.passwd.getText().toString());

        Call<User> userCall = RetrofitClient.getInstance().getApi().registrationNewUser(newUser);
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                binding.progressBar.setVisibility(View.INVISIBLE);
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(requireActivity(), "Registration was successful", Toast.LENGTH_SHORT).show();
                    SharedPreferences preferences = requireActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor prefLoginEdit = preferences.edit();
                    prefLoginEdit.putBoolean("registration", true);
                    prefLoginEdit.apply();
                } else {
                    Log.e("RegisterFragment", "Registration failed: " + response.code() + " " + response.message());
                    if (response.errorBody() != null) {
                        try {
                            Log.e("RegisterFragment", "Error body: " + response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    Toast.makeText(requireActivity(), "Registration is not available now", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable throwable) {
                binding.progressBar.setVisibility(View.INVISIBLE);
                Log.e("RegisterFragment", "Registration failed: " + throwable.getMessage());
                Toast.makeText(requireActivity(), "Registration failed: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private boolean isEmptyEditTextReg() {
        boolean isEmpty = binding.nameUser.getText().toString().isEmpty() ||
                binding.email.getText().toString().isEmpty() ||
                binding.passwd.getText().toString().isEmpty();

        if (isEmpty) {
            Toast toast = Toast.makeText(getActivity(), "Fill all fields to register", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
        return isEmpty;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
