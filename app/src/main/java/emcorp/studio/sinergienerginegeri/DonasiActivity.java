package emcorp.studio.sinergienerginegeri;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Base64;
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
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
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
    private Bitmap bitmapFoto = null;
    ImageView imgDondasi;
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
                addDonasi();
            }
        });
    }

    private void addDonasi() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_donasi);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


        final EditText edtJudul = (EditText) dialog.findViewById(R.id.edtJudul);
        final EditText edtDeskripsi = (EditText) dialog.findViewById(R.id.edtDeskripsi);
        imgDondasi = (ImageView) dialog.findViewById(R.id.imgDonasi);

        imgDondasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage(DonasiActivity.this);
            }
        });



        ((ImageButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        ((Button) dialog.findViewById(R.id.bt_save)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddDonasiProcee(edtJudul.getText().toString(),edtDeskripsi.getText().toString());
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    public void AddDonasiProcee(String judul, String deskripsi){
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
                                LoadProcess();
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
                params.put("function", Constant.FUNCTION_ADDDONASI);
                params.put("key", Constant.KEY);
                params.put("id_user", SharedPrefManager.getInstance(getApplicationContext()).getReferences(Constant.PREFERENCES_ID));
                params.put("judul", judul);
                params.put("deskripsi", deskripsi);
                params.put("foto", getStringImage(bitmapFoto));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void selectImage(Context context) {
        final CharSequence[] options = { "Pilih dari Gallery","Batal" };

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Pilih Foto");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Pilih dari Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto , 1);

                } else if (options[item].equals("Batal")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        bitmapFoto = selectedImage;
                        imgDondasi.setImageBitmap(selectedImage);
                    }

                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();

                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                imgDondasi.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                bitmapFoto = BitmapFactory.decodeFile(picturePath);
                                cursor.close();
                                Log.d("CETAK","UPLAOD FOTO");
                            }
                        }

                    }
                    break;
            }
        }
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