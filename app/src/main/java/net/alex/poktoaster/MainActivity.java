package net.alex.poktoaster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    TextView etiqueta;
    Button btnCalc;
    ProgressBar barraProgreso;
    Spinner spin;
    ConstraintLayout pantalla;

    private void operacion( int zeros )
    {
        double ceros = 0.01;

        switch (zeros)
        {
            case 5: ceros = 0.000001;
                break;
            case 6: ceros = 0.0000001;
                break;
            case 7: ceros = 0.00000001;
                break;
            case 8: ceros = 0.000000001;
                break;
            case 9: ceros = 0.0000000001;
                break;
            default: ceros = 0.0001;
                break;
        }

        double a, b, c, d, x = 0.0, x1 = 0.0;

        int s = 0;

        for (a = -10; a <= 10; a = a + ceros)
        {
            b = -2 * (Math.pow(a, 2));
            c = (11 * a) / 3;
            d = b + c;

            if (a == -10)
            {
                x = d;
            }

            if (d > x)
            {
                x1 = d;
                x = d;
            }

            s++;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        etiqueta = findViewById(R.id.txtvRes);
        btnCalc = findViewById(R.id.btnCalcular);
        barraProgreso = findViewById(R.id.pgOperacion);
        pantalla = findViewById(R.id.Layout);

        pantalla.setBackgroundResource(R.color.BlueApp);

        barraProgreso.getIndeterminateDrawable()
                .setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);

        spin = findViewById(R.id.spinNum);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(
                this,
                R.array.zeros,
                R.layout.spinner_item
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);

        btnCalc.setOnClickListener(view ->
        {
            etiqueta.setText(R.string.calculando);
            barraProgreso.setVisibility(View.VISIBLE);
            btnCalc.setEnabled(false);

            new Thread(() -> {
                long startTime = System.currentTimeMillis();
                operacion( Integer.parseInt( spin.getSelectedItem().toString() ) );
                long endTime = System.currentTimeMillis();

                long duration = (endTime - startTime);

                runOnUiThread(() -> {
                    @SuppressLint("DefaultLocale") String format = String.format(" %d min, %d sec",
                            TimeUnit.MILLISECONDS.toMinutes(duration),
                            TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));

                    etiqueta.setText(getResources().getString(R.string.tiempotranscu) + format);

                    barraProgreso.setVisibility(View.GONE);
                    btnCalc.setEnabled(true);
                });
            }).start();
        });
    }
}