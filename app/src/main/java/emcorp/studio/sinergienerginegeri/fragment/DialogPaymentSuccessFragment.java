package emcorp.studio.sinergienerginegeri.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import emcorp.studio.sinergienerginegeri.HomeActivity;
import emcorp.studio.sinergienerginegeri.R;
import emcorp.studio.sinergienerginegeri.TopupActivity;
import emcorp.studio.sinergienerginegeri.utils.Constant;
import emcorp.studio.sinergienerginegeri.utils.SharedPrefManager;

public class DialogPaymentSuccessFragment extends DialogFragment {

    private View root_view;
    private TextView tvDate,tvTime;
    private TextView tvName, tvEmail,tvTransaction, tvAmount,tvType;
    private ImageView card_logo;
    String amount = "";
    String email = "";
    String name = "";
    String type = "";
    String phone = "";
    String transactionId = "";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root_view = inflater.inflate(R.layout.dialog_payment_success, container, false);
        tvDate = root_view.findViewById(R.id.tvDate);
        tvTime = root_view.findViewById(R.id.tvTime);
        tvName = root_view.findViewById(R.id.tvName);
        tvEmail = root_view.findViewById(R.id.tvEmail);
        tvTransaction = root_view.findViewById(R.id.tvTransaction);
        tvAmount = root_view.findViewById(R.id.tvAmount);
        tvType = root_view.findViewById(R.id.tvPayment);
        card_logo = root_view.findViewById(R.id.card_logo);

        type = SharedPrefManager.getInstance(getActivity()).getReferences(Constant.TOPUP_TYPE);
        amount = SharedPrefManager.getInstance(getActivity()).getReferences(Constant.TOPUP_AMOUNT);
        email = SharedPrefManager.getInstance(getActivity()).getReferences(Constant.TOPUP_EMAIL);
        name = SharedPrefManager.getInstance(getActivity()).getReferences(Constant.TOPUP_NAME);
        transactionId = SharedPrefManager.getInstance(getActivity()).getReferences(Constant.TOPUP_ID);

        Date datetimnow = Calendar.getInstance().getTime();

        ((FloatingActionButton) root_view.findViewById(R.id.fab)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                startActivity(new Intent(getActivity(), HomeActivity.class));
                getActivity().finish();
            }
        });

        SimpleDateFormat datenow = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        SimpleDateFormat timenow = new SimpleDateFormat("hh:mm aa", Locale.getDefault());

        tvDate.setText(datenow.format(datetimnow));
        tvTime.setText(timenow.format(datetimnow));
        tvName.setText(name);
        tvEmail.setText(email);
        tvAmount.setText("Rp "+String.format("%,.2f", Double.valueOf(amount)));
        tvType.setText(type);
        tvTransaction.setText("ID : "+transactionId);

        switch (type){
            case "CC" :
                card_logo.setImageResource(R.drawable.ic_visa);
                break;
            case "BCA" :
                card_logo.setImageResource(R.drawable.ic_bca);
                break;
            case "Mandiri" :
                card_logo.setImageResource(R.drawable.ic_mandiri);
                break;
            case "Permata" :
                card_logo.setImageResource(R.drawable.ic_permata);
                break;
            case "Transfer" :
                card_logo.setImageResource(R.drawable.ic_atmbersama);
                break;
        }

        return root_view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}