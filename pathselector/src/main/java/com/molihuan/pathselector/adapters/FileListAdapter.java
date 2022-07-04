package com.molihuan.pathselector.adapters;


import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.molihuan.pathselector.R;
import com.molihuan.pathselector.entities.FileBean;

import java.util.List;

/**
 * @ClassName FileAdapter
 * @Description TODO
 * @Author molihuan
 * @Date 2022/6/24 2:20
 */
public class FileListAdapter extends BaseQuickAdapter<FileBean, BaseViewHolder> implements LoadMoreModule {
    public FileListAdapter(List<FileBean> data) {
        super(R.layout.item_files_list_mlh, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, FileBean fileBean) {

        ImageView imgvFiletype = helper.getView(R.id.iv_file_type_fileitem);//文件类型图片
        ImageView imgvEnter = helper.getView(R.id.iv_file_enter_fileitem);//右边进入图片
        TextView tvFileName = helper.getView(R.id.tv_file_name_fileitem);//文件名称
        TextView tvFileDetail = helper.getView(R.id.tv_file_detail_fileitem);//文件或文件夹详细信息
        CheckBox checkBoxFile = helper.getView(R.id.checkbox_file_fileitem);//多选框
        //设置类型图片
        imgvFiletype.setImageResource(fileBean.getFileImgType());

        boolean isFile = fileBean.isFile();
        //设置详细信息
        if (isFile){
            tvFileDetail.setText(String.format("大小:%s", fileBean.getSize()));
        }else {
            tvFileDetail.setText(String.format("大小:%s | 文件:%s | 文件夹:%s", fileBean.getSize(),fileBean.getChildrenFileNumber(),fileBean.getChildrenDirNumber()));
        }

        //设置checkBox
        if (fileBean.isVisible()){//checkBox显示时
            checkBoxFile.setVisibility(View.VISIBLE);
            imgvEnter.setVisibility(View.INVISIBLE);
            checkBoxFile.setChecked(fileBean.isChecked());
        }else {//checkBox不显示
            checkBoxFile.setVisibility(View.INVISIBLE);
            if (!isFile){//只有当它是文件夹时才显示
                imgvEnter.setVisibility(View.VISIBLE);
            }else {
                imgvEnter.setVisibility(View.INVISIBLE);
            }
            checkBoxFile.setChecked(false);
        }



        //设置名称
        tvFileName.setText(fileBean.getFileName());



    }
}
