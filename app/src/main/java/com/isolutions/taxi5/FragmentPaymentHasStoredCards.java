package com.isolutions.taxi5;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import android.widget.ListView;

import com.isolutions.taxi5.API.Taxi5SDKEntity.ProfileData;
import com.isolutions.taxi5.APIAssist.ApiAssistFactory;
import com.isolutions.taxi5.APIAssist.AssistCardsHolder;
import com.isolutions.taxi5.APIAssist.AssistSDK;
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
import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by fedar.trukhan on 03.10.16.
 */

public class FragmentPaymentHasStoredCards extends Fragment {

    AdapterPaymentCards adapterCards;
    LayoutInflater inflater;

    AlertDialog dialog;

    @BindView(R.id.fragment_payments_has_stored_cards_list_view)
    ListView listView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(AppData.getInstance().toolbar != null) {
            AppData.getInstance().toolbar.ConvertToDefaultWithTitle(getString(R.string.left_drawer_menu_item_payments));
        }
        if(dialog != null) {
            dialog.dismiss();
        }
        if(adapterCards != null) {
            ArrayList<AssistStoredCardData> storedCardDatas = new ArrayList<AssistStoredCardData>();

            adapterCards.hasOneClick = AssistCardsHolder.GetOneClickState();
            adapterCards.updateResource(AssistCardsHolder.GetCards(), AssistCardsHolder.GetOneClickState());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payments_has_stored_cards, container, false);
        ButterKnife.bind(this, view);
        this.inflater = inflater;
        if(AppData.getInstance().toolbar != null) {
            AppData.getInstance().toolbar.ConvertToDefaultWithTitle(getString(R.string.left_drawer_menu_item_payments));
        }

        adapterCards = new AdapterPaymentCards(AppData.getInstance().getAppContext(), AssistCardsHolder.GetCards());

        adapterCards.hasOneClick = AssistCardsHolder.GetOneClickState();
        adapterCards.updateResource(AssistCardsHolder.GetCards(), AssistCardsHolder.GetOneClickState());
        listView.setAdapter(adapterCards);

//        LayoutInflater inflater = mainActivity.getLayoutInflater();
        View titleView = inflater.inflate(R.layout.payments_table_footer, null);
        listView.addFooterView(titleView);

        return view;
    }

    @OnClick(R.id.fragment_payments_has_stored_cards_add_card_button)
    void onAddCardClick() {
        AppData.getInstance().ShowChoosingCardTypeDialog();
    }

    public void UpdateListView() {
        adapterCards.updateResource(AssistCardsHolder.GetCards(), AssistCardsHolder.GetOneClickState());
    }
}
