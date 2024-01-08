package es.ua.eps.appficheros;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import es.ua.eps.appficheros.databinding.ActivityEstadoAlmExternoBinding;

public class EstadoAlmExterno extends AppCompatActivity {
    final String INFO_TYPE = "INFO_TYPE";
    final String LOCATION_TYPE = "LOCATION_TYPE";

    TextView info;
    Button volverButton;

    boolean externalStorage = false;

    private ActivityEstadoAlmExternoBinding viewBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = ActivityEstadoAlmExternoBinding.inflate(getLayoutInflater());
        setContentView(viewBinding.getRoot());

        volverButton = viewBinding.volver;
        info = viewBinding.textView;

        // Obtener el estado del almacenamiento externo y mostrarlo en el TextView
        String estado = getText();
        info.setText(estado);

        // Configurar el botón de volver para regresar a la actividad principal
        viewBinding.volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EstadoAlmExterno.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(Build.VERSION.SDK_INT >= 30) {
            if(externalStorage) {
                externalStorage = false;
            }
        }
    }

    // Obtener información sobre la tarjeta SD externa
    private String getExternalSDInfo() {
        StringBuilder info = new StringBuilder();

        info.append("State: ").append(Environment.getExternalStorageState()).append("\n");
        info.append("Emulated: ").append(Environment.isExternalStorageEmulated()).append("\n");
        info.append("Removable: ").append(Environment.isExternalStorageRemovable()).append("\n");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            info.append("Legacy: ").append(Environment.isExternalStorageLegacy()).append("\n");
        }

        info.append("Data Dir: ").append(getDataDir()).append("\n");
        info.append("Cache Dir: ").append(getApplicationContext().getCacheDir()).append("\n");
        File sdPath = Environment.getExternalStorageDirectory();
        info.append("External Storage Dir: ").append(sdPath).append("\n");
        info.append("External ALARMS Dir: ").append(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_ALARMS)).append("\n");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            info.append("External AUDIOBOOKS Dir: ").append(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_AUDIOBOOKS)).append("\n");
        }
        info.append("External DCIM Dir: ").append(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DCIM)).append("\n");

        info.append("External DOCUMENTS Dir: ").append(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)).append("\n");
        info.append("External DOWNLOADS Dir: ").append(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)).append("\n");
        info.append("External MOVIES Dir: ").append(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_MOVIES)).append("\n");
        info.append("External MUSIC Dir: ").append(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_MUSIC)).append("\n");
        info.append("External NOTIFICATIONS Dir: ").append(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_NOTIFICATIONS)).append("\n");
        info.append("External PICTURES Dir: ").append(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)).append("\n");
        info.append("External PODCASTS Dir: ").append(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PODCASTS)).append("\n");
        info.append("External RINGTONES Dir: ").append(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_RINGTONES)).append("\n");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            info.append("External SCREENSHOTS Dir: ").append(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_SCREENSHOTS)).append("\n");
        }
        info.append("Root Dir: ").append(Environment.getRootDirectory()).append("\n");

        return info.toString();
    }

    // Obtener el texto a mostrar en el TextView según el tipo de información y ubicación seleccionada
    private String getText() {
        Intent intent = getIntent();
        int infoType = intent.getIntExtra(INFO_TYPE, -1);
        int locationType = intent.getIntExtra(LOCATION_TYPE, -1);

        switch (infoType) {
            case 0:
                return getExternalSDInfo();
            case 1:
                switch (locationType) {
                    case 0:
                        return getFileText(getFilesDir());
                    case 1:
                        return getFileText(Environment.getExternalStorageDirectory());
                    default:
                        return "Error";
                }
            default:
                return "Error";
        }
    }

    // Leer el contenido de un archivo y devolverlo como texto
    private String getFileText(File dir) {
        File fileToRead = new File(dir, "information.txt");

        if (fileToRead.exists()) {
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(fileToRead))) {
                StringBuilder text = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    text.append(line).append("\n");
                }

                return text.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "El fichero no existe.";
    }

}
