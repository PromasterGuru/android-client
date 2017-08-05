package com.neeru.client.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;

/**
 * Created by brajendra on 05/08/17.
 */

public class DialogHelper {

    private Snackbar snackbar;
    private ProgressDialog pd;


    public void showProgressDialog(Context context, String message) {
        pd = new ProgressDialog(context);
        pd.setCanceledOnTouchOutside(false);
        pd.setMessage(message);
        pd.show();

    }

    public void isCancelable(boolean isCancelable) {
        pd.setCancelable(isCancelable);
    }


    public void hideProgressDialog() {
        if (pd != null) {
            pd.dismiss();
            pd = null;
        }
    }


    public void showSnackBar(String message, View mRoot) {
        try {
            if (snackbar == null) {
                snackbar = Snackbar
                        .make(mRoot, message, Snackbar.LENGTH_LONG);
            } else {
                snackbar.setText(message);
            }

            snackbar.show();
        } catch (Exception ex) {

        }

    }

}
