package com.z.fileselectorlib.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.z.fileselectorlib.Objects.BasicParams;
import com.z.fileselectorlib.Objects.FileInfo;
import com.z.fileselectorlib.R;
import com.z.fileselectorlib.Utils.FileUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

public class FileListAdapter extends BaseAdapter {

    private ArrayList<FileInfo> FileList;
    private HashMap<Integer,Boolean> SelectionMap =new HashMap<>();
    private Context mContext;
    private LayoutInflater inflater;
    private boolean isSelect=false;

    public FileListAdapter(ArrayList<FileInfo> fileList, Context mContext) {
        FileList = fileList;
        this.mContext = mContext;
        inflater=LayoutInflater.from(mContext);
        clearSelections();
    }

    public void clearSelections() {
        for (int i = 0; i < FileList.size(); i++) {
            SelectionMap.put(i, false);
        }
    }

    public void updateFileList(ArrayList<FileInfo> fileList){
        this.FileList=fileList;
        notifyDataSetChanged();
    }

    public void setSelect(boolean select) {
        isSelect = select;
        notifyDataSetChanged();
    }

    public void ModifyFileSelected(int position,boolean isSelected) {
        SelectionMap.put(position,isSelected);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return FileList.size();
    }

    @Override
    public Object getItem(int position) {
        return FileList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.file_list_item, null);
            viewHolder.ivFileIcon=convertView.findViewById(R.id.FileIcon);
            viewHolder.tvFileCount=convertView.findViewById(R.id.FileCount);
            viewHolder.tvFileName=convertView.findViewById(R.id.FileName);
            viewHolder.tvFileDate=convertView.findViewById(R.id.FileDate);
            viewHolder.ckSelector=convertView.findViewById(R.id.select_box);
            viewHolder.llInfo=convertView.findViewById(R.id.FileInfo);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvFileName.setText(FileList.get(position).getFileName());
        viewHolder.tvFileCount.setText(FileList.get(position).getFileCount());
        viewHolder.tvFileDate.setText(FileList.get(position).getFileLastUpdateTime());
        setIcon(position, viewHolder);
        if (FileList.get(position).getFileType()== FileInfo.FileType.Parent){
            ViewShow(viewHolder.llInfo,View.INVISIBLE,0);
            viewHolder.tvFileName.setGravity(Gravity.CENTER_VERTICAL);
        }else {
            ViewShow(viewHolder.llInfo,View.VISIBLE,25);
        }

        if (isSelect && FileList.get(position).getFileType()!= FileInfo.FileType.Parent)
            viewHolder.ckSelector.setVisibility(View.VISIBLE);
        else viewHolder.ckSelector.setVisibility(View.INVISIBLE);

        if (SelectionMap !=null && isSelect && viewHolder.ckSelector!=null) {
                viewHolder.ckSelector.setChecked(Objects.isNull(SelectionMap.get(position)) ? false : (boolean) SelectionMap.get(position));
        }

        return convertView;
    }

    private void setIcon(int position, ViewHolder viewHolder) {
        FileInfo currentFile = FileList.get(position);
        if (BasicParams.getInstance().getCustomIcon().size()>0){
            Map<String, Bitmap> customIcons = BasicParams.getInstance().getCustomIcon();
            Iterator<Map.Entry<String, Bitmap>> entries = customIcons.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry<String, Bitmap> entry = entries.next();
                if (FileUtil.fileFilter(currentFile.getFilePath(),entry.getKey())){
                    viewHolder.ivFileIcon.setImageBitmap(entry.getValue());
                    return;
                }
            }
        }
        switch(currentFile.getFileType()){
            case Folder:{
                viewHolder.ivFileIcon.setImageResource(R.mipmap.file_folder);
            }
                break;
            case Parent:{
                viewHolder.ivFileIcon.setImageResource(R.mipmap.back_to_parent);
//                viewHolder.llInfo.setVisibility(View.INVISIBLE);
//                viewHolder.tvFileName.setPadding(20,50,0,-10);
            }
                break;
            case Image:{
                viewHolder.ivFileIcon.setImageResource(R.mipmap.file_image);
            }
                break;
            case Audio:{
                viewHolder.ivFileIcon.setImageResource(R.mipmap.file_audio);
            }
            break;
            case Video:{
                viewHolder.ivFileIcon.setImageResource(R.mipmap.file_video);
            }
            break;
            case Text:{
                viewHolder.ivFileIcon.setImageResource(R.mipmap.file_text);
            }
            break;
            case Unknown:{
                viewHolder.ivFileIcon.setImageResource(R.mipmap.file_unknown);
            }
                break;
            default:{
            }
        }
    }

    private void ViewShow(ViewGroup view,int visible, int dp) {
        view.setVisibility(visible);
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = (int)(dp*mContext.getResources().getDisplayMetrics().density);
        view.setLayoutParams(params);
    }
    public static class  ViewHolder{
        public LinearLayout llInfo;
        public ImageView ivFileIcon;
        public TextView tvFileName,tvFileCount,tvFileDate;
        public CheckBox ckSelector;
    }
}


