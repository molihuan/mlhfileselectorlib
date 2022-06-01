package com.zlylib.mlhfileselectorlib.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.zlylib.mlhfileselectorlib.R;
import com.zlylib.mlhfileselectorlib.interfaces.IActivityAndFragment;


/**
 * author: molihuan
 * 2022.6.1
 * 通用返回toolbar  fragment
 */
public class BackToolbarFragment extends Fragment implements View.OnClickListener {
    private String title="";
    private String subtitle="";
    private View view;
    private ImageView btn_back_toolbar;
    private Activity activity;
    private TextView main_title_toolbar;
    private TextView subtitle_toolbar;
    private TextView btn_choose_toolbar;
    private IActivityAndFragment IMainActivity;//定义activity与fragment通信接口



    public BackToolbarFragment() {
        // Required empty public constructor
    }

    public BackToolbarFragment(String title) {
        this.title=title;
    }
    public BackToolbarFragment(String title, String subtitle) {
        this.title=title;
        this.subtitle=subtitle;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view==null){
            view = inflater.inflate(R.layout.fragment_back_toolbar, container, false);
            getComponents();//获取组件
            setListeners();//设置监听
            initData();//初始化数据
        }
        return view;
    }

    /**
     * 设置按钮是否可见
     * @param type
     */
    public void setBtnChooseVisibility(int type){
        if (type==View.VISIBLE){
            btn_choose_toolbar.setVisibility(View.VISIBLE);
        }else if (type==View.INVISIBLE){
            btn_choose_toolbar.setVisibility(View.INVISIBLE);
        }
    }

    private void initData() {
        setTitles(title,subtitle);
    }

    /**
     * 设置标题
     * @param title
     * @param subtitle
     */
    public void setTitles(String title, String subtitle) {
        main_title_toolbar.setText(title);
        if (subtitle.equals("")) {//如果副标题为空则移除它
            ((RelativeLayout)subtitle_toolbar.getParent()).removeView(subtitle_toolbar);
        }else {
            subtitle_toolbar.setText(subtitle);
        }
    }



    private void setListeners() {
        btn_back_toolbar.setOnClickListener(this);
        btn_choose_toolbar.setOnClickListener(this);
    }

    private void getComponents() {
        btn_back_toolbar=view.findViewById(R.id.btn_back_toolbar);
        main_title_toolbar=view.findViewById(R.id.main_title_toolbar);
        subtitle_toolbar=view.findViewById(R.id.subtitle_toolbar);
        btn_choose_toolbar=view.findViewById(R.id.btn_choose_toolbar);
    }
    @Override
    public void onClick(View v) {
        int ID=v.getId();

        if (activity == null) {
            activity = getActivity();
        }

        if (ID == R.id.btn_back_toolbar) {
            IMainActivity.invokeFuncAiF(R.id.btn_back_toolbar);
        }else if (ID == R.id.btn_choose_toolbar){
            IMainActivity.invokeFuncAiF(R.id.btn_choose_toolbar);
        }
    }

    /**
     * 当Activity和Fragment产生关系时调用
     * context可以强转为Activity
     * @param context
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (activity==null){
            activity= getActivity();
        }

        try {
            //获取通信接口实例
            IMainActivity= (IActivityAndFragment) context;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("interfaceError","Activity必须实现IActivityAndFragment接口");
        }
    }
    /**
     * 当Activity和Fragment脱离时调用
     */
    @Override
    public void onDetach() {
        super.onDetach();
    }

}