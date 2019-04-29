package com.ceedlive.ceeday.dialog;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.util.TypedValue;
import android.widget.TextView;

public class DialogBuilder extends AlertDialog.Builder {
    protected Context callContext;
    protected AlertDialog showDialog;

    public AlertDialog getDialog() { return showDialog; }

    public DialogBuilder(Context context) { this(context, 0); }
    public DialogBuilder(Context context, int themeResId) {
        super(context, themeResId);
        callContext = context;
    }

    @Override public AlertDialog show() { return showDialog = applyStyle(super.show()); }

    private AlertDialog applyStyle(AlertDialog alertDialog) {
        final int titleId = android.support.v7.appcompat.R.id.alertTitle;//callContext.getResources().getIdentifier("alertTitle", "id", "android");
        final TextView titleView = alertDialog.findViewById(titleId);//titleId > 0 ? alertDialog.findViewById(titleId) : null;
        if (titleView == null) {
            return alertDialog;
        }

        // 라인 제한 해제
        titleView.setMaxLines(0);
        titleView.setSingleLine(false);

        final TypedValue typedValue = new TypedValue();
        if (callContext.getTheme().resolveAttribute(android.R.attr.alertDialogTheme, typedValue, true)) {
            if (typedValue.resourceId != 0) {
                TypedArray typedArray = callContext.obtainStyledAttributes(typedValue.resourceId, new int[] { android.R.attr.windowTitleStyle });
                if (typedArray != null && typedArray.getValue(0, typedValue)) {
                    // textSize
                    {
                        typedArray = callContext.obtainStyledAttributes(typedValue.resourceId, new int[] { android.R.attr.textSize });
                        final TypedValue outValue = new TypedValue();
                        if (typedArray != null && typedArray.getValue(0, outValue)) {
                            if (outValue.resourceId != 0) {
                                final float textSize = callContext.getResources().getDimensionPixelSize(outValue.resourceId);
                                titleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                            } else {
                                final float textSize = typedArray.getDimensionPixelSize(0, 0);
                                titleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                            }
                        }
                    }

                    // fontFamily
                    {
                        typedArray = callContext.obtainStyledAttributes(typedValue.resourceId, new int[] { android.R.attr.fontFamily });
                        final TypedValue outValue = new TypedValue();
                        if (typedArray != null && typedArray.getValue(0, outValue)) {
                            if (outValue.resourceId != 0) {
                                titleView.setTypeface(ResourcesCompat.getFont(callContext, outValue.resourceId));
                            }
                        }
                    }
                }
            }
        }

        return alertDialog;
    }
}
