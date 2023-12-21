package es.ua.eps.appficheros;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import es.ua.eps.appficheros.databinding.ActivityEstadoAlmExternoBinding;
import es.ua.eps.appficheros.databinding.ActivityFicheroBinding;

public class EstadoAlmExterno extends AppCompatActivity {

    private ActivityEstadoAlmExternoBinding viewBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = ActivityEstadoAlmExternoBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());

        viewBinding.volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EstadoAlmExterno.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}