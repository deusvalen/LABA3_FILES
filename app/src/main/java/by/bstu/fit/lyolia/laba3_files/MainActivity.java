package by.bstu.fit.lyolia.laba3_files;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText russianWordEt;
    EditText englishWordEt;
    Button getButton;
    Button saveButton;

    SharedPreferences prefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        russianWordEt = (EditText) findViewById(R.id.russianWordEt);
        englishWordEt = (EditText) findViewById(R.id.englishWordEt);
        getButton = (Button) findViewById(R.id.getButton);
        saveButton = (Button) findViewById(R.id.saveButton);

        prefs = getPreferences(Context.MODE_PRIVATE);

        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (prefs.getBoolean("firstrun", true)) {
            Misc.initDictionary(); // Заполнение словаря значениями по-умолчанию
            prefs.edit().putBoolean("firstrun", false).apply();
        }
    }

    public void onGetClick(View view) {
        englishWordEt.setText("");
        String russianWord = russianWordEt.getText().toString();
        if(russianWord.isEmpty()) {
            Toast.makeText(this.getApplicationContext(), "Заполните поле!", Toast.LENGTH_SHORT).show();
            return;
        } else {
            List<String> chunks = Misc.readFileSD(Misc.FILENAME);
            assert chunks != null;

            for (String chunk:
                    chunks) {
                Log.d(Misc.TAG, chunk);
                String [] mass = chunk.split(":");
                Log.d(Misc.TAG, Misc.levensteinDist(russianWord.toLowerCase(), mass[0].toLowerCase()) + "");
                if(Misc.levensteinDist(russianWord.toLowerCase(), mass[0].toLowerCase()) <= 1) {
                    englishWordEt.setText(mass[1]);
                    return;
                }
            }
        }
        if (englishWordEt.getText().toString().isEmpty()) {
            Toast.makeText(this.getApplicationContext(), "Слова нет словаре!", Toast.LENGTH_SHORT).show();
        }
    }

    public void onSaveClick(View view) {
        String russianWord = russianWordEt.getText().toString();
        String englishWord = englishWordEt.getText().toString();
        if(russianWord.isEmpty() || englishWord.isEmpty()) {
            Toast.makeText(this.getApplicationContext(), "Заполните поля!", Toast.LENGTH_SHORT).show();
        } else {
            if(Misc.writeInFile(russianWord + ":" + englishWord + "\n", Misc.FILENAME, true)) {
                Toast.makeText(this.getApplicationContext(), "Слова записаны в словарь", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this.getApplicationContext(), "Ошибка записи в словарь!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getButton.setEnabled(true);
                } else {
                    getButton.setEnabled(false);
                    Toast.makeText(MainActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }

        }
    }
}
