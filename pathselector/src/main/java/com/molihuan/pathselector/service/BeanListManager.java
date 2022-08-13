package com.molihuan.pathselector.service;

import com.molihuan.pathselector.R;
import com.molihuan.pathselector.adapters.FileListAdapter;
import com.molihuan.pathselector.adapters.TabbarFileListAdapter;
import com.molihuan.pathselector.entities.FileBean;
import com.molihuan.pathselector.entities.TabbarFileBean;
import com.molihuan.pathselector.utils.Constants;
import com.molihuan.pathselector.utils.FileTools;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * @ClassName BeanListManager
 * @Description TODO list管理者
 * @Author molihuan
 * @Date 2022/6/25 14:42
 */
public class BeanListManager {

    //更新mTabbarFileList模式
    public static final int TypeAddTabbar=0;
    public static final int TypeDelTabbar=1;
    public static final int TypeInitTabbar=2;

    /**
     * 清理List增强
     * @param list
     */
    public static void clearList(List list){
        if (list!=null&&list.size()!=0){
            list.clear();
        }
    }

    /**
     * 设置List<FileBean> 的所有Checked
     * @param list
     * @param var
     */
    public static void setCheckList(List<FileBean> list,boolean var){
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setChecked(var);
        }
    }




    /**
     * 异步
     * 更新fileBeanList数据通过路径的方式
     * @param fileBeanList
     * @param fileListAdapter
     * @param path
     * @return
     */
    public static void upDataFileBeanListByAsyn(List<FileBean> fileBeanList, FileListAdapter fileListAdapter, String path, List<String> fileTypes,int sortType) {

        //清除列表
        if (fileBeanList==null){
            fileBeanList=new ArrayList<>();
        }else if (fileBeanList.size()!=0){
            fileBeanList.clear();
        }

        Observable
                .just(fileBeanList)
                .map(new Function<List<FileBean>, List<FileBean>>() {
                    @Override
                    public List<FileBean> apply(List<FileBean> fileBeanList) throws Throwable {
                        //一些数据处理
                        FileBean fileBean;
                        //添加数据
                        File file = FileTools.getFileByPath(path);
                        File[] files = file.listFiles();
                        if (files!=null){
                            for (int i = 0; i < files.length; i++) {
                                fileBean=new FileBean(files[i].getAbsolutePath(),false);
                                //只添加文件后缀符合要求的、文件夹添加、没有要求就都添加
                                if (fileTypes==null||fileTypes.size()==0||fileBean.isDir()||fileTypes.contains(fileBean.getFileExtension())){
                                    fileBeanList.add(fileBean);
                                }
                            }
                        }
                        //排序
                        sortFileBeanList(fileBeanList,sortType);

                        return fileBeanList;
                    }
                })
                .subscribeOn(Schedulers.io())//将被观察者切换到子线程
                .observeOn(AndroidSchedulers.mainThread())//将观察者切换到主线程  需要在Android环境下运行
                .subscribe(new Observer<List<FileBean>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull List<FileBean> fileBeanList) {
                        //刷新数据
                        if (fileListAdapter!=null){
                            fileListAdapter.notifyDataSetChanged();
                            if (fileBeanList.size()==0){
                                //没有数据时显示空
                                fileListAdapter.setEmptyView(R.layout.fragment_empty_files_list_mlh);
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }



    /**
     * FileBeanList排序
     * @param fileBeanList
     * @param sortType
     */
    public static void sortFileBeanList(List<FileBean> fileBeanList,int sortType){
        Collections.sort(fileBeanList, new Comparator<FileBean>() {
            /**
             * 注意返回值
             * @param o1
             * @param o2
             * @return 1交换 -1不交换
             */
            @Override
            public int compare(FileBean o1, FileBean o2) {

                if (o1.isDir() && o2.isFile())//如果前面的是文件夹就不换,下面相反
                    return -1;
                if (o1.isFile() && o2.isDir())
                    return 1;

                switch (sortType){
                    case Constants.SORT_NAME_ASC :
                        return o1.getFileName().compareToIgnoreCase(o2.getFileName());//根据名称字符串ASCLL码进行比较(忽略大小写)
                    case Constants.SORT_NAME_DESC :
                        return o2.getFileName().compareToIgnoreCase(o1.getFileName());
                    case Constants.SORT_TIME_ASC :
                        long diff = o1.getModifyTime() - o2.getModifyTime();
                        if (diff > 0)
                            return 1;
                        else if (diff == 0)
                            return 0;
                        else
                            return -1;
                    case Constants.SORT_TIME_DESC :
                        diff = o2.getModifyTime() - o1.getModifyTime();
                        if (diff > 0)
                            return 1;
                        else if (diff == 0)
                            return 0;
                        else
                            return -1;
                    case Constants.SORT_SIZE_ASC :
                        diff = o1.getSimpleSize() - o2.getSimpleSize();
                        if (diff > 0)
                            return 1;
                        else if (diff == 0)
                            return 0;
                        else
                            return -1;
                    case Constants.SORT_SIZE_DESC :
                        diff = o2.getSimpleSize() - o1.getSimpleSize();
                        if (diff > 0)
                            return 1;
                        else if (diff == 0)
                            return 0;
                        else
                            return -1;
                    default: return 0;
                }
            }
        });
    }


    /**
     *初始化数据tabbarFileBeanList数据
     * @param tabbarList
     * @param path
     */
    public static void getTabbarFileBeanList(List<TabbarFileBean> tabbarList,String path,List<String> SdCardList){
        if (SdCardList.contains(path)){
            int i = SdCardList.indexOf(path);
            if (i==0){
                tabbarList.add(0,new TabbarFileBean(path,"内部存储",false,null));
            }else if (i>0){
                tabbarList.add(0,new TabbarFileBean(path, String.format("SD%d", i),false,null));
            }else{
                tabbarList.add(0,new TabbarFileBean(path,"错误163",false,null));
            }
            return;
        }
        tabbarList.add(0,new TabbarFileBean(path,false));
        getTabbarFileBeanList(tabbarList,FileTools.getParentPath(path),SdCardList);
    }
    /**
     * 有问题
     * 更新tabbarFileBeanList数据
     * @param tabbarList
     * @param tabbarAdapter
     * @param path
     * @param type
     * @return
     */
    public static void upDataTabbarFileBeanListByAsyn (List<TabbarFileBean> tabbarList, TabbarFileListAdapter tabbarAdapter, String path, int type,List<String> SdCardList){

        Observable
                .just(tabbarList)
                .map(new Function<List<TabbarFileBean>, List<TabbarFileBean>>() {
                    @Override
                    public List<TabbarFileBean> apply(List<TabbarFileBean> tabbarLists) throws Throwable {
                        //选择模式
                        switch (type){
                            case TypeAddTabbar :
                                //添加数据
                                tabbarLists.add(new TabbarFileBean(path,false));
                                break;
                            case TypeDelTabbar:
                                //移除数据
                                for (int i = tabbarLists.size() - 1; i >= 0; i--) {
                                    if (tabbarLists.get(i).getFilePath().length()>path.length()){//移除比当前路径还长的数据
                                        tabbarLists.remove(i);
                                    }else {
                                        break;
                                    }
                                }
                                break;
                            case TypeInitTabbar:
                                //创建初始化数据
                                if (tabbarLists==null){
                                    tabbarLists=new ArrayList<>();
                                    getTabbarFileBeanList(tabbarLists,path,SdCardList);
                                }else {
                                    tabbarLists.clear();
                                    getTabbarFileBeanList(tabbarLists,path,SdCardList);
                                }
                                break;
                        }

                        return null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<TabbarFileBean>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull List<TabbarFileBean> tabbarFileBeanList) {
                        //刷新数据
                        if (tabbarAdapter!=null){
                            tabbarAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    public static List<TabbarFileBean> upDataTabbarFileBeanList (List<TabbarFileBean> tabbarList, TabbarFileListAdapter tabbarAdapter, String path, int type,List<String> SdCardList){
        //选择模式
        switch (type){
            case TypeAddTabbar :
                //添加数据
                tabbarList.add(new TabbarFileBean(path,false));
                break;
            case TypeDelTabbar:
                //移除数据
                for (int i = tabbarList.size() - 1; i >= 0; i--) {
                    if (tabbarList.get(i).getFilePath().length()>path.length()){//移除比当前路径还长的数据
                        tabbarList.remove(i);
                    }else {
                        break;
                    }
                }
                break;
            case TypeInitTabbar:
                //创建初始化数据
                if (tabbarList==null){
                    tabbarList=new ArrayList<>();
                    getTabbarFileBeanList(tabbarList,path,SdCardList);
                }else {
                    tabbarList.clear();
                    getTabbarFileBeanList(tabbarList,path,SdCardList);
                }
                break;
        }

        //刷新数据
        if (tabbarAdapter!=null){
            tabbarAdapter.notifyDataSetChanged();
        }

        return tabbarList;
    }

    /**
     * 获取选择返回的数据
     * @param fileBeanList
     * @return
     */
    public static List<String> getCallBackData(List<FileBean> fileBeanList){
        if (fileBeanList==null){
            return null;
        }
        List<String> data = new ArrayList<>();
        for (int i = 0; i < fileBeanList.size(); i++) {
            if (fileBeanList.get(i).isChecked()){
                data.add(fileBeanList.get(i).getFilePath());
            }
        }
        return data;
    }
    /**
     * 获取选择返回的FileBean list
     * @param fileBeanList
     * @return
     */
    public static List<FileBean> getCallBackFileBeanList(List<FileBean> fileBeanList){
        if (fileBeanList==null){
            return null;
        }
        List<FileBean> data = new ArrayList<>();
        for (int i = 0; i < fileBeanList.size(); i++) {
            if (fileBeanList.get(i).isChecked()){
                data.add(fileBeanList.get(i));
            }
        }
        return data;
    }

    /**
     * 设置CheckBox显示、隐藏
     * @param fileBeanList
     * @param fileListAdapter
     * @param state
     */
    public static void setCheckBoxVisible(List<FileBean> fileBeanList,FileListAdapter fileListAdapter,boolean state){
        //判空
        if (fileBeanList==null||fileListAdapter==null){
            return ;
        }
        //设置状态
        if (state){
            for (int i = 0; i < fileBeanList.size(); i++) {
                fileBeanList.get(i).setVisible(true);
            }
        }else {
            for (int i = 0; i < fileBeanList.size(); i++) {
                fileBeanList.get(i).setVisible(false);
                fileBeanList.get(i).setChecked(false);
            }
        }

        //刷新适配器
        fileListAdapter.notifyDataSetChanged();
    }


}

