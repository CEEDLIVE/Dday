package com.example.ceedlive.dday.validation;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.example.ceedlive.dday.R;

public class ValidationHelper {

    private Context context;

    /**
     * constructor
     * @param context
     */
    public ValidationHelper(Context context) {
        this.context = context;
    }

    /**
     * method to check EditText filled .
     * @param editText
     * @param textInputLayout
     * @param message
     * @return
     */
    public boolean isEditTextFilled(EditText editText, TextInputLayout textInputLayout, String message) {
        String value = editText.getText().toString().trim();
        if ( value.isEmpty() ) {
            textInputLayout.setError(message);
            hideKeyboardFrom(editText);
            return false;
        } else {
            textInputLayout.setErrorEnabled(false);
        }
        return true;
    }

    /**
     * method to check valid email inEditText .
     * @param editText
     * @param textInputLayout
     * @param message
     * @return
     */
    public boolean isEditTextEmail(EditText editText, TextInputLayout textInputLayout, String message) {
        String value = editText.getText().toString().trim();
        if ( value.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(value).matches() ) {
            textInputLayout.setError(message);
            hideKeyboardFrom(editText);
            return false;
        } else {
            textInputLayout.setErrorEnabled(false);
        }
        return true;
    }


    /**
     * method to Hide keyboard
     * @param view
     */
    private void hideKeyboardFrom(View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

}