package com.example.proyecto_integrador_2.ui.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.proyecto_integrador_2.R;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LoginFragment extends Fragment {

    private NavController navController;
    private LoginViewModel loginViewModel;
    private EditText editTextEmail;
    private EditText editTextPassword;

    public LoginFragment() {
        // Required empty public constructor

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_login, container, false);
        navController = NavHostFragment.findNavController(this);
        editTextEmail = (EditText) root.findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) root.findViewById(R.id.editTextPassword);
        Button loginButton = (Button) root.findViewById(R.id.buttonLogin);
        loginButton.setOnClickListener(this::login);
        Button registerButton = (Button) root.findViewById(R.id.buttonRegistrarse);
        registerButton.setOnClickListener(this::register);
        observe();
        return root;
    }

    public void observe() {
        loginViewModel.getEmailError().observe(getViewLifecycleOwner(), throwable -> {
            if (throwable != null) {
                editTextEmail.setError("Field can't be empty");
            }
        });
        loginViewModel.getPasswordError().observe(getViewLifecycleOwner(), throwable -> {
            if (throwable != null) {
                editTextPassword.setError("Field can't be empty");
            }
        });
        loginViewModel.getSignInError().observe(getViewLifecycleOwner(), throwable -> {
            if (throwable != null) {
                Toast.makeText(requireContext(), "Authentication failed.",
                        Toast.LENGTH_SHORT).show();
            }
        });
        loginViewModel.getGetUserError().observe(getViewLifecycleOwner(), throwable -> {
            if (throwable != null) {
                Toast.makeText(requireContext(), "Unable to get user info",
                        Toast.LENGTH_SHORT).show();
            }
        });
        loginViewModel.getSignInSuccessful().observe(getViewLifecycleOwner(), success -> {
            if (success) {
//                NavHostFragment navHostFragment = (NavHostFragment) SupportFra
                navController.navigate(LoginFragmentDirections.actionLoginToHome());
//                findNavController()LoginFragmentDirections.actionLoginToHome();
            }
        });
    }

    //Button login
    public void login(View v) {
        String emailInput = editTextEmail.getText().toString().trim();
        String passwordInput = editTextPassword.getText().toString().trim();
        loginViewModel.connect(emailInput, passwordInput);

    }

    //Button Fragment
    public void register(View v) {
        navController.navigate(LoginFragmentDirections.actionLoginToRegister());
    }
}