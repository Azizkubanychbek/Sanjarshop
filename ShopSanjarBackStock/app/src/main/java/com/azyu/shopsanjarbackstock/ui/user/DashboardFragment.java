package com.azyu.shopsanjarbackstock.ui.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.azyu.shopsanjarbackstock.databinding.FragmentDashboardBinding;
import com.azyu.shopsanjarbackstock.models.User;
import com.azyu.shopsanjarbackstock.remotedata.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    UserAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.rvMainListUsers.setLayoutManager(new LinearLayoutManager(getContext()));

        Call<List<User>> apiCall = RetrofitClient.getInstance().getApi().getAllUsers();
        apiCall.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if(response.isSuccessful() && response.body() != null){
                    ArrayList<User> list = (ArrayList<User>) response.body();
                    adapter = new UserAdapter(requireActivity(), list);
                    binding.rvMainListUsers.setAdapter(adapter);
                } else {
                    Toast.makeText(getContext(), "Failed to retrieve users", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable throwable) {
                Toast.makeText(requireActivity(), "Failed to fetch data", Toast.LENGTH_SHORT).show();
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