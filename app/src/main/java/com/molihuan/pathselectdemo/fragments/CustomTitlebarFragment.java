package com.molihuan.pathselectdemo.fragments;

import android.view.View;
import android.widget.Button;

import com.molihuan.pathselectdemo.R;
import com.molihuan.pathselector.fragment.AbstractTitlebarFragment;
import com.molihuan.pathselector.utils.Mtools;

/**
 * @ClassName CustomTitlebarFragment
 * @Author molihuan
 * @Date 2022/12/22 6:50
 */
public class CustomTitlebarFragment extends AbstractTitlebarFragment {

    private Button btn1;
    private Button btn2;

    @Override
    public int setFragmentViewId() {
        return R.layout.fragment_custom_titlebar;
    }

    @Override
    public void getComponents(View view) {
        btn1 = view.findViewById(R.id.my_btn1);
        btn2 = view.findViewById(R.id.my_btn2);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Mtools.toast("The current path is:\n" + psf.getCurrentPath());
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                psf.selectAllFile(true);
            }
        });
    }

}