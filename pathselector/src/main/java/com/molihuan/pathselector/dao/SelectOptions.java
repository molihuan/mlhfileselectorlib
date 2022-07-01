package com.molihuan.pathselector.dao;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.molihuan.pathselector.R;
import com.molihuan.pathselector.adapters.FileListAdapter;
import com.molihuan.pathselector.adapters.TabbarFileListAdapter;
import com.molihuan.pathselector.entities.FileBean;
import com.molihuan.pathselector.fragments.MoreChooseFragment;
import com.molihuan.pathselector.fragments.ToolbarFragment;
import com.molihuan.pathselector.utils.Constants;

import java.util.Arrays;
import java.util.List;

/**
 * @ClassName SelectOptions
 * @Description TODO 数据设置存储
 * @Author molihuan
 * @Date 2022/6/22 3:07
 */
public class SelectOptions {

    private static SelectOptions mSelectOptions;//本身
    public Integer requestCode;//请求码
    public Integer frameLayoutID;//添加fragment地方
    private Context mContext;//上下文

    public String[] mShowFileTypes;//显示文件类型
    public String[] mSelectFileTypes;//选择文件类型
    public Integer mSortType;//排序类型
    public Boolean mSingle;//是否是单选
    public Integer mMaxCount;//最多选的个数
    public Fragment mToolbarFragment;//ToolbarFragment
    public Fragment mMoreChooseFragment;//MoreChooseFragment
    public Boolean onlyShowImages;//只显示图片
    public Boolean onlyShowVideos;//只显示视频
    public Boolean needMoreOptions;//是否需要选项
    public Boolean needMoreChoose;//是否需要多选
    public String[] optionsName;//toolbar选项名称
    public String[] MoreChooseItemName;//多选名称
    public boolean[] optionsNeedCallBack;//toolbar选项是否需要回调
    public boolean[] toolbarViewNeedCallBack;//toolbar视图点击是否需要回调
    public boolean[] moreChooseItemNeedCallBack;//多选是否需要回调

    public onToolbarOptionsListener[] optionListeners;//toolbar选项监听器
    public onToolbarListener[] toolbarListeners;//toolbar点击监听器
    public onMoreChooseItemsListener[] moreChooseItemListeners;//多选监听器
    public onFileItemListener fileItemListener;//文件item监听器

    public String rootPath;//设置默认目录

    //toolbar
    public String toolbarMainTitle;//主标题
    public String toolbarSubtitleTitle;//副标题
    public Integer toolbarBG;//背景色
    public Integer toolbarMainTitleColor;//主标题字颜色
    public Integer toolbarSubtitleColor;//副标题字颜色
    public Integer toolbarOptionColor;//选项字颜色
    public Integer toolbarOptionSize;//选项字大小18

    public FragmentManager fragmentManager;//fragment管理者

    public Boolean showToolBarFragment;//显示ToolBarFragment

    public Integer typeLoadCustomView;//加载自定义View类型



    /**
     * 获取一个实例
     * @return
     */
    public static SelectOptions getInstance() {
        if (mSelectOptions==null){
            mSelectOptions=new SelectOptions();
        }
        return mSelectOptions;
    }



    /**
     * 获取一个数据重置的实例
     * @return
     */
    public static SelectOptions getResetInstance(Context context) {
        mSelectOptions=getInstance();
        mSelectOptions.mContext=context;
        mSelectOptions.reset();
        return mSelectOptions;
    }

    /**
     * 获取上下文
     * @return
     */
    public Context getContext() {
        return mContext;
    }



    /**
     * 获取显示文件类型
     * @return
     */
    public String[] getShowFileTypes() {
        if (mShowFileTypes == null) {
            return new String[]{};
        }
        return mShowFileTypes;
    }
    /**
     * 获取选择文件类型
     * @return
     */
    public String[] getSelectFileTypes() {
        if (mSelectFileTypes == null) {
            return new String[]{};
        }
        return mSelectFileTypes;
    }

    /**
     * 排序规则
     * @return
     */
    public int getSortType() {
        if (mSortType==null) {
            return Constants.SORT_NAME_ASC;
        }
        return mSortType;
    }


    /**
     * MoreChooseFragment
     * @return
     */
    public Fragment getMoreChooseFragment() {
        if (mMoreChooseFragment ==null) {
            if (MoreChooseItemName==null){
                return null;
                //throw new NullPointerException("MoreChooseItemName null 多选按钮名称对象为空");
            }else {
                return (new MoreChooseFragment(Arrays.asList(MoreChooseItemName)));
            }

        }
        return mMoreChooseFragment;
    }
    /**
     * ToolbarFragment
     * @return
     */
    public Fragment getToolbarFragment() {
        if (mToolbarFragment ==null) {
//            if (optionsName==null){
//                throw new NullPointerException("optionsName null Toolbar选项按钮名称对象为空");
//            }
            return (new ToolbarFragment());
        }
        return mToolbarFragment;
    }

