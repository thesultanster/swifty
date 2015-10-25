package com.example.haasith.parse2.profile;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.haasith.parse2.R;

public class ConfirmPaymentDialog extends DialogFragment implements View.OnClickListener {

    TextView cancel;
    TextView yes;
    TextView total;
    ConfirmPaymentCommunicator communicator;
    int sum;

    public static ConfirmPaymentDialog newInstance(int sum) {
        ConfirmPaymentDialog f = new ConfirmPaymentDialog();
        Bundle args = new Bundle();
        args.putInt("sum", sum);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        communicator = (ConfirmPaymentCommunicator) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_confirm_payment, null);
        getDialog().setTitle("Confirm Payment");
        cancel = (TextView) view.findViewById(R.id.cancel);
        yes = (TextView) view.findViewById(R.id.yes);
        total = (TextView) view.findViewById(R.id.total);

        sum = getArguments().getInt("sum");
        total.setText("$" + String.valueOf(sum));

        cancel.setOnClickListener(this);
        yes.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.cancel) {
            dismiss();
        } else if (view.getId() == R.id.yes) {
            dismiss();

            communicator.onDialogPayment();
        }


    }

}


interface ConfirmPaymentCommunicator {

    public void onDialogPayment();
}
