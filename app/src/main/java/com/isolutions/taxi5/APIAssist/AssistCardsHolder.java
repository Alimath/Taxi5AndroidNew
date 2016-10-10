package com.isolutions.taxi5.APIAssist;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.isolutions.taxi5.APIAssist.Entities.AssistStoredCardData;

import java.util.ArrayList;

import io.paperdb.Paper;

/**
 * Created by fedar.trukhan on 03.10.16.
 */

public class AssistCardsHolder {
    @Nullable
    public static ArrayList<AssistStoredCardData> GetCards() {
        ArrayList<AssistStoredCardData> storedCardData = Paper.book().read("taxi5AndroidCardsData");

        if(storedCardData != null) {
            return storedCardData;
        }
        return null;
    }

    @Nullable
    public static ArrayList<AssistStoredCardData> GetSuccessCards() {
        ArrayList<AssistStoredCardData> allCards = Paper.book().read("taxi5AndroidCardsData");
        if(allCards != null) {
            ArrayList<AssistStoredCardData> successCards = new ArrayList<AssistStoredCardData>();
            for (AssistStoredCardData cardItem: allCards) {
                if(cardItem.initBillResponseCode.equalsIgnoreCase("AS000")) {
                    successCards.add(cardItem);
                }
            }
            if(successCards.size() > 0) {
                return successCards;
            }
            else {
                return null;
            }
        }


        return null;
    }

    public static boolean AddCard(AssistStoredCardData cardData) {
        if(cardData == null) {
            return false;
        }
        ArrayList<AssistStoredCardData> storedCardData = Paper.book().read("taxi5AndroidCardsData");
        if(storedCardData == null) {
            storedCardData = new ArrayList<AssistStoredCardData>();
        }
        storedCardData.add(cardData);
        Paper.book().delete("taxi5AndroidCardsData");
        Paper.book().write("taxi5AndroidCardsData", storedCardData);
        return true;
    }

    public static boolean RemoveCard(int index) {
        ArrayList<AssistStoredCardData> storedCardData = Paper.book().read("taxi5AndroidCardsData");
        storedCardData.remove(index);
        Paper.book().delete("taxi5AndroidCardsData");
        Paper.book().write("taxi5AndroidCardsData", storedCardData);
        return true;
    }

    public static boolean RemoveOneClick() {
        Paper.book().write("taxi5AndroidCardsDataOneClick", false);
        return true;
    }

    public static boolean AddOneClick() {
        Paper.book().write("taxi5AndroidCardsDataOneClick", true);
        return true;
    }

    public static boolean GetOneClickState() {
        if(Paper.book().read("taxi5AndroidCardsDataOneClick") != null) {
            return Paper.book().read("taxi5AndroidCardsDataOneClick");
        }
        else {
            return false;
        }
    }

    public static boolean UpdateCard(AssistStoredCardData cardData) {
        if(cardData == null) {
            return false;
        }

        ArrayList<AssistStoredCardData> storedCardData = Paper.book().read("taxi5AndroidCardsData");
        if(storedCardData == null) {
            return false;
        }
        else {
            int updateIndex = -1;
            for (AssistStoredCardData card: storedCardData) {
                if(card.initBillNumber.equalsIgnoreCase(cardData.initBillNumber)) {
                    updateIndex = storedCardData.indexOf(card);
                }
            }
            if(updateIndex > 0 && updateIndex < storedCardData.size()) {
                storedCardData.set(updateIndex, cardData);
                Paper.book().delete("taxi5AndroidCardsData");
                Paper.book().write("taxi5AndroidCardsData", storedCardData);
                return true;
            }
            else {
                return false;
            }
        }
    }
}
