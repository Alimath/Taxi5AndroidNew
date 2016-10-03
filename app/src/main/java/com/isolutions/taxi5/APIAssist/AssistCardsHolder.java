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

    public static boolean AddCard(AssistStoredCardData cardData) {
        if(cardData == null) {
            return false;
        }
        ArrayList<AssistStoredCardData> storedCardData = Paper.book().read("taxi5AndroidCardsData");
        if(storedCardData == null) {
            storedCardData = new ArrayList<AssistStoredCardData>();
        }
        storedCardData.add(cardData);
        Paper.book().write("taxi5AndroidCardsData", storedCardData);
        return true;
    }

    public static boolean RemoveCard(int index) {
//        if(TextUtils.isEmpty(initBillNumber)) {
//            return false;
//        }
//        ArrayList<AssistStoredCardData> storedCardData = Paper.book().read("taxi5AndroidCardsData");
//        if(storedCardData == null) {
//            return false;
//        }
//        else {
//            int removedIndex = -1;
//            for (AssistStoredCardData card: storedCardData) {
//                if(card.initBillNumber.equalsIgnoreCase(initBillNumber)) {
//                    removedIndex = storedCardData.indexOf(card);
//                }
//            }
//            if(removedIndex > 0 && removedIndex < storedCardData.size()) {
//                storedCardData.remove(removedIndex);
//                Paper.book().write("taxi5AndroidCardsData", storedCardData);
//                return true;
//            }
//            else {
//                return false;
//            }
//        }

        ArrayList<AssistStoredCardData> storedCardData = Paper.book().read("taxi5AndroidCardsData");
        storedCardData.remove(index);
        Paper.book().write("taxi5AndroidCardsData", storedCardData);
        return true;
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
                Paper.book().write("taxi5AndroidCardsData", storedCardData);
                return true;
            }
            else {
                return false;
            }
        }
    }
}
