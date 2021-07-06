package emcorp.studio.sinergienerginegeri;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import emcorp.studio.sinergienerginegeri.fragment.DialogPaymentSuccessFragment;
import emcorp.studio.sinergienerginegeri.utils.Constant;
import emcorp.studio.sinergienerginegeri.utils.SdkConfig;
import emcorp.studio.sinergienerginegeri.utils.SharedPrefManager;
import emcorp.studio.sinergienerginegeri.utils.Tools;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.midtrans.sdk.corekit.callback.CardRegistrationCallback;
import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.core.PaymentMethod;
import com.midtrans.sdk.corekit.core.TransactionRequest;
import com.midtrans.sdk.corekit.core.UIKitCustomSetting;
import com.midtrans.sdk.corekit.core.themes.CustomColorTheme;
import com.midtrans.sdk.corekit.models.CardRegistrationResponse;
import com.midtrans.sdk.corekit.models.CustomerDetails;
import com.midtrans.sdk.corekit.models.snap.Gopay;
import com.midtrans.sdk.corekit.models.snap.Shopeepay;
import com.midtrans.sdk.corekit.models.snap.TransactionResult;
import com.midtrans.sdk.uikit.SdkUIFlowBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TopupActivity extends AppCompatActivity implements TransactionFinishedCallback {
    LinearLayout btnKartuKredit, btnBca, btnMandiri, btnPermata, btnAtm;
    EditText edtNominal;
    private ProgressBar progress_bar;
    String amount = "";
    String email = "";
    String name = "";
    String type = "";
    String phone = "";
    String transactionId = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topup);
        initToolbar();
        bindViews();
        initActionButtons();
        initMidtransSdk();
    }

    public void Topup(){
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
//                                startActivity(new Intent(TopupActivity.this,HomeActivity.class));
//                                finish();
                                successAction();
                            }else{
                                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                            }
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
                params.put("function", Constant.FUNCTION_TOPUP);
                params.put("key", Constant.KEY);
                params.put("id_user", SharedPrefManager.getInstance(getApplicationContext()).getReferences(Constant.PREFERENCES_ID));
                params.put("amount", amount);
                params.put("type", type);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void bindViews() {
        btnKartuKredit = findViewById(R.id.btnKartuKredit);
        btnBca = findViewById(R.id.btnBca);
        btnPermata = findViewById(R.id.btnPermata);
        btnMandiri = findViewById(R.id.btnMandiri);
        btnAtm = findViewById(R.id.btnAtm);
        edtNominal = findViewById(R.id.edtNominal);
        progress_bar = (ProgressBar) findViewById(R.id.progress_bar);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Top Up");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this);
    }

    private TransactionRequest initTransactionRequest() {
        // Create new Transaction Request
//        TransactionRequest transactionRequestNew = new
//                TransactionRequest(System.currentTimeMillis() + "", 36500.0);
        TransactionRequest transactionRequestNew = new
                TransactionRequest(System.currentTimeMillis() + "", Double.valueOf(amount));
        transactionRequestNew.setCustomerDetails(initCustomerDetails());
        transactionRequestNew.setGopay(new Gopay("mysamplesdk:://midtrans"));
        transactionRequestNew.setShopeepay(new Shopeepay("mysamplesdk:://midtrans"));
        return transactionRequestNew;
    }

    private CustomerDetails initCustomerDetails() {
        //define customer detail (mandatory for coreflow)
        CustomerDetails mCustomerDetails = new CustomerDetails();
        mCustomerDetails.setPhone(phone);
        mCustomerDetails.setFirstName(name);
        mCustomerDetails.setEmail(email);
        mCustomerDetails.setCustomerIdentifier(email);
//        mCustomerDetails.setPhone("085655312333");
//        mCustomerDetails.setFirstName("user fullname");
//        mCustomerDetails.setEmail("sen@gmail.com");
//        mCustomerDetails.setCustomerIdentifier("sen@gmail.com");
        return mCustomerDetails;
    }

    private void initMidtransSdk() {
        String client_key = SdkConfig.MERCHANT_CLIENT_KEY;
        String base_url = SdkConfig.MERCHANT_BASE_CHECKOUT_URL;
        SdkUIFlowBuilder sdkUIFlowBuilder = SdkUIFlowBuilder.init()
                .setClientKey(client_key) // client_key is mandatory
                .setContext(this) // context is mandatory
                .setTransactionFinishedCallback(this) // set transaction finish callback (sdk callback)
                .setMerchantBaseUrl(base_url)//set merchant url
                .setUIkitCustomSetting(uiKitCustomSetting())
                .enableLog(true) // enable sdk log
                .setColorTheme(new CustomColorTheme("#FFE51255", "#B61548", "#FFE51255")) // will replace theme on snap theme on MAP
                .setLanguage("en");
        sdkUIFlowBuilder.buildSDK();
    }

    private void successAction() {
        progress_bar.setVisibility(View.VISIBLE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showDialogPaymentSuccess();
                progress_bar.setVisibility(View.GONE);
            }
        }, 100);
    }

    private void showDialogPaymentSuccess() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        DialogPaymentSuccessFragment newFragment = new DialogPaymentSuccessFragment();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//        transaction.add(android.R.id.content, newFragment).addToBackStack(null).commit();
        transaction.add(android.R.id.content, newFragment).addToBackStack(null).commitAllowingStateLoss();
    }

    private void setData(){
        email = "misbakhulmunir11@gmail.com";
        name = "Misbakhul Munir";
        phone = "085655312333";
        amount = edtNominal.getText().toString();
        SharedPrefManager.getInstance(getApplicationContext()).setReferences(Constant.TOPUP_TYPE,type);
        SharedPrefManager.getInstance(getApplicationContext()).setReferences(Constant.TOPUP_AMOUNT,amount);
        SharedPrefManager.getInstance(getApplicationContext()).setReferences(Constant.TOPUP_NAME,name);
        SharedPrefManager.getInstance(getApplicationContext()).setReferences(Constant.TOPUP_EMAIL,email);
    }

    @Override
    public void onTransactionFinished(TransactionResult result) {
        if (result.getResponse() != null) {
            switch (result.getStatus()) {
                case TransactionResult.STATUS_SUCCESS:
//                    Toast.makeText(this, "Transaction Finished. ID: " + result.getResponse().getTransactionId(), Toast.LENGTH_LONG).show();
                    break;
                case TransactionResult.STATUS_PENDING:
//                    Toast.makeText(this, "Transaction Pending. ID: " + result.getResponse().getTransactionId(), Toast.LENGTH_LONG).show();
                    break;
                case TransactionResult.STATUS_FAILED:
//                    Toast.makeText(this, "Transaction Failed. ID: " + result.getResponse().getTransactionId() + ". Message: " + result.getResponse().getStatusMessage(), Toast.LENGTH_LONG).show();
                    break;
            }
            result.getResponse().getValidationMessages();
            transactionId = result.getResponse().getTransactionId();
            SharedPrefManager.getInstance(getApplicationContext()).setReferences(Constant.TOPUP_ID,transactionId);
            Topup();
//            successAction();
        } else if (result.isTransactionCanceled()) {
            Toast.makeText(this, "Transaction Canceled", Toast.LENGTH_LONG).show();
        } else {
            if (result.getStatus().equalsIgnoreCase(TransactionResult.STATUS_INVALID)) {
                Toast.makeText(this, "Transaction Invalid", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Transaction Finished with failure.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void initActionButtons() {
//
        btnKartuKredit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = "CC";
                setData();
                MidtransSDK.getInstance().setTransactionRequest(initTransactionRequest());
                MidtransSDK.getInstance().UiCardRegistration(TopupActivity.this, new CardRegistrationCallback() {
                    @Override
                    public void onSuccess(CardRegistrationResponse cardRegistrationResponse) {
                        Toast.makeText(TopupActivity.this, "register card token success", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(CardRegistrationResponse cardRegistrationResponse, String s) {
                        Toast.makeText(TopupActivity.this, "register card token Failed", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }
                });
            }
        });


        btnBca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = "BCA";
                setData();
                MidtransSDK.getInstance().setTransactionRequest(initTransactionRequest());
                MidtransSDK.getInstance().startPaymentUiFlow(TopupActivity.this, PaymentMethod.BANK_TRANSFER_BCA);
            }
        });
//
        btnMandiri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = "Mandiri";
                setData();
                MidtransSDK.getInstance().setTransactionRequest(initTransactionRequest());
                MidtransSDK.getInstance().startPaymentUiFlow(TopupActivity.this, PaymentMethod.BANK_TRANSFER_MANDIRI);
            }
        });


        btnPermata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = "Permata";
                setData();
                MidtransSDK.getInstance().setTransactionRequest(initTransactionRequest());
                MidtransSDK.getInstance().startPaymentUiFlow(TopupActivity.this, PaymentMethod.BANK_TRANSFER_PERMATA);
            }
        });

        btnAtm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = "Transfer";
                setData();
                MidtransSDK.getInstance().setTransactionRequest(initTransactionRequest());
                MidtransSDK.getInstance().startPaymentUiFlow(TopupActivity.this, PaymentMethod.BANK_TRANSFER);
            }
        });
    }

    private UIKitCustomSetting uiKitCustomSetting() {
        UIKitCustomSetting uIKitCustomSetting = new UIKitCustomSetting();
        uIKitCustomSetting.setSkipCustomerDetailsPages(true);
        uIKitCustomSetting.setShowPaymentStatus(true);
        return uIKitCustomSetting;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(TopupActivity.this,HomeActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(TopupActivity.this,HomeActivity.class));
        finish();
    }
}