package com.molihuan.demo01.fragments;

import android.view.View;
import android.widget.TextView;

import com.molihuan.demo01.R;

public class EmptyFragment extends BaseFragment {

    private TextView tv1;

    public EmptyFragment() {
        super(R.layout.fragment_empty);
    }

    @Override
    public void initData(View view) {

    }

    @Override
    public void setListeners(View view) {
        tv1.setOnClickListener(this);
    }

    @Override
    public void getComponents(View view) {
        tv1=view.findViewById(R.id.tv1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv1:
                tv1.setVisibility(View.INVISIBLE);

                break;
        }
    }


}
