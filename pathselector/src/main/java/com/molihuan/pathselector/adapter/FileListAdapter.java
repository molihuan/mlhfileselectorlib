package com.molihuan.pathselector.adapter;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.molihuan.utilcode.util.StringUtils;
import com.blankj.molihuan.utilcode.util.TimeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.molihuan.pathselector.R;
import com.molihuan.pathselector.dao.SelectConfigData;
import com.molihuan.pathselector.entity.FileBean;
import com.molihuan.pathselector.fragment.AbstractFileShowFragment;
import com.molihuan.pathselector.service.impl.ConfigDataBuilderImpl;
import com.molihuan.pathselector.utils.MConstants;

import java.util.List;

/**
 * @ClassName: FileListAdapter
 * @Author: molihuan
 * @Date: 2022/11/22/21:55
 * @Description:
 */
public class FileListAdapter extends BaseQuickAdapter<FileBean, BaseViewHolder> implements LoadMoreModule {
    protected SelectConfigData mConfigData = ConfigDataBuilderImpl.getInstance().getSelectConfigData();

    private AbstractFileShowFragment fileShowFragment = mConfigData.fileShowFragment;

    public FileListAdapter(int layoutResId, @Nullable List<FileBean> data) {
        super(layoutResId, data);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void convert(@NonNull BaseViewHolder holder, FileBean fileBean) {

        LinearLayout linlContainer = holder.getView(R.id.linl_item_file_container);//总容器
        ImageView imgvIco = holder.getView(R.id.imgv_item_file_ico);//文件图标
        ImageView imgvEnter = holder.getView(R.id.imgv_item_file_enter);//右边进入图片
        TextView tvName = holder.getView(R.id.tv_item_file_name);//文件名称
        TextView tvDetail = holder.getView(R.id.tv_item_file_detail);//文件详细信息
        CheckBox checkBox = holder.getView(R.id.checkbox_item_file_choose);//多选框

        if (fileBean.getPath() == null) {
            //说明是缓存filebean
            linlContainer.setVisibility(View.GONE);//隐藏总容器
            return;
        } else if (fileBean.getSize() == MConstants.FILEBEAN_BACK_FLAG) {
            linlContainer.setVisibility(View.VISIBLE);
            //说明是返回fileBean
            imgvIco.setImageResource(fileBean.getFileIcoType());
            imgvEnter.setVisibility(View.INVISIBLE);
            tvName.setText(fileBean.getName());
            tvDetail.setText("");
            checkBox.setVisibility(View.INVISIBLE);
            checkBox.setChecked(false);
        } else {
            linlContainer.setVisibility(View.VISIBLE);
            //正常filebean
            imgvIco.setImageResource(fileBean.getFileIcoType());

            //如果是文件夹且当前不是多选模式则设置可以进入的图标
            if (fileBean.isDir() && !fileShowFragment.isMultipleSelectionMode()) {
                imgvEnter.setVisibility(View.VISIBLE);
            } else {
                imgvEnter.setVisibility(View.INVISIBLE);
            }
            //如果CheckBox需要显示则显示
            if (fileBean.getBoxVisible()) {
                checkBox.setVisibility(View.VISIBLE);
            } else {
                checkBox.setVisibility(View.INVISIBLE);
            }

            checkBox.setChecked(fileBean.getBoxChecked());
            tvName.setText(fileBean.getName());
            //文件文件夹时间----大小时间
            if (fileBean.isDir()) {
                String dirDetail = String.format(StringUtils.getString(R.string.filebeanitem_dir_detail_mlh), fileBean.getChildrenDirNumber(), fileBean.getChildrenFileNumber());
                tvDetail.setText(dirDetail);
            } else {
                tvDetail.setText(TimeUtils.millis2String(fileBean.getModifyTime(), "yy-MM-dd HH:mm  ") + fileBean.getSizeString());
            }


        }

    }
}
