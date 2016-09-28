package com.isolutions.taxi5;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.isolutions.taxi5.API.Taxi5SDKEntity.ProfileData;

import java.util.Map;
import org.apache.http.util.EncodingUtils;

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

        builder.setTitle(getString(R.string.payments_agreement_title));
        builder.setMessage(getString(R.string.payments_agreement));
        builder.setPositiveButton(R.string.payments_agreement_ok_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                AlertDialog.Builder alert = new AlertDialog.Builder(AppData.getInstance().mainActivity);
                alert.setTitle(getString(R.string.payments_web_view_add_card_title));

                LinearLayout wrapper = new LinearLayout(AppData.getInstance().mainActivity);
                EditText keyboardHack = new EditText(AppData.getInstance().mainActivity);

                keyboardHack.setVisibility(View.GONE);


                WebView wv = new WebView(AppData.getInstance().mainActivity);

                String currentDateString = ((Long)(System.currentTimeMillis()/1000)).toString();
                ProfileData profileData = ProfileData.getInstance();

                String postData = "Merchant_ID=460330&OrderNumber=taxi5_test_auth_"+currentDateString+"&OrderAmount=1&OrderComment=КОММЕНТАРИЙ&OrderCurrency=BYN&Lastname=Трухан Федор";
                if(!TextUtils.isEmpty(profileData.getMsid())) {
                    postData += "&CustomerNumber="+profileData.getMsid();
                }
                if(!TextUtils.isEmpty(profileData.getName())) {
                    postData += "&Firstname="+profileData.getName();
                }
//                if(!TextUtils.isEmpty(profileData.getEmail())) {
//                    postData += "&Email="+profileData.getEmail();
//                }

                wv.postUrl("https://pay140.paysec.by/pay/order.cfm", EncodingUtils.getBytes(postData, "BASE64"));
                wv.setWebViewClient(new WebViewClient() {
//                    @Override
//                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                        view.loadUrl(url);
//
//                        return true;
//                    }

                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                        return super.shouldOverrideUrlLoading(view, request);
                    }
                });

                alert.setView(wv);
                alert.setNegativeButton(getString(R.string.status_car_on_way_call_driver_dialog_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

                wrapper.setOrientation(LinearLayout.VERTICAL);
                wrapper.addView(wv, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                wrapper.addView(keyboardHack, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                alert.setView(wrapper);
                alert.create().show();
            }
        });
        builder.setNegativeButton(R.string.payments_agreement_cancel_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
