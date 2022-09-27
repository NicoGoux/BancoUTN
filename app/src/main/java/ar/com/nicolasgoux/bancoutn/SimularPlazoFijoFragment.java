package ar.com.nicolasgoux.bancoutn;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

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
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSimularPlazoFijoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Se obtiene el navHost
        navHost = NavHostFragment.findNavController(this);

        // Modificacion de action bar
        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Simular Plazo Fijo");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // se desactiva el boton de confirmar
        binding.confirmarButton.setEnabled(false);

        // Se restablece el estado de ser posible
        if (savedInstanceState != null) {
            binding.tasaNominalField.setText(savedInstanceState.getString("tasaNominal"));
            binding.tasaEfectivaField.setText(savedInstanceState.getString("tasaEfectiva"));
            binding.capitalInvertirField.setText(savedInstanceState.getString("capitalInvertir"));
            binding.seekBar.setProgress(savedInstanceState.getInt("seekbar"));
            binding.checkBox.setChecked(savedInstanceState.getBoolean("checkbox"));
            this.calcular();
        }

        // se modifica el titulo de resultaod segun la moneda utilizada
        if (getArguments() != null) {
            String tituloResultado = binding.tituloResultado.getText().toString();
            Bundle bundleMoneda = getArguments().getBundle("bundleMoneda");
            String moneda = bundleMoneda.getString("nombreMoneda");
            moneda = moneda.substring(0, 1) + moneda.substring(1).toLowerCase();
            binding.tituloResultado.setText(tituloResultado + " en " + moneda);
        }

        // Se agregan Event Listener
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                calcular();
            }
        };

        binding.tasaNominalField.addTextChangedListener(textWatcher);
        binding.tasaEfectivaField.addTextChangedListener(textWatcher);
        binding.capitalInvertirField.addTextChangedListener(textWatcher);

        binding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                binding.diasPlazoFijo.setText(seekBar.getProgress() * 30 + " dias");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                calcular();
            }
        });

        binding.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                calcular();
            }
        });

        binding.confirmarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle args = new Bundle();
                args.putFloat("capitalInvertido", Float.parseFloat(binding.capitalInvertirField.getText().toString()));
                args.putInt("plazoInversion", binding.seekBar.getProgress() * 30);
                args.putBundle("bundleMoneda", getArguments().getBundle("bundleMoneda"));
                navHost.navigate(R.id.constituirPlazoFijoFragment, args);
            }
        });
    }

    private void calcular() {
        if (
                binding.tasaNominalField.getText().toString().equals("") ||
                        binding.tasaEfectivaField.getText().toString().equals("") ||
                        binding.capitalInvertirField.getText().toString().equals("") ||
                        binding.seekBar.getProgress() == 0
        ) {
            binding.confirmarButton.setEnabled(false);
            return;
        }
        int plazo = binding.seekBar.getProgress(); // plazo en meses
        float capital = Float.parseFloat(binding.capitalInvertirField.getText().toString());
        float tasaEfectivaAnual = Float.parseFloat(binding.tasaEfectivaField.getText().toString());
        float tasaNominalAnual = Float.parseFloat(binding.tasaNominalField.getText().toString());
        float interesMensualGanado = ((tasaNominalAnual / 12) / 100) * capital;
        float interesGanadoEnPlazo = (interesMensualGanado) * plazo;
        float montoTotal = capital + interesGanadoEnPlazo;
        float montoTotalAnual;
        if (binding.checkBox.isChecked() && plazo < 12) {
            montoTotalAnual = capital + (tasaEfectivaAnual / 100) * capital;
        } else {
            montoTotalAnual = capital + (tasaNominalAnual / 100) * capital;
        }

        // Seteo de textos
        binding.plazoResultado.setText("Plazo: " + plazo * 30 + " dias");
        binding.capitalResultado.setText("Capital: $" + Math.round(capital * 1000f) / 1000f);
        binding.interesesResultado.setText("Intereses ganados: $" + Math.round(interesGanadoEnPlazo * 1000f) / 1000f);
        binding.montoTotalResultado.setText("Monto total: $" + Math.round(montoTotal * 1000f) / 1000f);
        binding.montoAnualResultado.setText("Monto total anual: $" + Math.round(montoTotalAnual * 1000f) / 1000f);
        binding.confirmarButton.setEnabled(true);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (binding != null) {
            outState.putString("tasaNominal", binding.tasaNominalField.getText().toString());
            outState.putString("tasaEfectiva", binding.tasaEfectivaField.getText().toString());
            outState.putString("capitalInvertir", binding.capitalInvertirField.getText().toString());
            outState.putInt("seekbar", binding.seekBar.getProgress());
            outState.putBoolean("checkbox", binding.checkBox.isChecked());
        }
    }
}