    /**
     * 初始化/重置数据
     */
    private void reset() {
        requestCode=100;//默认请求码
        frameLayoutID=null;//添加fragment地方
        mShowFileTypes=null;//显示文件类型
        mSelectFileTypes=null;//选择文件类型
        mSortType=Constants.SORT_NAME_ASC;//排序类型
        mSingle=false;//是否是单选
        mMaxCount=-1;//最多选的个数默认是-1即无限
        mToolbarFragment=null;//ToolbarFragment
        mMoreChooseFragment=null;//MoreChooseFragment
        onlyShowImages=false;//只显示图片
        onlyShowVideos=false;//只显示视频
        needMoreOptions=false;//是否需要选项
        needMoreChoose=false;//是否需要多选
        optionsName=null;//选项名称
        MoreChooseItemName=null;//多选名称
        optionsNeedCallBack=null;//选项是否需要回调
        toolbarViewNeedCallBack=null;//Toolbar视图点击是否需要回调
        moreChooseItemNeedCallBack=null;//多选是否需要回调
        optionListeners=null;//toolbar选项监听
        toolbarListeners=null;//toolbar点击监听
        moreChooseItemListeners=null;//多选监听
        fileItemListener=null;//文件item监听
        rootPath=null;//设置默认目录
        toolbarMainTitle="请选择路径";//默认主标题
        toolbarSubtitleTitle=null;//默认副标题
        toolbarBG= ContextCompat.getColor(mContext, R.color.orange_mlh);//toolbar背景颜色
        toolbarMainTitleColor=Color.WHITE;//主标题文字颜色
        toolbarSubtitleColor=Color.WHITE;//副标题文字颜色
        toolbarOptionColor= Color.WHITE;//一个选项颜色
        toolbarOptionSize=18;//一个选项大小
        fragmentManager=null;
        showToolBarFragment=true;
        typeLoadCustomView=Constants.TYPE_CUSTOM_VIEW_NULL;
    }

    /**
     *Toolbar选项回调
     */
    public interface onToolbarOptionsListener {
        /**
         *
         * @param view 点击的控件
         * @param currentPath 当前路径
         * @param fileBeanList 文件列表：用于修改列表
         * @param callBackData 返回的数据
         * @param tabbarAdapter 标签栏列表适配器：用于更新UI
         * @param fileAdapter 文件列表适配器：用于更新UI
         * @param callBackFileBeanList 已经选择的FileBeanList列表:主要用于获取DocumentFile操作Android/data目录
         */
        void onOptionClick(View view, String currentPath, List<FileBean> fileBeanList, List<String> callBackData, TabbarFileListAdapter tabbarAdapter, FileListAdapter fileAdapter,List<FileBean> callBackFileBeanList);
    }
    /**
     *Toolbar点击回调
     */
    public interface onToolbarListener {
        /**
         *
         * @param view 点击的控件
         * @param currentPath 当前路径
         * @param fileBeanList 文件列表：用于修改列表
         * @param callBackData 返回的数据
         * @param tabbarAdapter 标签栏列表适配器：用于更新UI
         * @param fileAdapter 文件列表适配器：用于更新UI
         * @param callBackFileBeanList 已经选择的FileBeanList列表:主要用于获取DocumentFile操作Android/data目录
         */
        void onClick(View view, String currentPath, List<FileBean> fileBeanList, List<String> callBackData, TabbarFileListAdapter tabbarAdapter, FileListAdapter fileAdapter,List<FileBean> callBackFileBeanList);
    }
    /**
     *多选回调
     */
    public interface onMoreChooseItemsListener {
        /**
         *
         * @param view 点击的控件
         * @param currentPath 当前路径
         * @param fileBeanList 文件列表：用于修改列表
         * @param callBackData 返回的数据
         * @param tabbarAdapter 标签栏列表适配器：用于更新UI
         * @param fileAdapter 文件列表适配器：用于更新UI
         * @param callBackFileBeanList 已经选择的FileBeanList列表:主要用于获取DocumentFile操作Android/data目录
         */
        void onItemsClick(View view, String currentPath, List<FileBean> fileBeanList, List<String> callBackData, TabbarFileListAdapter tabbarAdapter, FileListAdapter fileAdapter,List<FileBean> callBackFileBeanList);
    }

    /**
     * 文件item点击/长按回调
     */
    public interface onFileItemListener {
        /**
         *返回值 true 表示后面不需要进行处理了 false反之
         * @param view 点击的控件
         * @param currentPath 当前路径
         * @param fileBeanList 文件列表
         * @param callBackData 返回的数据
         * @param tabbarAdapter 标签栏列表适配器
         * @param fileAdapter 文件列表适配器
         * @param fileBean 已经选择的FileBeanList:主要用于获取DocumentFile操作Android/data目录
         * @return
         */
        boolean onFileItemClick(View view, String currentPath, List<FileBean> fileBeanList, List<String> callBackData, TabbarFileListAdapter tabbarAdapter, FileListAdapter fileAdapter,FileBean fileBean);
        boolean onLongFileItemClick(View view, String currentPath, List<FileBean> fileBeanList, List<String> callBackData, TabbarFileListAdapter tabbarAdapter, FileListAdapter fileAdapter,FileBean fileBean);
    }

    /**
     * 释放资源
     */
    public void release() {
        mSelectOptions=null;
    }
}
