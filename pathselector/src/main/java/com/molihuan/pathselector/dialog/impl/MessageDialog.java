package com.molihuan.pathselector.dialog.impl;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.blankj.molihuan.utilcode.util.ScreenUtils;
import com.molihuan.pathselector.R;
import com.molihuan.pathselector.dialog.BaseDialog;
import com.molihuan.pathselector.entity.FontBean;

/**
 * @ClassName: MessageDialog
 * @Author: molihuan
 * @Date: 2022/12/13/15:45
 * @Description:
 */
public class MessageDialog extends BaseDialog {
    private TextView titleTv;
    private TextView contentTv;
    private TextView confirmTv;
    private TextView cancelTv;

    private FontBean titleFont;
    private FontBean contentFont;
    private FontBean confirmFont;
    private FontBean cancelfont;

    private IOnConfirmListener confirmListener;
    private IOnCancelListener cancelListener;

    public MessageDialog(@NonNull Context context) {
        super(context);
    }

    public MessageDialog setTitle(FontBean titleFont) {
        this.titleFont = titleFont;
        return this;
    }

    public MessageDialog setContent(FontBean contentFont) {
        this.contentFont = contentFont;
        return this;
    }

    public MessageDialog setConfirm(FontBean confirmFont, IOnConfirmListener confirmListener) {
        this.confirmFont = confirmFont;
        this.confirmListener = confirmListener;
        return this;
    }

    public MessageDialog setCancel(FontBean cancelfont, IOnCancelListener cancelListener) {
        this.cancelfont = cancelfont;
        this.cancelListener = cancelListener;
        return this;
    }

    @Override
    public int setContentViewID() {
        return R.layout.general_title_content_btn_mlh;
    }

    @Override
    public void getComponents() {
        titleTv = findViewById(R.id.title_general_title_content_btn_mlh);
        contentTv = findViewById(R.id.content_general_title_content_btn_mlh);
        confirmTv = findViewById(R.id.confirm_general_title_content_btn_mlh);
        cancelTv = findViewById(R.id.cancel_general_title_content_btn_mlh);
    }

    @Override
    public void initData() {
        super.initData();

    }

    @Override
    public void initView() {
        getWindow().setLayout(ScreenUtils.getScreenWidth() * 75 / 100, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (titleFont != null) {
            titleTv.setText(titleFont.getText());
            titleTv.setTextSize(titleFont.getSize());
            titleTv.setTextColor(titleFont.getColor());
        } else {
            titleTv.setText(R.string.default_dialog_title_mlh);
            titleTv.setTextColor(Color.BLACK);
        }
        if (contentFont != null) {
            contentTv.setText(contentFont.getText());
            contentTv.setTextSize(contentFont.getSize());
            contentTv.setTextColor(contentFont.getColor());
        }
        if (confirmFont != null) {
            confirmTv.setText(confirmFont.getText());
            confirmTv.setTextSize(confirmFont.getSize());
            confirmTv.setTextColor(confirmFont.getColor());
        }
        if (cancelfont != null) {
            cancelTv.setText(cancelfont.getText());
            cancelTv.setTextSize(cancelfont.getSize());
            cancelTv.setTextColor(cancelfont.getColor());
        }
    }

    @Override
    public void setListeners() {
        confirmTv.setOnClickListener(this);
        cancelTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.confirm_general_title_content_btn_mlh) {
            confirmListener.onClick(v, this);
        } else if (id == R.id.cancel_general_title_content_btn_mlh) {
            cancelListener.onClick(v, this);
        }
    }
}
