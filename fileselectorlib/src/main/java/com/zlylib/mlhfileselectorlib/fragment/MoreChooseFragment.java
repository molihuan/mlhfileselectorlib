package com.zlylib.mlhfileselectorlib.fragment;

import android.view.View;
import android.widget.Button;

import com.zlylib.mlhfileselectorlib.R;


/**
 * author: molihuan
 * 2022.6.1
 * 多选按钮Fragment
 */
public class MoreChooseFragment extends BaseFragment {

    private Button btn_both_noboth;
    private Button btn_more_ok;

    public MoreChooseFragment() {
        super(R.layout.fragment_show_hidden_morechoose_ml);
    }


    @Override
    public void initData(View view) {

    }

    @Override
    public void setListeners(View view) {
        btn_both_noboth.setOnClickListener(this);
        btn_more_ok.setOnClickListener(this);
    }

    @Override
    public void getComponents(View view) {
        btn_both_noboth=view.findViewById(R.id.btn_both_noboth);
        btn_more_ok=view.findViewById(R.id.btn_more_ok);
    }


    /**
     * fragment隐藏显示监听
     * @param hidden
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        if (hidden){
            mIActivityAndFragment.invokeFuncAiF(00011);
        }else {
            mIActivityAndFragment.invokeFuncAiF(00012);
        }
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_both_noboth) {//全选按钮
            if (btn_both_noboth.getText().equals("全选")){
                mIActivityAndFragment.invokeFuncAiF(00011);
                btn_both_noboth.setText("取消全选");
            }else {
                mIActivityAndFragment.invokeFuncAiF(00012);
                btn_both_noboth.setText("全选");
            }
        } else if (id == R.id.btn_more_ok) {//确定按钮
            mIActivityAndFragment.invokeFuncAiF(R.id.btn_more_ok);
        }
    }



}
