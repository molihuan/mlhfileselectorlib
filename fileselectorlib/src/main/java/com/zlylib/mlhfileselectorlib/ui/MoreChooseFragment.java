package com.zlylib.mlhfileselectorlib.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.zlylib.mlhfileselectorlib.R;
import com.zlylib.mlhfileselectorlib.interfaces.IActivityAndFragment;


/**
 * author: molihuan
 * 2022.6.1
 * 多选按钮Fragment
 */
public class MoreChooseFragment extends Fragment implements View.OnClickListener  {
    private View view;
    private Button btn_both_noboth;
    private Button btn_more_ok;
    private IActivityAndFragment IMainActivity;//定义activity与fragment通信接口
    private FileSelectorActivity mainActivity;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (view==null){
            view = inflater.inflate(R.layout.fragment_show_hidden_morechoose, container, false);
            getComponents();//获取组件
            setListeners();//设置监听
            initData();//初始化数据
        }
        return view;
    }

    private void initData() {

    }

    private void setListeners() {
        btn_both_noboth.setOnClickListener(this);
        btn_more_ok.setOnClickListener(this);
    }

    private void getComponents() {
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
            IMainActivity.invokeFuncAiF(00011);
        }else {
            IMainActivity.invokeFuncAiF(00012);
        }
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_both_noboth) {//全选按钮
            if (btn_both_noboth.getText().equals("全选")){
                IMainActivity.invokeFuncAiF(00011);
                btn_both_noboth.setText("取消全选");
            }else {
                IMainActivity.invokeFuncAiF(00012);
                btn_both_noboth.setText("全选");
            }


        } else if (id == R.id.btn_more_ok) {//确定按钮
            IMainActivity.invokeFuncAiF(R.id.btn_more_ok);
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
        if (mainActivity==null){
            mainActivity= (FileSelectorActivity) context;
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
