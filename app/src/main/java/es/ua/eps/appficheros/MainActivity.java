package es.ua.eps.appficheros;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import es.ua.eps.appficheros.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    boolean activeSD = false;
    boolean accessSD = false;
    private boolean externalStorage = false;
    private int locationType = 0;

    private EditText textSave;
    private Button estadoButton;
    private Button addButton;
    private Button verButton;
    private Button externalButton;
    private Button internalButton;
    private Button cerrarButton;

    private ActivityMainBinding viewBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());

        // Asignación de elementos de la interfaz a variables
        textSave = viewBinding.textSave;
        estadoButton = viewBinding.estado;
        addButton = viewBinding.add;
        verButton = viewBinding.ver;
        externalButton = viewBinding.external;
        internalButton = viewBinding.internal;
        cerrarButton = viewBinding.cerrar;

        // Deshabilitar el botón interno
        viewBinding.internal.setEnabled(false);

        // Configuración del botón de estado
        estadoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear una nueva actividad y pasar información adicional
                Intent intent = new Intent(MainActivity.this, EstadoAlmExterno.class);
                intent.putExtra("INFO_TYPE", 0);
                startActivity(intent);
            }
        });

        // Configuración del botón de agregar
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Llamar al método para guardar el texto
                saveText();
            }
        });

        // Configuración del botón de ver
        verButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear una nueva actividad y pasar información adicional
                Intent intent = new Intent(MainActivity.this, EstadoAlmExterno.class);
                intent.putExtra("INFO_TYPE", 1);
                intent.putExtra("LOCATION_TYPE", locationType);
                startActivity(intent);
            }
        });

        // Configuración del botón externo
        externalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Verificar los permisos de acceso y cambiar al almacenamiento externo
                if (accessPermission()) {
                    changeToExternal();
                }
            }
        });

        // Configuración del botón interno
        internalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cambiar al almacenamiento interno
                changeToInternal();
            }
        });

        // Configuración del botón cerrar
        cerrarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finalizar la actividad
                finish();
            }
        });
    }

    // Método onRestart, se ejecuta al reiniciar la actividad
    @Override
    protected void onRestart() {
        super.onRestart();
        // Verificar si se ha cambiado al almacenamiento externo y si se permite el acceso
        if (Build.VERSION.SDK_INT >= 30) {
            if (externalStorage) {
                externalStorage = false;
                if (Environment.isExternalStorageManager()) {
                    changeToExternal();
                }
            }
        }
    }

    // Método para guardar el texto en un archivo
    private void saveText() {
        // Obtener el estado del almacenamiento externo
        String estado = Environment.getExternalStorageState();

        // Verificar si el almacenamiento externo está montado y disponible para escritura
        if (estado.equals(Environment.MEDIA_MOUNTED)) {
            activeSD = true;
            accessSD = true;

            try {
                File sdPath = null;
                // Obtener la ruta del almacenamiento interno o externo según el tipo de ubicación seleccionada
                if (locationType == 0) {
                    sdPath = getFilesDir();
                } else {
                    sdPath = Environment.getExternalStorageDirectory();
                }

                // Crear un archivo en la ruta especificada y escribir el texto en él
                File file = new File(sdPath, "information.txt");
                OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file));
                writer.write(textSave.getText().toString());
                writer.flush();
                writer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (estado.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
            activeSD = true;
            accessSD = false;
        }
    }

    // Método para mover un archivo de un directorio a otro
    private void moveFile(File inDir, File outDir) {
        try {
            File inFile = new File(inDir, "information.txt");
            File outFile = new File(outDir, "information.txt");

            FileInputStream inStream = new FileInputStream(inFile);
            FileOutputStream outStream = new FileOutputStream(outFile);

            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }

            inStream.close();
            outStream.close();

            inFile.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método para cambiar al almacenamiento externo
    private void changeToExternal() {
        locationType = 1;
        internalButton.setEnabled(true);
        externalButton.setEnabled(false);
        moveFile(getFilesDir(), Environment.getExternalStorageDirectory());
    }

    // Método para cambiar al almacenamiento interno
    private void changeToInternal() {
        locationType = 0;
        internalButton.setEnabled(false);
        externalButton.setEnabled(true);
        moveFile(Environment.getExternalStorageDirectory(), getFilesDir());
    }

    // Método para verificar los permisos de acceso al almacenamiento externo
    private boolean accessPermission() {
        if (Build.VERSION.SDK_INT >= 30) {
            if (!Environment.isExternalStorageManager()) {
                Uri uri = Uri.parse("package:" + BuildConfig.APPLICATION_ID);
                startActivity(new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, uri));
                externalStorage = true;
                return false;
            } else {
                return true;
            }
        }
        return true;
    }
}
