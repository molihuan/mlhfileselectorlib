package com.zlylib.mlhfileselectorlib.adapter;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatCheckBox;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.zlylib.mlhfileselectorlib.R;
import com.zlylib.mlhfileselectorlib.bean.EssFile;
import com.zlylib.mlhfileselectorlib.utils.FileSizeUtil;
import com.zlylib.mlhfileselectorlib.utils.FileUtils;

import java.util.List;

//import com.bumptech.glide.Glide;
//import com.bumptech.glide.request.RequestOptions;

//import com.bumptech.glide.Glide;
//import com.bumptech.glide.request.RequestOptions;

/**
 * FileListAdapter
 * Created by zhangliyang on 2020/6/20.
 */

public class FileListAdapter extends BaseQuickAdapter<EssFile, BaseViewHolder> {

    private onLoadFileCountListener loadFileCountListener;

    public interface onLoadFileCountListener{
        void onLoadFileCount(int posistion);
    }

    public onLoadFileCountListener getLoadFileCountListener() {
        return loadFileCountListener;
    }

    public void setLoadFileCountListener(onLoadFileCountListener loadFileCountListener) {
        this.loadFileCountListener = loadFileCountListener;
    }

    public FileListAdapter(@Nullable List<EssFile> data) {
        super(R.layout.item_file_list, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, EssFile item) {
        TextView textView = helper.getView(R.id.tv_item_file_list_desc);
        AppCompatCheckBox checkBox = helper.getView(R.id.checkbox_item_file_list);
        if (item.isDirectory()) {
            helper.setVisible(R.id.iv_item_file_select_right, true);
            if(item.getChildFolderCount().equals("加载中")){
                //查找数量
                if(loadFileCountListener!=null){
                    loadFileCountListener.onLoadFileCount(helper.getAdapterPosition());
                }
            }
            textView.setText(String.format(getContext().getString(R.string.folder_desc), item.getChildFileCount(),item.getChildFolderCount()));
        } else {
            helper.setVisible(R.id.iv_item_file_select_right, false);
            textView.setText(String.format(getContext().getString(R.string.file_desc), FileUtils.getDateTime(item.getAbsolutePath()), FileSizeUtil.getAutoFileOrFilesSize(item.getFile())));
        }
        helper.setText(R.id.tv_item_file_list, item.getName());


        if(item.isChecked()){
            checkBox.setChecked(true);
        }else {
            checkBox.setChecked(false);
        }

        if(item.isVisible()){

            helper.setVisible(R.id.checkbox_item_file_list,true);
            helper.setGone(R.id.iv_item_file_select_right,true);
        }else {
            checkBox.setChecked(false);
            helper.setVisible(R.id.checkbox_item_file_list,false);
            helper.setGone(R.id.iv_item_file_select_right,false);
        }

        //helper.setVisible(R.id.checkbox_item_file_list,true);

        ImageView imageView = helper.getView(R.id.iv_item_file_select_left);
        String fileNameExtension = FileUtils.getExtension(item.getName()).toLowerCase();
        switch (fileNameExtension) {
            case "apk":
                imageView.setImageResource(R.mipmap.apk);
                break;
            case "avi":
                imageView.setImageResource(R.mipmap.avi);
                break;
            case "doc":
            case "docx":
                imageView.setImageResource(R.mipmap.doc);
                break;
            case "exe":
                imageView.setImageResource(R.mipmap.exe);
                break;
            case "flv":
                imageView.setImageResource(R.mipmap.flv);
                break;
            case "gif":
                imageView.setImageResource(R.mipmap.gif);
//                RequestOptions options = new RequestOptions()
//                        .centerCrop()
//                        .placeholder(R.mipmap.gif);
//                Glide
//                        .with(getContext())
//                        .load(item.getAbsolutePath())
//                        .apply(options)
//                        .into(imageView);
                break;
            case "jpg":
            case "jpeg":
            case "png":
                imageView.setImageResource(R.mipmap.png);
//                RequestOptions options2 = new RequestOptions()
//                        .centerCrop()
//                        .placeholder(R.mipmap.png);
//                Glide
//                        .with(getContext())
//                        .load(item.getAbsolutePath())
//                        .apply(options2)
//                        .into(imageView);
                break;
            case "mp3":
                imageView.setImageResource(R.mipmap.mp3);
                break;
            case "mp4":
            case "f4v":
                imageView.setImageResource(R.mipmap.movie);
                break;
            case "pdf":
                imageView.setImageResource(R.mipmap.pdf);
                break;
            case "ppt":
            case "pptx":
                imageView.setImageResource(R.mipmap.ppt);
                break;
            case "wav":
                imageView.setImageResource(R.mipmap.wav);
                break;
            case "xls":
            case "xlsx":
                imageView.setImageResource(R.mipmap.xls);
                break;
            case "zip":
                imageView.setImageResource(R.mipmap.zip);
                break;
            case "ext":
            default:
                if (item.isDirectory()) {
                    imageView.setImageResource(R.mipmap.folder);
                } else {
                    imageView.setImageResource(R.mipmap.documents);
                }
                break;
        }
    }
}
