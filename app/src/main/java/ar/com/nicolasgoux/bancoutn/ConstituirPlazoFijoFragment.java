package ar.com.nicolasgoux.bancoutn;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import ar.com.nicolasgoux.bancoutn.databinding.FragmentConstituirPlazoFijoBinding;

public class ConstituirPlazoFijoFragment extends Fragment {

    // Variable para binding
    private FragmentConstituirPlazoFijoBinding binding;
    // Variable para navHost
    private NavController navHost;

    public ConstituirPlazoFijoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentConstituirPlazoFijoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Se obtiene el navHost
        navHost = NavHostFragment.findNavController(this);

        // Modificacion del actionBar
        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Constituir Plazo Fijo");
            actionBar.setDisplayHomeAsUpEnabled(false);
        }


        // Se restablece el estado de ser posible
        if (savedInstanceState != null) {
            binding.nameField.setText(savedInstanceState.getString("nombre"));
            binding.lastNameField.setText(savedInstanceState.getString("apellido"));
            binding.spinner.setSelection(savedInstanceState.getInt("spinerId"));
        }

        // Se deshabilita o habilita el boton constituir
        if (getArguments() != null) {
            Bundle bundleMoneda = getArguments().getBundle("bundleMoneda");
            binding.spinner.setSelection(bundleMoneda.getInt("idMoneda"));
            binding.constituirButton.setEnabled(true);
        } else {
            binding.constituirButton.setEnabled(false);
        }

        // Se añaden eventListener
        binding.simularButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle moneda = new Bundle();
                moneda.putInt("idMoneda", binding.spinner.getSelectedItemPosition());
                moneda.putString("nombreMoneda", binding.spinner.getSelectedItem().toString());


                Bundle args = new Bundle();
                args.putBundle("bundleMoneda", moneda);
                navHost.navigate(R.id.simularPlazoFijoFragment, args);
            }
        });

        binding.constituirButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.nameField.getText().length() == 0 || binding.lastNameField.getText().length() == 0) {
                    new AlertDialog.Builder(view.getContext())
                            .setCancelable(false).setTitle("Datos faltantes")
                            .setMessage("Ingrese su nombre y apellido en los campos correspondientes")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                } else {
                    if (getArguments() != null) {
                        new AlertDialog.Builder(view.getContext())
                                .setCancelable(false).setTitle("Felicitaciones " + binding.nameField.getText().toString() + " " + binding.lastNameField.getText().toString() + "!")
                                .setMessage("Tu plazo fijo de " + getArguments().getFloat("capitalInvertido") +
                                        " " + binding.spinner.getSelectedItem().toString().toLowerCase() +
                                        " por " + getArguments().getInt("plazoInversion") + " dias ha sido constituido")
                                .setPositiveButton("Piola!", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .show();
                    }
                }
            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (binding != null) {
            outState.putString("nombre", binding.nameField.getText().toString());
            outState.putString("apellido", binding.lastNameField.getText().toString());
            outState.putInt("spinerId", binding.spinner.getSelectedItemPosition());
        }

    }
}