package com.isolutions.taxi5;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by fedar.trukhan on 27.09.16.
 */

public class FragmentPayments extends Fragment {

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(AppData.getInstance().toolbar != null) {
            AppData.getInstance().toolbar.ConvertToDefaultWithTitle(getString(R.string.payments_screen_title));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View screenView = inflater.inflate(R.layout.fragment_payments, container, false);
        ButterKnife.bind(this, screenView);

        return screenView;
    }


    @OnClick(R.id.fragment_payments_add_card_button)
    void onAddCardClick() {
//        Log.d("taxi5", "add card button touch");

        AlertDialog.Builder builder;
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(getActivity());
        }
        else {
            builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Dialog_Alert);
        }

//        TextView msgTextView = new TextView(AppData.getInstance().getAppContext());
//        msgTextView.setTextSize(20);
//        msgTextView.setPadding(0,16,0,16);
//        msgTextView.setTextColor(AppData.getInstance().getColor(R.color.defaultBlack));
//        msgTextView.setText();


//        msgTextView.setGravity(Gravity.CENTER);

//        builder.setView(msgTextView);
        builder.setTitle("Соглашение");
        builder.setMessage(getString(R.string.payments_agreement));
//        builder.
        builder.setPositiveButton(R.string.status_car_on_way_call_driver_dialog_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
//                Intent intent = new Intent(Intent.ACTION_CALL);
//                intent.setData(Uri.parse("tel:+375291337500"));
//                startActivity(intent);
            }
        });
        builder.setNegativeButton(R.string.status_car_on_way_call_driver_dialog_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
