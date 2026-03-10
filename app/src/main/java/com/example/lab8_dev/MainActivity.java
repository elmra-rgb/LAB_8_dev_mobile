package com.example.lab8_dev;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView statusText;
    private ProgressBar progressBar;
    private ImageView imageView;
    private Handler uiHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        statusText = findViewById(R.id.statusText);
        progressBar = findViewById(R.id.progressBar);
        imageView = findViewById(R.id.imageView);

        Button loadImageBtn = findViewById(R.id.loadImageBtn);
        Button heavyCalcBtn = findViewById(R.id.heavyCalcBtn);
        Button showToastBtn = findViewById(R.id.showToastBtn);

        uiHandler = new Handler(Looper.getMainLooper());

        showToastBtn.setOnClickListener(v ->
                Toast.makeText(getApplicationContext(), "Interface réactive", Toast.LENGTH_SHORT).show()
        );

        loadImageBtn.setOnClickListener(v -> startImageLoading());
        heavyCalcBtn.setOnClickListener(v -> new ComplexCalculation().execute());
    }

    private void startImageLoading() {
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setProgress(0);
        statusText.setText("État : téléchargement image...");

        new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);

            uiHandler.post(() -> {
                imageView.setImageBitmap(imageBitmap);
                progressBar.setVisibility(View.INVISIBLE);
                statusText.setText("État : image chargée");
            });
        }).start();
    }

    private class ComplexCalculation extends AsyncTask<Void, Integer, Long> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(0);
            statusText.setText("État : calcul en cours...");
        }

        @Override
        protected Long doInBackground(Void... params) {
            long total = 0;

            for (int step = 1; step <= 100; step++) {
                for (int count = 0; count < 200000; count++) {
                    total += (step * count) % 7;
                }
                publishProgress(step);
            }
            return total;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Long result) {
            progressBar.setVisibility(View.INVISIBLE);
            statusText.setText("État : calcul terminé → " + result);
        }
    }
}