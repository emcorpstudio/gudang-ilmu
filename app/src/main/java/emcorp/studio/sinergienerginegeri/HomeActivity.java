package emcorp.studio.sinergienerginegeri;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import emcorp.studio.sinergienerginegeri.adapter.AdapterListDonation;
import emcorp.studio.sinergienerginegeri.data.DataGenerator;
import emcorp.studio.sinergienerginegeri.model.Donation;
import emcorp.studio.sinergienerginegeri.utils.Constant;
import emcorp.studio.sinergienerginegeri.utils.SharedPrefManager;
import emcorp.studio.sinergienerginegeri.utils.Tools;

public class HomeActivity extends AppCompatActivity {
    private View lyt_parent;

    private RecyclerView recyclerView;
    private AdapterListDonation mAdapter;
    private ImageView btnTopUp;
    private TextView tvName;
    private TextView tvSaldo, tvLainVideo, tvLainProduk, tvLainDonasi;
    private ProgressDialog progressDialog;
    private List<Donation> items = new ArrayList<Donation>();
    private String saldo;
    private FloatingActionButton btnElektronik,btnMotor,btnKabel,btnLampu,btnSafety,btnRumah,btnAll;
    private LinearLayout btnPelajaran,btnBisnis,btnKeluarga,btnSemua, btnAccount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
//        initComponent();
        LoadProcess();
    }

    public void LoadProcess(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constant.ROOT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("CETAK",response);
                        progressDialog.dismiss();
                        items.clear();
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray jsonArray = obj.getJSONArray("donasi");
                            if(jsonArray.length()==0){
                                Toast.makeText(getApplicationContext(),"Tidak ada data donasi",Toast.LENGTH_SHORT).show();
                                recyclerView.setVisibility(View.GONE);
                            }else{
                                for (int i=0; i<jsonArray.length(); i++) {
                                    JSONObject isiArray = jsonArray.getJSONObject(i);
                                    String id_donasi = isiArray.getString("id_donasi");
                                    String id_user = isiArray.getString("id_user");
                                    String judul = isiArray.getString("judul");
                                    String deskripsi = isiArray.getString("deskripsi");
                                    String foto = isiArray.getString("foto");
                                    String status = isiArray.getString("status");
                                    String created_at = isiArray.getString("created_at");
                                    String full_name = isiArray.getString("full_name");

                                    Donation donation = new Donation();
                                    donation.id_donasi = id_donasi;
                                    donation.id_user = id_user;
                                    donation.judul = judul;
                                    donation.deskripsi = deskripsi;
                                    donation.foto = foto;
                                    donation.status = status;
                                    donation.created_at = created_at;
                                    donation.full_name = full_name;

                                    items.add(donation);
                                }

                                jsonArray = obj.getJSONArray("saldo");
                                for (int i=0; i<jsonArray.length(); i++) {
                                    JSONObject isiArray = jsonArray.getJSONObject(i);
                                    saldo = isiArray.getString("saldo");
                                }
                                SharedPrefManager.getInstance(getApplicationContext()).setReferences(Constant.PREFERENCES_SALDO,saldo);
                                initComponent();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(
                                getApplicationContext(),
                                error.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("function", Constant.FUNCTION_LIST_HOME);
                params.put("key", Constant.KEY);
                params.put("id_user", SharedPrefManager.getInstance(getApplicationContext()).getReferences(Constant.PREFERENCES_ID));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void initComponent() {
        btnTopUp = findViewById(R.id.btnTopUp);
        tvName = findViewById(R.id.tvName);
        tvSaldo = findViewById(R.id.tvSaldo);
        tvLainDonasi = findViewById(R.id.tvLainDonasi);
        tvLainProduk = findViewById(R.id.tvLainProduk);
        tvLainVideo = findViewById(R.id.tvLainVideo);
        lyt_parent = findViewById(R.id.lyt_parent);
        recyclerView = findViewById(R.id.recyclerView);
        btnElektronik = findViewById(R.id.btnElektronik);
        btnMotor = findViewById(R.id.btnMotor);
        btnKabel = findViewById(R.id.btnKabel);
        btnLampu = findViewById(R.id.btnLampu);
        btnSafety = findViewById(R.id.btnSafety);
        btnRumah = findViewById(R.id.btnRumah);
        btnAll = findViewById(R.id.btnAll);
        btnPelajaran = findViewById(R.id.btnPelajaran);
        btnBisnis = findViewById(R.id.btnBisnis);
        btnKeluarga = findViewById(R.id.btnKeluarga);
        btnSemua = findViewById(R.id.btnSemua);
        btnAccount = findViewById(R.id.btnAccount);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);

        tvName.setText(SharedPrefManager.getInstance(getApplicationContext()).getReferences(Constant.PREFERENCES_NAMA));
        tvSaldo.setText("Rp "+String.format("%,.0f", Double.valueOf(saldo)));

//        List<Donation> items = DataGenerator.getDonationData(this, 10);

        //set data and list adapter
        mAdapter = new AdapterListDonation(this, items, R.layout.item_donation_horizontal);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new AdapterListDonation.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Donation obj, int position) {
                Intent intent = new Intent(getApplicationContext(), DetailDonasiActivity.class);
                intent.putExtra("donation", obj);
                intent.putExtra("from", "home");
                startActivity(intent);
            }
        });


        btnTopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this,TopupActivity.class));
                finish();
            }
        });

        tvLainDonasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this,DonasiActivity.class));
                finish();
            }
        });

        tvLainProduk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this,ProductActivity.class));
                finish();
            }
        });

        tvLainVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this,VideoActivity.class));
                finish();
            }
        });

        btnElektronik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this,ProductActivity.class));
                finish();
            }
        });

        btnMotor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this,ProductActivity.class));
                finish();
            }
        });

        btnKabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this,ProductActivity.class));
                finish();
            }
        });

        btnLampu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this,ProductActivity.class));
                finish();
            }
        });

        btnSafety.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this,ProductActivity.class));
                finish();
            }
        });

        btnRumah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this,ProductActivity.class));
                finish();
            }
        });

        btnAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this,ProductActivity.class));
                finish();
            }
        });

        btnPelajaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this,VideoActivity.class));
                finish();
            }
        });

        btnBisnis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this,VideoActivity.class));
                finish();
            }
        });

        btnKeluarga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this,VideoActivity.class));
                finish();
            }
        });

        btnSemua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this,VideoActivity.class));
                finish();
            }
        });

        btnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutProcess();
            }
        });


    }

    public void logoutProcess(){
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(HomeActivity.this);
        builder.setTitle(null);
        builder.setMessage("Anda yakin ingin logout?");

        String positiveText = getString(android.R.string.ok);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor editor = getSharedPreferences("SinergiEnergiNegeri", Context.MODE_PRIVATE).edit();
                        editor.clear();
                        editor.commit();
                        startActivity(new Intent(HomeActivity.this,LoginActivity.class));
                        finish();
                        Toast.makeText(getApplicationContext(),"Logout berhasil", Toast.LENGTH_LONG).show();
                    }
                });

        String negativeText = getString(android.R.string.cancel);
        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        androidx.appcompat.app.AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        Tools.changeMenuIconColor(menu, Color.BLACK);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}