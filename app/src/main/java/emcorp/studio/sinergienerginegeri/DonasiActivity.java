package emcorp.studio.sinergienerginegeri;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import emcorp.studio.sinergienerginegeri.adapter.AdapterListDonation;
import emcorp.studio.sinergienerginegeri.model.Donation;
import emcorp.studio.sinergienerginegeri.utils.Constant;
import emcorp.studio.sinergienerginegeri.utils.SharedPrefManager;
import emcorp.studio.sinergienerginegeri.utils.Tools;

public class DonasiActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;
    private List<Donation> items = new ArrayList<Donation>();
    private RecyclerView recyclerView;
    private AdapterListDonation mAdapter;
    private FloatingActionButton btn_add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donasi);
        initToolbar();
        LoadProcess();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Donasi");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this);
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
                            JSONArray jsonArray = obj.getJSONArray("hasil");
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
                params.put("function", Constant.FUNCTION_LIST_DONASI);
                params.put("key", Constant.KEY);
                params.put("id_user", "");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void initComponent() {
        recyclerView = findViewById(R.id.recyclerView);
        btn_add = findViewById(R.id.fab_add);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);

        //set data and list adapter
        mAdapter = new AdapterListDonation(this, items, R.layout.item_donation_horizontal);
        recyclerView.setAdapter(mAdapter);
        // on item list clicked
        mAdapter.setOnItemClickListener(new AdapterListDonation.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Donation obj, int position) {
                Intent intent = new Intent(getApplicationContext(), DetailDonasiActivity.class);
                intent.putExtra("donation", obj);
                intent.putExtra("from", "donasi");
                startActivity(intent);
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"ADD",Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(DonasiActivity.this,HomeActivity.class));
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(DonasiActivity.this,HomeActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}