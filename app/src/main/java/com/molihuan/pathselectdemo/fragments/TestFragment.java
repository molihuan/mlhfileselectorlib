package com.molihuan.pathselectdemo.fragments;

import android.view.View;
import android.widget.Button;

import com.molihuan.pathselectdemo.R;
import com.molihuan.pathselector.fragment.AbstractFragment;
import com.molihuan.pathselector.utils.Mtools;

public class TestFragment extends AbstractFragment {
    @Override
    public int setFragmentViewId() {
        return R.layout.test_fragment;
    }

    @Override
    public void getComponents(View view) {
        Button btn = view.findViewById(R.id.test_btn1);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Mtools.toast("66666");
            }
        });
    }
}
