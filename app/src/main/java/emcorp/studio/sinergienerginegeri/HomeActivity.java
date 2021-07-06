package emcorp.studio.sinergienerginegeri;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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