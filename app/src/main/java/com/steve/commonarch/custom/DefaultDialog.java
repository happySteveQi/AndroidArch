package com.steve.commonarch.custom;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.steve.commonarch.AppEnv;
import com.steve.commonarch.R;


public class DefaultDialog extends Dialog {

    protected final float WRAP = 1;
    protected final float MATCH = 2;

    public DefaultDialog(Context context) {
        this(context, R.style.App_Dialog);
    }

    public DefaultDialog(Context context, int style) {
        super(context, style);
    }

    private View view;

    protected void setDisplaySize(float widthPercent, float heightPercent) {
        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        WindowManager wm = window.getWindowManager();
        Display d = wm.getDefaultDisplay(); // 获取屏幕宽、高用
        if (widthPercent == WRAP) {
            layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        } else if (widthPercent == MATCH) {
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        } else {
            layoutParams.width = (int) (d.getWidth() * widthPercent);
        }
        if (heightPercent == WRAP) {
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        } else if (heightPercent == MATCH) {
            layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        } else {
            layoutParams.height = (int) (d.getHeight() * heightPercent);
        }

        window.setAttributes(layoutParams);
    }

    @Override
    public void setContentView(@NonNull View view) {
        this.view = view;
        super.setContentView(view);
    }

    public View getView() {
        return view;
    }

    /**
     * 显示对话框的时候，如果正好 Activity 关闭了，那么会抛出 Window Leaked 异常导致崩溃
     */
    @Override
    public void show() {
        try {
            super.show();
        } catch (Exception e) {
            if (AppEnv.DEBUG) {
                Log.e("AbsDialog", "", e);
            }
        }
    }

    @Override
    public void dismiss() {
        if (isShowing()) {
            try {
                super.dismiss();
            } catch (Exception e) {
            }
        }
    }
}
