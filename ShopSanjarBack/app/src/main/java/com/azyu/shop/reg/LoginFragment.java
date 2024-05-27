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
import com.azyu.shop.databinding.FragmentLoginBinding;
import com.azyu.shop.models.CurrentUser;
import com.azyu.shop.models.LoginResponse;
import com.azyu.shop.remotedata.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment {

    FragmentLoginBinding binding;
    NavController navController;
    String emailUserIdentify;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.btnLogin.setOnClickListener(v -> {
            binding.progressBar.setVisibility(View.VISIBLE);
            if(!IsEmptyEditTextLogin()){
                loginUser(new CurrentUser(binding.email.getText().toString(),
                        binding.passwd.getText().toString()));
            }
        });

        navController = Navigation.findNavController(requireActivity(), R.id.nav_host);

        binding.btnReg.setOnClickListener(v1->{
            navController.navigate(R.id.action_navigation_login_to_navigation_register);
        });
    }

    private void loginUser(CurrentUser currentUser) {
        try {
            Call<LoginResponse> response = RetrofitClient.getInstance().getApi().checkLoginUser(currentUser);
            response.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        String tokenN = response.body().getAccessToken();

                        saveUserCredentials(tokenN, binding.email.getText().toString());

                        Bundle bundle = new Bundle();
                        bundle.putString("identify", binding.email.getText().toString());
                        navController.navigate(R.id.action_navigation_login_to_navigation_home, bundle);
                    }
                }
                @Override
                public void onFailure(Call<LoginResponse> call, Throwable throwable) {
                    Log.e("API",throwable.toString());
                }
            });
        } catch (Exception e) {
            Log.d("API",e.toString());
            Toast.makeText(requireActivity(),e.toString(),Toast.LENGTH_SHORT).show();
        }
    }

    private void saveUserCredentials(String token, String email) {
        try {
            SharedPreferences preferences = getActivity()
                    .getSharedPreferences("prefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor prefLoginEdit = preferences.edit();
            prefLoginEdit.putBoolean("loggedin", true);
            prefLoginEdit.putString("token", token);
            prefLoginEdit.putString("email", email);
            prefLoginEdit.apply();
        } catch (Exception e) {
            Log.d("API", String.format("Token  error: %s", e.toString()));
        }
    }


    private boolean IsEmptyEditTextLogin(){
        if(binding.email.getText().toString().isEmpty() ||
                binding.passwd.getText().toString().isEmpty()){
            Toast toast=Toast.makeText(getActivity(),
                    "Fill all fields to login",Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
            return true;
        }else {
            return false;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}