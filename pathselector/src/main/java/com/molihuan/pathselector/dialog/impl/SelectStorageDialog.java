package com.molihuan.pathselector.dialog.impl;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.molihuan.pathselector.R;
import com.molihuan.pathselector.adapter.StorageListAdapter;
import com.molihuan.pathselector.dialog.BaseDialog;
import com.molihuan.pathselector.entity.StorageBean;
import com.molihuan.pathselector.utils.ReflectTools;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: SelectStorageDialog
 * @Author: molihuan
 * @Date: 2022/12/11/14:23
 * @Description:
 */
public class SelectStorageDialog extends BaseDialog implements OnItemClickListener {

    private TextView title;
    private TextView confirmBtn;
    private TextView cancelBtn;
    private RecyclerView recycView;

    protected List<StorageBean> storageList;
    protected StorageListAdapter storageListAdapter;
    //选择的StorageBean
    protected StorageBean selectedStorage;

    public SelectStorageDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    public int setContentViewID() {
        return R.layout.general_title_recyview_btn_mlh;
    }

    @Override
    public void getComponents() {
        title = findViewById(R.id.title_general_title_recyview_btn_mlh);
        confirmBtn = findViewById(R.id.confirm_general_title_recyview_btn_mlh);
        cancelBtn = findViewById(R.id.cancel_general_title_recyview_btn_mlh);
        recycView = findViewById(R.id.recyview_general_title_recyview_btn_mlh);
    }

    @Override
    public void initData() {
        super.initData();

        //存储数据
        storageList = new ArrayList<>();
        //通过反射的方式得到所有的存储路径（内部存储+外部存储）
        List<String> allStoragePath = ReflectTools.getAllStoragePath(mContext);
//        List<StorageVolume> storageVolumes = ReflectTools.getStorageVolumes(mContext);
//        String s = storageVolumes.get(0).getDirectory().getName();
//        Mtools.log(s);

        for (String storagePath : allStoragePath) {
            storageList.add(new StorageBean(storagePath, false));
        }

        //设置数据和监听
        recycView.setLayoutManager(new LinearLayoutManager(mContext));
        storageListAdapter = new StorageListAdapter(R.layout.general_item_tv_mlh, storageList);
        recycView.setAdapter(storageListAdapter);

    }

    @Override
    public void initView() {
        setCanceledOnTouchOutside(false);
        getWindow().setLayout(mConfigData.pathSelectDialogWidth * 92 / 100, ViewGroup.LayoutParams.WRAP_CONTENT);
        title.setText(R.string.tip_dialog_title_select_memory_path_mlh);
        confirmBtn.setText(R.string.option_confirm_mlh);
        cancelBtn.setText(R.string.option_cancel_mlh);

    }

    @Override
    public void setListeners() {
        storageListAdapter.setOnItemClickListener(this);
        confirmBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.confirm_general_title_recyview_btn_mlh) {


            //确定按钮
            if (selectedStorage != null) {
                //改变初始路径
//                psf.setInitPath(selectedStorage.getRootPath());
                //刷新
                psf.updateFileList(selectedStorage.getRootPath());
                //刷新面包屑
                psf.updateTabbarList();
            }
            dismiss();
        } else if (id == R.id.cancel_general_title_recyview_btn_mlh) {
            //取消按钮
            dismiss();
        }
    }

    @Override
    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
        if (adapter instanceof StorageListAdapter) {
            //不为null说明已经选择了，则把当前选择的item设置未选中
            if (selectedStorage != null) {
                selectedStorage.setSelected(false);
            }
            //获取当前点击新的item，并设置选中
            selectedStorage = storageList.get(position);
            selectedStorage.setSelected(true);
            //刷新
            storageListAdapter.notifyDataSetChanged();

        }
    }
}
