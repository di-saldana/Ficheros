package es.ua.eps.appficheros;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import es.ua.eps.appficheros.databinding.ActivityFicheroBinding;
import es.ua.eps.appficheros.databinding.ActivityMainBinding;

public class Fichero extends AppCompatActivity {

    private ActivityFicheroBinding viewBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = ActivityFicheroBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());

        viewBinding.volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Fichero.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}