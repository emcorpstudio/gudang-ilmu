package emcorp.studio.sinergienerginegeri;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import emcorp.studio.sinergienerginegeri.model.Donation;
import emcorp.studio.sinergienerginegeri.utils.Constant;
import emcorp.studio.sinergienerginegeri.utils.SharedPrefManager;
import emcorp.studio.sinergienerginegeri.utils.Tools;

public class DetailDonasiActivity extends AppCompatActivity {
    Donation donation;
    private View parent_view;

    private ImageButton bt_toggle_reviews, bt_toggle_warranty, bt_toggle_description;
    private View lyt_expand_reviews, lyt_expand_warranty, lyt_expand_description;
    private NestedScrollView nested_scroll_view;
    private TextView tvJudul, tvName, tvDeskripsi;
    String from = "";
    String id_donasi = "";
    private ImageView image;
    private FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_donasi);
        parent_view = findViewById(R.id.parent_view);
        tvJudul = findViewById(R.id.tvJudul);
        tvName = findViewById(R.id.tvName);
        tvDeskripsi = findViewById(R.id.tvDeskripsi);
        image = findViewById(R.id.image);
        fab = findViewById(R.id.fab);
        initToolbar();
        donation = (Donation)getIntent().getSerializableExtra("donation");
        from = getIntent().getStringExtra("from");
        tvJudul.setText(donation.judul);
        tvName.setText(donation.full_name);
        tvDeskripsi.setText(donation.deskripsi);
        id_donasi = donation.id_donasi;
        Tools.displayImageOriginal(getApplicationContext(),image, Constant.PICT_DONASI_URL+donation.foto);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDonation();
            }
        });
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Detail Donasi");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this);
    }

    private void showDonation() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_donate);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        final EditText edtNilai = (EditText) dialog.findViewById(R.id.edtNilai);

        ((ImageButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        ((Button) dialog.findViewById(R.id.bt_save)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!edtNilai.getText().toString().equals("")){
                    Sumbang(edtNilai.getText().toString());
                    dialog.dismiss();
                }
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    public void Sumbang(String amount){
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constant.ROOT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("CETAK",Constant.ROOT_URL+" "+response);
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONObject userDetails = obj.getJSONObject("result");
                            String success = userDetails.getString("success");
                            String message = userDetails.getString("message");
                            if(success.equals("1")){
                                if(from.equals("donasi")){
                                    startActivity(new Intent(DetailDonasiActivity.this,DonasiActivity.class));
                                }else{
                                    startActivity(new Intent(DetailDonasiActivity.this,HomeActivity.class));
                                }
                                finish();
                            }
                            Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("function", Constant.FUNCTION_DONASI);
                params.put("key", Constant.KEY);
                params.put("id_user", SharedPrefManager.getInstance(getApplicationContext()).getReferences(Constant.PREFERENCES_ID));
                params.put("amount", amount);
                params.put("id_donasi", id_donasi);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        if(from.equals("donasi")){
            startActivity(new Intent(DetailDonasiActivity.this,DonasiActivity.class));
        }else{
            startActivity(new Intent(DetailDonasiActivity.this,HomeActivity.class));
        }
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if(from.equals("donasi")){
                startActivity(new Intent(DetailDonasiActivity.this,DonasiActivity.class));
            }else{
                startActivity(new Intent(DetailDonasiActivity.this,HomeActivity.class));
            }
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}