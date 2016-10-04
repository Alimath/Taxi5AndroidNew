package com.isolutions.taxi5;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.isolutions.taxi5.API.Taxi5SDKEntity.ProfileData;
import com.isolutions.taxi5.APIAssist.ApiAssistFactory;
import com.isolutions.taxi5.APIAssist.AssistCardsHolder;
import com.isolutions.taxi5.APIAssist.AssistSDK;
import com.isolutions.taxi5.APIAssist.Entities.AssistCustomerInfo;
import com.isolutions.taxi5.APIAssist.Entities.AssistOrder;
import com.isolutions.taxi5.APIAssist.Entities.AssistOrderStatusResponseData;
import com.isolutions.taxi5.APIAssist.Entities.AssistStoredCardData;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by fedar.trukhan on 03.10.16.
 */

public class FragmentPaymentsCustomerInfo extends Fragment {
    @BindView(R.id.fragment_payments_customer_info_email_edit_text)
    EditText emailEditText;

    @BindView(R.id.fragment_payments_customer_info_name_edit_text)
    EditText nameEditText;

    @BindView(R.id.fragment_payments_customer_info_f_name_edit_text)
    EditText familyNameEditText;

    private Boolean needAuthPayment = false;
    private Boolean authOneClickPayment = false;


    AlertDialog dialog;
    AdapterPaymentCards adapterCards;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(AppData.getInstance().toolbar != null) {
            AppData.getInstance().toolbar.ConvertToDefaultWithTitle(getString(R.string.assist_profile_customer_info_fragment_title));
        }


    }

    @Override
    public void onStart() {
        super.onStart();

        if(emailEditText != null && !TextUtils.isEmpty(AssistCustomerInfo.getInstance().getCustomerEmail())) {
            emailEditText.setText(AssistCustomerInfo.getInstance().getCustomerEmail());
        }
        else {
            if(emailEditText != null) {
                emailEditText.setText("");
            }
        }

        if(nameEditText != null && !TextUtils.isEmpty(AssistCustomerInfo.getInstance().getCustomerName())) {
            nameEditText.setText(AssistCustomerInfo.getInstance().getCustomerName());
        }
        else {
            if(nameEditText != null) {
                nameEditText.setText("");
            }
        }

        if(familyNameEditText != null && !TextUtils.isEmpty(AssistCustomerInfo.getInstance().getCustomerFamilyName())) {
            familyNameEditText.setText(AssistCustomerInfo.getInstance().getCustomerFamilyName());
        }
        else {
            if(familyNameEditText != null) {
                familyNameEditText.setText("");
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View screenView = inflater.inflate(R.layout.fragment_payments_customer_info, container, false);
        ButterKnife.bind(this, screenView);

        return screenView;
    }


    @OnClick(R.id.fragment_payments_customer_info_save_button)
    public void OnSaveButtonClick() {
        if(!TextUtils.isEmpty(nameEditText.getText()) && !TextUtils.isEmpty(familyNameEditText.getText())
                && isValidEmail(emailEditText.getText())) {
            AssistCustomerInfo customerInfo = AssistCustomerInfo.getInstance();
            customerInfo.setCustomerName(nameEditText.getText().toString());
            customerInfo.setCustomerFamilyName(familyNameEditText.getText().toString());
            customerInfo.setCustomerEmail(emailEditText.getText().toString());

            customerInfo.saveCustomerData();

            if(AppData.getInstance().mainActivity != null) {
                AppData.getInstance().mainActivity.OpenPayments();
            }
            InitReccurentPayment();
        }
        else if(TextUtils.isEmpty(nameEditText.getText())) {
            Toast.makeText(AppData.getInstance().getAppContext(), getString(R.string.assist_profile_please_input_name), Toast.LENGTH_SHORT).show();

        }
        else if(TextUtils.isEmpty(familyNameEditText.getText())) {
            Toast.makeText(AppData.getInstance().getAppContext(), getString(R.string.assist_profile_please_input_family_name), Toast.LENGTH_SHORT).show();
        }
        else if(!isValidEmail(emailEditText.getText())) {
            Toast.makeText(AppData.getInstance().getAppContext(), getString(R.string.assist_profile_please_input_email), Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.fragment_payments_customer_info_clear_button)
    public void OnClearButtonClick() {
        this.nameEditText.setText("");
        this.familyNameEditText.setText("");
        this.emailEditText.setText("");
    }

    private boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }


    //region Payments
    private void InitReccurentPayment() {
//        AlertDialog.Builder builder;
//        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
//            builder = new AlertDialog.Builder(getActivity());
//        }
//        else {
//            builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Dialog_Alert);
//        }

//        builder.setTitle(getString(R.string.payments_agreement_title));
//        builder.setMessage(getString(R.string.payments_agreement));
//        builder.setPositiveButton(R.string.payments_agreement_ok_button, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
                AlertDialog.Builder alert = new AlertDialog.Builder(AppData.getInstance().mainActivity);
                alert.setTitle(getString(R.string.payments_web_view_add_card_title));

                LinearLayout wrapper = new LinearLayout(AppData.getInstance().mainActivity);
                EditText keyboardHack = new EditText(AppData.getInstance().mainActivity);

                keyboardHack.setVisibility(View.GONE);


                WebView wv = new WebView(AppData.getInstance().mainActivity);

        AssistCustomerInfo info = AssistCustomerInfo.getInstance();

                String currentDateString = ((Long)(System.currentTimeMillis()/1000)).toString();
                ProfileData profileData = ProfileData.getInstance();

                final String orderNumber = "taxi5_test_auth_"+currentDateString+"_"+profileData.getMsid();
                final String merchantID = "460330";

                String postData = "Merchant_ID="+merchantID+"&OrderNumber="+orderNumber+"&OrderAmount=1&OrderComment=КОММЕНТАРИЙ&OrderCurrency=BYN";
                if(!TextUtils.isEmpty(profileData.getMsid())) {
                    postData += "&CustomerNumber="+profileData.getMsid();
                }
                if(!TextUtils.isEmpty(info.getCustomerEmail())) {
                    postData += "&Email="+info.getCustomerEmail();
                }
                if(!TextUtils.isEmpty(info.getCustomerName())) {
                    postData += "&Firstname="+info.getCustomerName();
                }
                if(!TextUtils.isEmpty(info.getCustomerFamilyName())) {
                    postData += "&Lastname="+info.getCustomerFamilyName();
                }

//                Log.d("taxi5", postData);
                ByteBuffer bb = Charset.forName("UTF-16").encode(postData);


                wv.postUrl("https://pay140.paysec.by/pay/order.cfm", bb.array());
                wv.setWebViewClient(new WebViewClient() {
                    private boolean isRedirected;

                    @Override
                    public void onPageStarted(WebView view, String url, Bitmap favicon) {
                        if(url.contains("testreturn2.taxi")) {
                            Log.d("taxi5", "paymentOK");
                            LoadOrderStatus(orderNumber, merchantID);
                            view.stopLoading();
                            return;

                        }
                        else if (url.contains("testreturn.taxi")) {
                            LoadOrderStatus(orderNumber, merchantID);
                            Log.d("taxi5", "payment error");
                            view.stopLoading();
                            return;
                        }
                        else {
                            super.onPageStarted(view, url, favicon);
                        }
                    }

                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                        isRedirected = true;
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
                dialog = alert.create();
                dialog.show();
//            }
//        });
//        builder.setNegativeButton(R.string.payments_agreement_cancel_button, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//
//            }
//        });
//
//        builder.create().show();
    }

    private void LoadOrderStatus(String orderNumber, String merchID) {
        final AssistSDK assistSDK = ApiAssistFactory.getAssistSDK();
        if(assistSDK != null) {
            Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);

            if(month == 1) {
                year -= 1;
                month = 12;
            }

            Call<AssistOrderStatusResponseData> call = assistSDK.GetOrderStatus("isolutions_1_test", "qweasdzxc123", merchID, orderNumber, 3, "1", ""+month, ""+year);
            call.enqueue(new Callback<AssistOrderStatusResponseData>() {
                @Override
                public void onResponse(Call<AssistOrderStatusResponseData> call, Response<AssistOrderStatusResponseData> response) {
                    if(dialog != null) {
                        dialog.dismiss();
                    }

                    if(response.body() != null && response.body().getOrdersList() != null) {
//                        Log.d("taxi5", "order list empty: " + response.body().getOrdersList().isEmpty());
                        AssistOrder order = response.body().getOrdersList().get(0);

                        AssistStoredCardData card1 = new AssistStoredCardData(order);

                        AssistCardsHolder.AddCard(card1);

                        if(adapterCards!= null) {
                            adapterCards.updateResource(AssistCardsHolder.GetCards());
                        }

                        String billNumber = order.getBillnumber();

                        Call<AssistOrderStatusResponseData> callCancel = assistSDK.CancelPayment("isolutions_1_test", "qweasdzxc123", "460330", billNumber, 3);
                        callCancel.enqueue(new Callback<AssistOrderStatusResponseData>() {
                            @Override
                            public void onResponse(Call<AssistOrderStatusResponseData> call, Response<AssistOrderStatusResponseData> response) {
                                Log.d("taxi5", "Cancel Response OK: " + "rc1: " + response.body().getFirstcode() + " - rc2: " + response.body().getSecondcode());
                            }

                            @Override
                            public void onFailure(Call<AssistOrderStatusResponseData> call, Throwable t) {
                                Log.d("taxi5", "Cancel Response FAILURE");
                            }
                        });
                    }
                }

                @Override
                public void onFailure(Call<AssistOrderStatusResponseData> call, Throwable t) {
                    Log.d("taxi5", "Order Status Check Error:  " + t.getLocalizedMessage());
                    if(dialog != null) {
                        dialog.dismiss();
                    }
                    if(adapterCards != null) {
                        adapterCards.updateResource(AssistCardsHolder.GetCards());
                    }
                }
            });
        }
    }

    //endregion

}
