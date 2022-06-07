package com.zlylib.mlhfileselectorlib;

import android.app.Activity;
import android.content.Context;

import androidx.fragment.app.Fragment;

import java.lang.ref.WeakReference;
/**
 * SelectCreator
 * Created by zhangliyang on 2020/6/20.
 *
 */
public class FileSelector {

    public static final int BY_NAME_ASC = 0;
    public static final int BY_NAME_DESC = 1;
    public static final int BY_TIME_ASC = 2;
    public static final int BY_TIME_DESC = 3;
    public static final int BY_SIZE_ASC = 4;
    public static final int BY_SIZE_DESC = 5;
    public static final int BY_EXTENSION_ASC = 6;
    public static final int BY_EXTENSION_DESC = 7;

    private final WeakReference<Context> mContext;
    private final WeakReference<Fragment> mFragment;


    private FileSelector(Activity activity) {
        this(activity,null);
    }

    private FileSelector(Fragment fragment){
        this( fragment.getActivity(),fragment);
    }

    private FileSelector(Context mContext, Fragment mFragment) {
        this.mContext = new WeakReference<>(mContext);
        this.mFragment = new WeakReference<>(mFragment);
    }


    public static SelectCreator from(Activity activity){
        return new FileSelector(activity).initFile();
    }

    public static SelectCreator from(Fragment fragment){
        return new FileSelector(fragment).initFile();
    }

    private SelectCreator initFile(){
        return new SelectCreator(this);
    }




    public Context getActivity() {
        return mContext.get();
    }

    public Fragment getFragment() {
        return mFragment != null ? mFragment.get() : null;
    }



}
