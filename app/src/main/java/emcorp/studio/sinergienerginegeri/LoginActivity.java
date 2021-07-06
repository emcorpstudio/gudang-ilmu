package emcorp.studio.sinergienerginegeri;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import emcorp.studio.sinergienerginegeri.utils.Constant;
import emcorp.studio.sinergienerginegeri.utils.SharedPrefManager;

public class LoginActivity extends AppCompatActivity {
    Button btnLogin;
    TextView tvRegister;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private ProgressDialog progressDialog;
    private EditText edtPassword, edtEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);

        if (Build.VERSION.SDK_INT >= 23){
            if (checkPermission()){
                //Permission already granted
            } else {
                requestPermission(); // Code for permission
            }
        }

        if(SharedPrefManager.getInstance(getApplicationContext()).getReferences(Constant.PREFERENCES_LOGIN)!=null){
            if(SharedPrefManager.getInstance(getApplicationContext()).getReferences(Constant.PREFERENCES_LOGIN).equals("1")){
                startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                finish();
            }
        }

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
                finish();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edtEmail.getText().toString().equals("")){
                    edtEmail.setError("Email/username belum diisi!");
                }else{
                    if(edtPassword.getText().toString().equals("")){
                        edtPassword.setError("Password belum diisi!");
                    }else{
                        LoginProcess();
                    }
                }
            }
        });
    }

    public void LoginProcess(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constant.ROOT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.d("CETAK", Constant.ROOT_URL+" "+response);
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONObject userDetails = obj.getJSONObject("hasil");
                            String success = userDetails.getString("success");
                            String message = userDetails.getString("message");

                            if(success.equals("1")){
                                //Succes
                                String id = userDetails.getString("id_user");
                                String full_name = userDetails.getString("full_name");
                                String email = userDetails.getString("email");
                                String role = userDetails.getString("role");
                                String address = userDetails.getString("address");
                                String phone = userDetails.getString("phone");
                                String status = userDetails.getString("status");
                                String photo = userDetails.getString("photo");

                                SharedPrefManager.getInstance(getApplicationContext()).setReferences(Constant.PREFERENCES_ID,id);
                                SharedPrefManager.getInstance(getApplicationContext()).setReferences(Constant.PREFERENCES_NAMA,full_name);
                                SharedPrefManager.getInstance(getApplicationContext()).setReferences(Constant.PREFERENCES_ALAMAT,address);
                                SharedPrefManager.getInstance(getApplicationContext()).setReferences(Constant.PREFERENCES_HP,phone);
                                SharedPrefManager.getInstance(getApplicationContext()).setReferences(Constant.PREFERENCES_EMAIL,email);
                                SharedPrefManager.getInstance(getApplicationContext()).setReferences(Constant.PREFERENCES_STATUS,status);
                                SharedPrefManager.getInstance(getApplicationContext()).setReferences(Constant.PREFERENCES_ROLE,role);
                                SharedPrefManager.getInstance(getApplicationContext()).setReferences(Constant.PREFERENCES_FOTO,photo);
                                SharedPrefManager.getInstance(getApplicationContext()).setReferences(Constant.PREFERENCES_LOGIN,"1");
                                startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                                finish();
                            }
                            Toast.makeText(getApplicationContext(),message, Toast.LENGTH_SHORT).show();

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
                params.put("function", Constant.FUNCTION_LOGIN);
                params.put("key", Constant.KEY);
                params.put("email", edtEmail.getText().toString());
                params.put("password", edtPassword.getText().toString());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(LoginActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(LoginActivity.this, "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(LoginActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("value", "Permission Granted, Now you can use local drive .");
                } else {
                    Log.e("value", "Permission Denied, You cannot use local drive .");
                }
                break;
        }
    }
}