package com.isolutions.taxi5;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.constraint.ConstraintLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.isolutions.taxi5.API.ApiFactory;
import com.isolutions.taxi5.API.Taxi5SDK;
import com.isolutions.taxi5.API.Taxi5SDKEntity.AmountData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.OrderData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.OrderPayments;
import com.isolutions.taxi5.API.Taxi5SDKEntity.OrderStatusType;
import com.isolutions.taxi5.API.Taxi5SDKEntity.ProfileData;
import com.isolutions.taxi5.API.Taxi5SDKEntity.TokenData;
import com.isolutions.taxi5.APIAssist.ApiAssistFactory;
import com.isolutions.taxi5.APIAssist.AssistCardsHolder;
import com.isolutions.taxi5.APIAssist.AssistSDK;
import com.isolutions.taxi5.APIAssist.Entities.AssistCustomerInfo;
import com.isolutions.taxi5.APIAssist.Entities.AssistOrderStatusResponseData;
import com.isolutions.taxi5.APIAssist.Entities.AssistStoredCardData;
import com.wang.avi.AVLoadingIndicatorView;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by fedar.trukhan on 22.08.16.
 */

public class FragmentStatusPayment extends StatusesBaseFragment
implements AdapterView.OnItemClickListener{

    @BindView(R.id.fragment_status_order_payment_loading_progress_text_view)
    TextView paymentHelpTextView;

    @BindView(R.id.fragment_status_order_payment_price_progress_bar)
    AVLoadingIndicatorView pendingPriceProgressBar;

    @BindView(R.id.fragment_status_order_payment_price_progress_bar_text)
    TextView pendingPriceProgressBarText;

    @BindView(R.id.fragment_status_order_payment_price_new)
    TextView priceNewTextView;

    @BindView(R.id.fragment_status_order_payment_price_old)
    TextView priceOldTextView;

    @BindView(R.id.fragment_status_order_payment_from_text_view)
    TextView fromTextView;

    @BindView(R.id.fragment_status_order_payment_to_text_view)
    TextView toTextView;

    @BindView(R.id.fragment_status_order_payment_list_view_main)
    ConstraintLayout cardsListViewMain;

    @BindView(R.id.fragment_status_order_payment_list_view)
    ListView cardsListView;

    @BindView(R.id.fragment_status_order_payment_pay_with_card_btn)
    Button payWithCardButton;

    @BindView(R.id.fragment_status_order_payment_header_bottom)
    TextView payWithCardText;

    @BindView(R.id.fragment_status_order_payment_loading_progress_bar)
    ConstraintLayout paymentInProcessIndicatorView;

    @BindView(R.id.fragment_status_order_payment_price_separator)
    View currencysSeparator;

    public boolean isCardsShowing = false;
    AdapterPaymentCards adapterCards;

    AlertDialog dialog = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View paymentWaiting = inflater.inflate(R.layout.fragment_status_payment, container, false);
        ButterKnife.bind(this, paymentWaiting);

        adapterCards = new AdapterPaymentCards(AppData.getInstance().getAppContext(), AssistCardsHolder.GetSuccessCards());

        adapterCards.hasOneClick = AssistCardsHolder.GetOneClickState();
        adapterCards.updateResource(AssistCardsHolder.GetSuccessCards(), AssistCardsHolder.GetOneClickState());


        adapterCards.isChoosingPaymentCard = true;
        cardsListView.setAdapter(adapterCards);
        cardsListView.setFocusable(true);
        cardsListView.setClickable(true);
        cardsListView.setOnItemClickListener(this);

        return paymentWaiting;
    }

    @Override
    public void onItemClick(final AdapterView<?> parent, View view, final int position, long id) {
        AlertDialog.Builder builder;
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(AppData.getInstance().mainActivity);
        }
        else {
            builder = new AlertDialog.Builder(AppData.getInstance().mainActivity, android.R.style.Theme_Material_Light_Dialog_Alert);
        }

        builder.setTitle(AppData.getInstance().getAppContext().getString(R.string.assist_payment_with_card_question));
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Double payAmount = 0.0;

                OrderData order = AppData.getInstance().getCurrentOrder();

                if(order.status != null && order.status.status != null) {
                    if (order.status.status == OrderStatusType.OrderPendingPayment) {
                        if (order.amount != null) {
                            Long bynAmount = null;
                            Long byrAmount = null;
                            for (AmountData amountItem : order.amount) {
                                if (amountItem.currency.equalsIgnoreCase("byr")) {
                                    byrAmount = amountItem.value;
                                }
                                if (amountItem.currency.equalsIgnoreCase("byn")) {
                                    bynAmount = amountItem.value;
                                }
                            }
                            if (order.amountPaid != null) {
                                Long bynAmountPaid = null;
                                Long byrAmountPaid = null;

                                for (AmountData amountItem : order.amountPaid) {
                                    if (amountItem.currency.equalsIgnoreCase("byr")) {
                                        byrAmountPaid = amountItem.value;
                                    }
                                    if (amountItem.currency.equalsIgnoreCase("byn")) {
                                        bynAmountPaid = amountItem.value;
                                    }
                                }

                                if (bynAmount != null) {
                                    if (bynAmountPaid != null) {
                                        bynAmount -= bynAmountPaid;
                                    } else if (byrAmountPaid != null) {
                                        bynAmount -= byrAmountPaid / 100;
                                    }
                                }
                                if (byrAmount != null) {
                                    if (byrAmountPaid != null) {
                                        byrAmount -= byrAmountPaid;
                                    } else if (bynAmountPaid != null) {
                                        byrAmount -= bynAmountPaid * 100;
                                    }
                                }
                            }
                            if(bynAmount != null || byrAmount != null) {
                                if(bynAmount != null) {
                                    payAmount = bynAmount.doubleValue()/100.0;
                                }
                                else if(byrAmount != null) {
                                    payAmount = byrAmount.doubleValue()/10000.0;
                                }
                            }
                        }
                    }
                }

                final Double bynAmount = payAmount;
                final AssistStoredCardData card =(AssistStoredCardData)parent.getItemAtPosition(position);
                if(card == null) {
                    HideCardsList();
                    final Taxi5SDK taxi5SDK = ApiFactory.getTaxi5SDK();
                    if(taxi5SDK == null) {
                        return;
                    }
                    else {
                        Double copsAmount = (payAmount * 100.0);

                        String currentDateString = ((Long) (System.currentTimeMillis() / 1000)).toString();
                        ProfileData profileData = ProfileData.getInstance();

                        final String paymentIdentity = "taxi5_" + currentDateString + "_" + profileData.getMsid() + "_OneClick_Android";

                        Call<Void> checkCall = taxi5SDK.CheckPaymentRequest(TokenData.getInstance().getToken(), order.id,
                                AppData.payments_provider, paymentIdentity, copsAmount.intValue(), AppData.oneClickMerchantID);

                        checkCall.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if(response.isSuccessful()) {
                                    AlertDialog.Builder alert = new AlertDialog.Builder(AppData.getInstance().mainActivity);
                                    alert.setTitle(getString(R.string.payments_web_view_pay_one_click_title));

                                    LinearLayout wrapper = new LinearLayout(AppData.getInstance().mainActivity);
                                    EditText keyboardHack = new EditText(AppData.getInstance().mainActivity);


                                    WebView wv = new WebView(AppData.getInstance().mainActivity);

                                    AssistCustomerInfo info = AssistCustomerInfo.getInstance();

                                    String currentDateString = ((Long)(System.currentTimeMillis()/1000)).toString();
                                    ProfileData profileData = ProfileData.getInstance();

                                    String postData = "Merchant_ID="+AppData.oneClickMerchantID+"&OrderNumber="+paymentIdentity+"&OrderAmount="+bynAmount
                                            +"&CustomerNumber="+ProfileData.getInstance().getAssistCustomerNumber()
                                            +"&OrderComment="+getString(R.string.assist_payment_comment)+"&OrderCurrency="+"BYN";

                                    if(!TextUtils.isEmpty(info.getCustomerEmail())) {
                                        postData += "&Email="+info.getCustomerEmail();
                                    }
                                    if(!TextUtils.isEmpty(info.getCustomerName())) {
                                        postData += "&Firstname="+info.getCustomerName();
                                    }
                                    if(!TextUtils.isEmpty(info.getCustomerFamilyName())) {
                                        postData += "&Lastname="+info.getCustomerFamilyName();
                                    }

                                    ByteBuffer bb = Charset.forName("UTF-16").encode(postData);


                                    wv.setWebViewClient(new WebViewClient() {
                                        private boolean isRedirected;

                                        @Override
                                        public void onPageStarted(WebView view, String url, Bitmap favicon) {
                                            if(url.contains("testreturn2.taxi")) {
                                                Log.d("taxi5", "paymentOK");
                                                view.stopLoading();
                                                HideWebView();
                                                return;

                                            }
                                            else if (url.contains("testreturn.taxi")) {
                                                Log.d("taxi5", "payment error");
                                                Toast.makeText(AppData.getInstance().mainActivity, "Платеж был отклонен.", Toast.LENGTH_SHORT).show();
                                                view.stopLoading();
                                                HideWebView();
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
                                    wv.setWebChromeClient(new WebChromeClient());
                                    wv.getSettings().setDomStorageEnabled(true);
                                    wv.getSettings().setJavaScriptEnabled(true);

                                    alert.setView(wv);
                                    alert.setNegativeButton(getString(R.string.status_car_on_way_call_driver_dialog_cancel), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.dismiss();
                                            Call<Void> cancelCheckCall = taxi5SDK.CancelCheckPaymentRequest(TokenData.getInstance().getToken(), AppData.payments_provider,
                                                    paymentIdentity);
                                            cancelCheckCall.enqueue(new Callback<Void>() {
                                                @Override
                                                public void onResponse(Call<Void> call, Response<Void> response) {
                                                    Log.d("taxi5", "cancelcode: "+response.code());
                                                }

                                                @Override
                                                public void onFailure(Call<Void> call, Throwable t) {
                                                    Log.d("taxi5", "Cancel FAIL");
                                                }
                                            });

                                        }
                                    });

                                    wrapper.setOrientation(LinearLayout.VERTICAL);
                                    wrapper.addView(wv, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                                    wrapper.addView(keyboardHack, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                                    keyboardHack.setVisibility(View.GONE);


                                    alert.setView(wrapper);
                                    dialog = alert.create();
                                    dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                                        @Override
                                        public void onShow(DialogInterface arg0) {
                                            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(AppData.getInstance().getColor(R.color.defaultBlue));
                                        }
                                    });

                                    dialog.setOnKeyListener(new Dialog.OnKeyListener() {

                                        @Override
                                        public boolean onKey(DialogInterface arg0, int keyCode,
                                                             KeyEvent event) {
                                            if (keyCode == KeyEvent.KEYCODE_BACK) {
                                                dialog.dismiss();
                                                Call<Void> cancelCheckCall = taxi5SDK.CancelCheckPaymentRequest(TokenData.getInstance().getToken(), AppData.payments_provider,
                                                        paymentIdentity);
                                                cancelCheckCall.enqueue(new Callback<Void>() {
                                                    @Override
                                                    public void onResponse(Call<Void> call, Response<Void> response) {
                                                        Log.d("taxi5", "cancelcode: "+response.code());
                                                    }

                                                    @Override
                                                    public void onFailure(Call<Void> call, Throwable t) {
                                                        Log.d("taxi5", "Cancel FAIL");
                                                    }
                                                });
                                            }
                                            else {
                                                return false;
                                            }
                                            return true;
                                        }
                                    });

                                    dialog.show();
                                    wv.postUrl("https://pay140.paysec.by/pay/order.cfm", bb.array());
                                }
                                else {
                                    Toast.makeText(AppData.getInstance().mainActivity, "Ошибка запроса проверки карты на сервере Такси Пятница", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Toast.makeText(AppData.getInstance().mainActivity, "Ошибка запроса проверки карты на сервере Такси Пятница", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
                else if (card != null) {

                    //region recurrent pay
                    HideCardsList();
                    Taxi5SDK taxi5SDK = ApiFactory.getTaxi5SDK();
                    if(taxi5SDK == null) {
                        return;
                    }
                    else {
                        Double copsAmount = (payAmount * 100.0);

                        String currentDateString = ((Long)(System.currentTimeMillis()/1000)).toString();
                        ProfileData profileData = ProfileData.getInstance();

                        final String paymentIdentity = "taxi5_"+currentDateString+"_"+profileData.getMsid()+"_Recurrent_Android";

                        Call<Void> checkCall = taxi5SDK.CheckPaymentRequest(TokenData.getInstance().getToken(), order.id,
                                AppData.payments_provider, paymentIdentity, copsAmount.intValue(), AppData.reccurentMerchantID);

                        checkCall.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if(response.isSuccessful()) {
                                    AssistSDK assistSDK = ApiAssistFactory.getAssistSDK();
                                    if(assistSDK == null) {
                                        return;
                                    }
                                    Call<AssistOrderStatusResponseData> assist_call = assistSDK.PayReccurent(card.initBillNumber, paymentIdentity,
                                            AppData.reccurentMerchantID, AppData.assist_login, AppData.assist_pass,
                                            bynAmount, "BYN", "3");
                                    assist_call.enqueue(new Callback<AssistOrderStatusResponseData>() {
                                        @Override
                                        public void onResponse(Call<AssistOrderStatusResponseData> call, Response<AssistOrderStatusResponseData> response) {
                                            if(response.isSuccessful()) {
                                                if(response.body() != null && response.body().getOrdersList() != null
                                                        && !response.body().getOrdersList().isEmpty()) {
                                                    String message = response.body().getOrdersList().get(0).getCustomermessage();
                                                    if(message != null) {
                                                        Toast.makeText(AppData.getInstance().mainActivity, message, Toast.LENGTH_SHORT).show();
                                                    }
                                                    else {
                                                        Toast.makeText(AppData.getInstance().mainActivity, "Ошибка валидации данных, попробуйте повторить запрос позже.", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }
                                            else {
                                                if(response.body() != null && response.body().getOrdersList() != null
                                                        && !response.body().getOrdersList().isEmpty()) {
                                                    String message = response.body().getOrdersList().get(0).getCustomermessage();
                                                    if(message != null) {
                                                        Toast.makeText(AppData.getInstance().mainActivity, message, Toast.LENGTH_SHORT).show();
                                                    }
                                                    else {
                                                        Toast.makeText(AppData.getInstance().mainActivity, "Ошибка валидации данных, попробуйте повторить запрос позже.", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                                else {
                                                    Toast.makeText(AppData.getInstance().mainActivity, "Ошибка валидации данных, попробуйте повторить запрос позже.", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<AssistOrderStatusResponseData> call, Throwable t) {
                                            Log.d("taxi5", "Fail to pay");
                                            Toast.makeText(AppData.getInstance().mainActivity, "Ошибка запроса к серверу Assist.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                                else {
                                    Toast.makeText(AppData.getInstance().mainActivity, "Ошибка запроса проверки карты на сервере Такси Пятница", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Toast.makeText(AppData.getInstance().mainActivity, "Ошибка запроса проверки карты на сервере Такси Пятница", Toast.LENGTH_SHORT).show();
                            }
                        });



                    }
                }
                //endregion
                else {
                    HideCardsList();
                    Toast.makeText(AppData.getInstance().mainActivity, "Ошибка выбора карты", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        builder.create().show();
    }

    @Override
    public void onStart() {
        super.onStart();
        isCardsShowing = false;
        if(adapterCards != null) {
            adapterCards.hasOneClick = AssistCardsHolder.GetOneClickState();
            adapterCards.updateResource(AssistCardsHolder.GetSuccessCards(), AssistCardsHolder.GetOneClickState());
        }
    }

    public void HideCardsList() {
        isCardsShowing = false;
        cardsListViewMain.setVisibility(View.INVISIBLE);
    }

    public void ShowCardsList() {
        isCardsShowing = true;
        cardsListViewMain.setVisibility(View.VISIBLE);
    }

    public void DisablePaymentButton() {
        payWithCardButton.setClickable(false);
        payWithCardButton.setBackground(AppData.getInstance().getMyDrawable(R.drawable.round_shape_white_bordered_btn));
        payWithCardButton.setTextColor(AppData.getInstance().getColor(R.color.registrationColorHeader2));
        payWithCardButton.setVisibility(View.VISIBLE);
        payWithCardText.setVisibility(View.VISIBLE);
//        payWithCardButton.setVisibility(View.INVISIBLE);
//        payWithCardText.setVisibility(View.INVISIBLE);
    }

    public void EnablePaymentButton() {
        payWithCardButton.setClickable(true);
        payWithCardButton.setBackground(AppData.getInstance().getMyDrawable(R.drawable.round_shape_blue_btn));
        payWithCardButton.setTextColor(AppData.getInstance().getColor(R.color.defaultWhite));
        payWithCardButton.setVisibility(View.VISIBLE);
        payWithCardText.setVisibility(View.VISIBLE);
    }

    @Override
    public void fillWithOrder() {
        if((AssistCardsHolder.GetCards() != null && !AssistCardsHolder.GetCards().isEmpty()) || AssistCardsHolder.GetOneClickState()) {
            EnablePaymentButton();
        }
        else {
            DisablePaymentButton();
        }

        if(AppData.getInstance().getCurrentOrder() != null) {
            OrderData order = AppData.getInstance().getCurrentOrder();
            if(order.payments != null && !order.payments.isEmpty()) {
                boolean needShowProgress = false;
                for (OrderPayments item: order.payments) {
                    if(item.getState().equalsIgnoreCase("pending")) {
                        needShowProgress = true;
                    }
                }
                if(needShowProgress) {
                    ShowLoadingIndicator();
                }
                else {
                    HideLoadingIndicator();
                }
            }

            if(order.status.status != OrderStatusType.OrderPendingPayment) {
                DisablePaymentButton();
            }

            if(order.from != null) {
                fromTextView.setText(order.from.getStringDescription());
            }
            else {
                fromTextView.setText("");
            }
            if(order.to != null) {
                toTextView.setTextColor(AppData.getInstance().getColor(R.color.defaultBlack));
                toTextView.setText(order.to.getStringDescription());
            }
            else {
                toTextView.setTextColor(AppData.getInstance().getColor(R.color.registrationColorHeader2));
                toTextView.setText(getString(R.string.status_payment_wait_to_no_place));
            }
            if(order.status != null && order.status.status != null) {
                if(order.status.status == OrderStatusType.OrderPendingPayment) {
                    if(order.amount != null) {
                        Long bynAmount = null;
                        Long byrAmount = null;
                        for (AmountData amountItem:order.amount) {
                            if(amountItem.currency.equalsIgnoreCase("byr")) {
                                byrAmount = amountItem.value;
                            }
                            if(amountItem.currency.equalsIgnoreCase("byn")) {
                                bynAmount = amountItem.value;
                            }
                        }
                        if(order.amountPaid != null) {
                            Long bynAmountPaid = null;
                            Long byrAmountPaid = null;

                            for (AmountData amountItem:order.amountPaid) {
                                if(amountItem.currency.equalsIgnoreCase("byr")) {
                                    byrAmountPaid = amountItem.value;
                                }
                                if(amountItem.currency.equalsIgnoreCase("byn")) {
                                    bynAmountPaid = amountItem.value;
                                }
                            }

                            if(bynAmount != null) {
                                if(bynAmountPaid != null) {
                                    bynAmount -= bynAmountPaid;
                                }
                                else if(byrAmountPaid != null) {
                                    bynAmount -= byrAmountPaid/100;
                                }
                            }
                            if(byrAmount != null) {
                                if(byrAmountPaid != null) {
                                    byrAmount -= byrAmountPaid;
                                }
                                else if(bynAmountPaid != null) {
                                    byrAmount -= bynAmountPaid*100;
                                }
                            }
                        }
                        HidePendingAmountPlaceHolder();

                        if(bynAmount != null || byrAmount != null) {
                            if(bynAmount == null) {
                                bynAmount = byrAmount / 100;
                            }
                            if(byrAmount == null) {
                                byrAmount = bynAmount * 100;
                            }
//                            String bynAmountString = (bynAmount/100)+",";
                            Long rubs = bynAmount/100;
                            Long cop = bynAmount - (bynAmount/100)*100;
                            String copString;
                            if(cop <= 0) {
                                copString = "00";
                            }
                            else if(cop < 10) {
                                copString = "0" + cop;
                            }
                            else {
                                copString = "" + cop;
                            }

                            String bynAmountString = rubs + "," + copString + " " + getString(R.string.new_currency);
                            priceNewTextView.setText(bynAmountString);

                            String byrAmountString = (byrAmount/1000) + " ";

                            String oldValueCopsSctring =""+(byrAmount - byrAmount/1000*1000);
                            while(oldValueCopsSctring.length() < 3) {
                                oldValueCopsSctring+="0";
                            }
                            byrAmountString += oldValueCopsSctring + " " + getString(R.string.old_currency);

                            priceOldTextView.setText(byrAmountString);
                        }
                        else {
                            ShowPendingAmountPlaceHolder();
                        }

                    }
                }
                else {
                    ShowPendingAmountPlaceHolder();

                }
            }
            else {
                ShowPendingAmountPlaceHolder();
            }
        }
        else {
            HideLoadingIndicator();
        }
    }

    @OnClick(R.id.fragment_status_order_payment_pay_with_card_btn)
    void OnPayWithCardButtonClick() {
        ShowCardsList();
    }

    void ShowLoadingIndicator() {
        paymentInProcessIndicatorView.setVisibility(View.VISIBLE);
        animatePendingPaymentTextChange();
    }

    void HideLoadingIndicator() {
        paymentInProcessIndicatorView.setVisibility(View.INVISIBLE);
        if(helpTextChangeTimer != null) {
            helpTextChangeTimer.cancel();
        }
    }

    CountDownTimer helpTextChangeTimer;
    private int helpTextDotsCount = 1;
    private void animatePendingPaymentTextChange() {
        if(helpTextChangeTimer != null) {
            helpTextChangeTimer.cancel();
        }
        helpTextChangeTimer = new CountDownTimer(10000000, 700) {

            @Override
            public void onTick(long l) {
                helpTextDotsCount += 1;
                if(helpTextDotsCount > 3) {
                    helpTextDotsCount = 1;
                }

                String dots = "";
                for(int i = 0; i < helpTextDotsCount; i++) {
                    dots += ".";
                }
                String helpString = getString(R.string.status_payment_wait_pay_help_text) + dots;
                paymentHelpTextView.setText(helpString);
            }

            @Override
            public void onFinish() {
            }
        };
        helpTextChangeTimer.start();
    }

    void HideWebView() {
        if(dialog != null) {
            dialog.dismiss();
        }
    }

    private void animateWaitAmountTextChange() {
        if(helpTextChangeTimer != null) {
            helpTextChangeTimer.cancel();
        }
        helpTextChangeTimer = new CountDownTimer(10000000, 700) {

            @Override
            public void onTick(long l) {
                helpTextDotsCount += 1;
                if(helpTextDotsCount > 3) {
                    helpTextDotsCount = 1;
                }

                String dots = "";
                for(int i = 0; i < helpTextDotsCount; i++) {
                    dots += ".";
                }
                String helpString = getString(R.string.payments_wait_for_amount) + dots;
                pendingPriceProgressBarText.setText(helpString);
            }

            @Override
            public void onFinish() {
            }
        };
        helpTextChangeTimer.start();
    }

    @Override
    public void onDestroy() {
        if(helpTextChangeTimer != null) {
            helpTextChangeTimer.cancel();
        }
        super.onDestroy();
    }


    void ShowPendingAmountPlaceHolder() {
        pendingPriceProgressBar.setVisibility(View.VISIBLE);
        pendingPriceProgressBarText.setVisibility(View.VISIBLE);
        currencysSeparator.setVisibility(View.INVISIBLE);
        priceNewTextView.setText("");
        priceOldTextView.setText("");
        animateWaitAmountTextChange();
    }

    void HidePendingAmountPlaceHolder() {
        pendingPriceProgressBar.setVisibility(View.INVISIBLE);
        pendingPriceProgressBarText.setVisibility(View.INVISIBLE);
        currencysSeparator.setVisibility(View.VISIBLE);
        if(helpTextChangeTimer != null) {
            helpTextChangeTimer.cancel();
        }
    }
}