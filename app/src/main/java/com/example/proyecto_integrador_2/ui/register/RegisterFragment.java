package com.example.proyecto_integrador_2.ui.register;

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
public class RegisterFragment extends Fragment {

    private NavController navController;
    private RegisterViewModel registerViewModel;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextName;
    private EditText editTextPhone;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_register, container, false);
        navController = NavHostFragment.findNavController(this);
        editTextEmail = (EditText) root.findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) root.findViewById(R.id.editTextPassword);
        editTextName = (EditText) root.findViewById(R.id.editTextName);
        editTextPhone = (EditText) root.findViewById(R.id.editTextPhone);
        Button registerButton = (Button) root.findViewById(R.id.buttonRegistrarse);
        registerButton.setOnClickListener(this::register);

        observe();

        return root;
    }

    public void observe() {
        registerViewModel.getEmailError().observe(getViewLifecycleOwner(), throwable -> {
            if (throwable != null) {
                editTextEmail.setError("Field can't be empty");
            }
        });
        registerViewModel.getPasswordError().observe(getViewLifecycleOwner(), throwable -> {
            if (throwable != null) {
                editTextPassword.setError("Field can't be empty");
            }
        });
        registerViewModel.getNameError().observe(getViewLifecycleOwner(), throwable -> {
            if (throwable != null) {
                editTextName.setError("Field can't be empty");
            }
        });
        registerViewModel.getPhoneError().observe(getViewLifecycleOwner(), throwable -> {
            if (throwable != null) {
                editTextPhone.setError("Field can't be empty");
            }
        });
        registerViewModel.getRegisterError().observe(getViewLifecycleOwner(), throwable -> {
            if (throwable != null) {
                Toast.makeText(requireContext(), "Registration failed.",
                        Toast.LENGTH_SHORT).show();
            }
        });
        registerViewModel.getRegisterSuccessful().observe(getViewLifecycleOwner(), success -> {
            if (success) {
                navController.navigate(RegisterFragmentDirections.actionRegisterToHome());
            }
        });
    }

    //Button register
    public void register(View v) {
        String emailInput = editTextEmail.getText().toString().trim();
        String passwordInput = editTextPassword.getText().toString().trim();
        String nameInput = editTextName.getText().toString().trim();
        String phoneInput = editTextPhone.getText().toString().trim();
        registerViewModel.register(emailInput, passwordInput, nameInput, phoneInput);

    }
}