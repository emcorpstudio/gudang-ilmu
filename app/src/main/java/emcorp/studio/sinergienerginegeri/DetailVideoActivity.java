package emcorp.studio.sinergienerginegeri;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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

import emcorp.studio.sinergienerginegeri.model.Produk;
import emcorp.studio.sinergienerginegeri.model.Video;
import emcorp.studio.sinergienerginegeri.utils.Constant;
import emcorp.studio.sinergienerginegeri.utils.MusicUtils;
import emcorp.studio.sinergienerginegeri.utils.SharedPrefManager;
import emcorp.studio.sinergienerginegeri.utils.Tools;

public class DetailVideoActivity extends AppCompatActivity {
    private FloatingActionButton bt_play;
    private ImageView image;
    private TextView tv_duration;
    private View lyt_progress;
    private AppCompatSeekBar seek_bar;

    private CountDownTimer countDownTimer;
    long millisInFuture = 30000; //30 seconds
    private MusicUtils musicUtils;

    private boolean state_play = false;
    private boolean show_action = true;
    private Video video;
    private TextView tvJudul, tvDeskripsi;
    private String id_video = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_video);
        video = (Video) getIntent().getSerializableExtra("video");
        initToolbar();
        initComponent();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Detail Video");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this);
    }


    private void initComponent() {
        musicUtils = new MusicUtils();
        bt_play = (FloatingActionButton) findViewById(R.id.bt_play);
        image = (ImageView) findViewById(R.id.image);
        lyt_progress = (View) findViewById(R.id.lyt_progress);
        tv_duration = (TextView) findViewById(R.id.tv_duration);
        tvDeskripsi = (TextView) findViewById(R.id.tvDeskripsi);
        tvJudul = (TextView) findViewById(R.id.tvJudul);
        seek_bar = (AppCompatSeekBar) findViewById(R.id.seek_bar);

        tvJudul.setText(video.judul);
        tvDeskripsi.setText(video.deskripsi);
        id_video = video.id_video;

        Tools.displayImageOriginal(getApplicationContext(),image, Constant.PICT_VIDEO_URL+video.foto);


        seek_bar.setMax(30);

        bt_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleButtonPlay();
            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleActionView();
            }
        });
    }

    private void toggleActionView() {
        show_action = !show_action;
        if (show_action) {
            bt_play.setVisibility(View.VISIBLE);
            lyt_progress.setVisibility(View.VISIBLE);
        } else {
            bt_play.setVisibility(View.INVISIBLE);
            lyt_progress.setVisibility(View.INVISIBLE);
        }
    }

    private void toggleButtonPlay() {
        state_play = !state_play;
        if (state_play) {
            bt_play.setImageResource(R.drawable.ic_pause);
            runCountDownTimer();
        } else {
            bt_play.setImageResource(R.drawable.ic_play_arrow);
            if (countDownTimer != null) countDownTimer.cancel();
        }
    }

    private void runCountDownTimer() {
        if (countDownTimer != null) countDownTimer.cancel();
        countDownTimer = new CountDownTimer(millisInFuture, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                millisInFuture = millisUntilFinished;
                tv_duration.setText(musicUtils.milliSecondsToTimer(millisUntilFinished));
                Long progress = (30000 - millisUntilFinished) / 1000;
                seek_bar.setProgress(progress.intValue());
            }

            @Override
            public void onFinish() {
                bt_play.setImageResource(R.drawable.ic_play_arrow);
                state_play = false;
                millisInFuture = 30000;
                seek_bar.setProgress(0);
            }
        }.start();
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
                                startActivity(new Intent(DetailVideoActivity.this,VideoActivity.class));
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
                params.put("function", Constant.FUNCTION_DONASIVIDEO);
                params.put("key", Constant.KEY);
                params.put("id_user", SharedPrefManager.getInstance(getApplicationContext()).getReferences(Constant.PREFERENCES_ID));
                params.put("amount", amount);
                params.put("id_donasi", id_video);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_donate, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else {
            showDonation();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        super.onPause();
    }
}