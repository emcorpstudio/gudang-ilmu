package emcorp.studio.sinergienerginegeri;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import emcorp.studio.sinergienerginegeri.utils.Constant;
import emcorp.studio.sinergienerginegeri.utils.SharedPrefManager;
import emcorp.studio.sinergienerginegeri.utils.Tools;

public class CheckoutActivity extends AppCompatActivity {
    LinearLayout btnPay;
    TextView tvSaldo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        initToolbar();

        btnPay = findViewById(R.id.btnPay);
        tvSaldo = findViewById(R.id.tvSaldo);

        tvSaldo.setText("Rp "+SharedPrefManager.getInstance(getApplicationContext()).getReferences(Constant.PREFERENCES_SALDO));

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Pembayaran berhasil",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(CheckoutActivity.this,ProductActivity.class));
                finish();
            }
        });
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Proses Order");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(CheckoutActivity.this,ProductActivity.class));
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(CheckoutActivity.this,ProductActivity.class));
            finish();
        }else {
            startActivity(new Intent(CheckoutActivity.this,ProductActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}