package ar.com.nicolasgoux.bancoutn;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import ar.com.nicolasgoux.bancoutn.databinding.FragmentConstituirPlazoFijoBinding;
import ar.com.nicolasgoux.bancoutn.databinding.FragmentSimularPlazoFijoBinding;

public class SimularPlazoFijoFragment extends Fragment {

    // Variable para binding
    private FragmentSimularPlazoFijoBinding binding;
    // Variable para navHost
    private NavController navHost;

    public SimularPlazoFijoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSimularPlazoFijoBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Se obtiene el navHost
        navHost = NavHostFragment.findNavController(this);

        // Modificacion de action bar
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle("Simular Plazo Fijo");
        actionBar.setDisplayHomeAsUpEnabled(true);
    }
}