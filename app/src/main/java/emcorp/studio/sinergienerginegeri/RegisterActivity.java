package emcorp.studio.sinergienerginegeri;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
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

import emcorp.studio.sinergienerginegeri.utils.Constant;
import emcorp.studio.sinergienerginegeri.utils.Tools;

public class RegisterActivity extends AppCompatActivity {
    FloatingActionButton fab_done;
    EditText edtEmail, edtPassword, edtFullname, edtAddress, edtPhone;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);
        initToolbar();
        fab_done = findViewById(R.id.fab_done);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        edtFullname = findViewById(R.id.edtFullname);
        edtAddress = findViewById(R.id.edtAddress);
        edtPhone = findViewById(R.id.edtPhone);

        edtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

        fab_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!edtEmail.getText().toString().equals("")){
                    if(!edtPassword.getText().toString().equals("")){
                        if(!edtFullname.getText().toString().equals("")){
                            if(!edtAddress.getText().toString().equals("")){
                                if(!edtPhone.getText().toString().equals("")){
                                    //Register Process
                                    Register();
                                }else{
                                    Toast.makeText(getApplicationContext(),"Nomor handphone wajib diisi!",Toast.LENGTH_SHORT).show();
                                    edtPhone.requestFocus();
                                }
                            }else{
                                Toast.makeText(getApplicationContext(),"Alamat wajib diisi!",Toast.LENGTH_SHORT).show();
                                edtAddress.requestFocus();
                            }
                        }else{
                            Toast.makeText(getApplicationContext(),"Nama wajib diisi!",Toast.LENGTH_SHORT).show();
                            edtFullname.requestFocus();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(),"Password wajib diisi!",Toast.LENGTH_SHORT).show();
                        edtPassword.requestFocus();
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"Email wajib diisi!",Toast.LENGTH_SHORT).show();
                    edtEmail.requestFocus();
                }
            }
        });
    }

    public void Register(){
        progressDialog = new ProgressDialog(RegisterActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constant.ROOT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.d("CETAK",Constant.ROOT_URL+" "+response);
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONObject userDetails = obj.getJSONObject("result");
                            String success = userDetails.getString("success");
                            String message = userDetails.getString("message");
                            if(success.equals("1")){
                                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
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
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("function", Constant.FUNCTION_REGISTER);
                params.put("key", Constant.KEY);
                params.put("id_user", "");
                params.put("full_name", edtFullname.getText().toString());
                params.put("address", edtAddress.getText().toString());
                params.put("phone", edtPhone.getText().toString());
                params.put("email", edtEmail.getText().toString());
                params.put("password", edtPassword.getText().toString());
                params.put("status", "PENDING");
                params.put("role", "USER");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("Register");
        Tools.setSystemBarColor(this, R.color.green_800);
    }

    private void initComponent() {
        (findViewById(R.id.fab_done)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_SHORT).show();
            }
        });
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