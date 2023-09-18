

<p align="center">
<img src="https://s2.loli.net/2022/12/14/WoYwfehDNHbMzIZ.png" alt="Banner" />
</p>

<h1 align="center">mlhfileselector</h1>

[![Maven Central](https://img.shields.io/maven-central/v/io.github.molihuan/pathselector.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.github.molihuan%22%20AND%20a:%22pathselector%22)[![jitpack](https://jitpack.io/v/molihuan/mlhfileselectorlib.svg)](https://jitpack.io/#molihuan/mlhfileselectorlib)![API: 19-33 (shields.io)](https://img.shields.io/badge/API-19--33-green)[![license: Apache-2.0 (shields.io)](https://img.shields.io/badge/license-Apache--2.0-brightgreen)](https://github.com/molihuan/mlhfileselectorlib/blob/master/LICENSE)[![Star](https://img.shields.io/github/stars/molihuan/mlhfileselectorlib.svg)](https://github.com/molihuan/mlhfileselectorlib)[![bilibili: 玲莫利 (shields.io)](https://img.shields.io/badge/bilibili-玲莫利-orange)](https://space.bilibili.com/454222981)[![CSDN: molihuan (shields.io)](https://img.shields.io/badge/CSDN-molihuan-blue)](https://blog.csdn.net/molihuan)

<h3 align="center">Library that provides file or path selection on Android</h3>
<p align="center">Automatically apply for storage permission, support Android 4.4 to 13, support Android/data and Android/obb directory access,</p>
<p align="center">support custom UI,support SD card.</p>
<p align="center">(The Keyword:file selector operator android/data android 11 android 13)</p>

## Language(语言)

#### **[Chinese](./README.md)** | [English](./README_EN.md)

## Why choose me?

Automatically apply storage permission, support Android4.4 ~ 13, no longer need to struggle to adapt to various versions, fast integration, a code to get it done, perfect documentation, support no root access and operation of Android/data and Android/obb directory (adapted to Android 13), support SD card, highly customizable UI to meet all your needs, use Very flexible, support internationalization, for Android file selection you just need to focus on your business code and leave the rest to it.

## Characteristics

- [x] Automatically request storage permissions(Can control)
- [x] Android 4.4 ~ 13
- [x] Android/data and Android/obb directory access and manipulation
- [x] SD Card
- [x] Highly customizable UI
- [x] Internationalization
- [ ] Search function
- [x] Custom icon
- [ ] Show hidden files

## Preface

#### Can you give the project a Star before starting? Thank you very much, your support is the only thing that keeps me going. Welcome Star and Issues!

#### We need your Pr. Note (please Pr to dev branch)

#### Project Address：
##### [Github](https://github.com/molihuan/mlhfileselectorlib)
##### [Gitee](https://gitee.com/molihuan/mlhfileselectorlib)

## Demo：

#### Android version：Android 13 

#### Download Links：[Experience App](https://github.com/molihuan/mlhfileselectorlib/tree/master/app/release)

![pathSelectorDemo.gif](https://s2.loli.net/2022/12/14/QSGrIvwzYKhZuMe.gif)

## I. Quick start

#### Step 1: Add the repository:

- ##### If your project Gradle configuration is under `7.0`, you need to add to the `build.gradle` file

```java
allprojects {
    repositories {
        ...
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}
```

- ##### If your Gradle configuration is `7.0 and above`, you need to add to your `settings.gradle` file

```java
dependencyResolutionManagement {
    repositories {
    	...
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}
```

#### Step 2: Add the remote dependency:

- ##### After configuring the remote repository, add the remote dependency to the `build.gradle` file under the project app module

- ##### Latest Release:[![Maven Central](https://img.shields.io/maven-central/v/io.github.molihuan/pathselector.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.github.molihuan%22%20AND%20a:%22pathselector%22)

```java
dependencies {
    ...
    // We recommend using the latest release version, please see above for the latest release version
    // Please replace "version" with a specific version number, e.g. 1.1.16
    implementation 'io.github.molihuan:pathselector:version'
}
```

#### Step 3: Demonstration of basic usage:

```java
//Permissions will be requested automatically if you don't have them
PathSelector.build(this, MConstants.BUILD_DIALOG)//Dialog build mode
        .setMorePopupItemListeners(
                new CommonItemListener("OK") {
                    @Override
                    public boolean onClick(View v, TextView tv, List<FileBean> selectedFiles, String currentPath, BasePathSelectFragment pathSelectFragment) {
                        
                        /**Dialog dismiss
 * pathSelectFragment.close();
 */

                        StringBuilder builder = new StringBuilder();
                        builder.append("you selected:\n");
                        for (FileBean fileBean : selectedFiles) {
                            builder.append(fileBean.getPath() + "\n");
                        }
                        Mtools.toast(builder.toString());

                        return false;
                    }
                }
        )
        .show();//Start building
```



## II. Basic settings

#### Turn on debug mode

```java
//Turn on debug mode, please turn off production environment
PathSelectorConfig.setDebug(true);
//or use PathSelector.setDebug(true);
```

#### 1、Activity build mode：

```java
//Activity build mode
PathSelectFragment selector = PathSelector.build(this, MConstants.BUILD_ACTIVITY)
        .setRequestCode(635)
        .setMorePopupItemListeners(
                new CommonItemListener("OK") {
                    @Override
                    public boolean onClick(View v, TextView tv, List<FileBean> selectedFiles, String currentPath, BasePathSelectFragment pathSelectFragment) {

                        StringBuilder builder = new StringBuilder();
                        builder.append("you selected:\n");
                        for (FileBean fileBean : selectedFiles) {
                            builder.append(fileBean.getPath() + "\n");
                        }
                        Mtools.toast(builder.toString());

                        return false;
                    }
                }
        )
        .show();
```

#### 2、Fragment build mode：

##### Step 1: Use FrameLayout placeholders in the xml of the layout file you need to display

```xml
<FrameLayout
    android:id="@+id/fragment_select_show_area"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content" />
```

##### Step 2: Write the code

```java
//Get the PathSelectFragment instance and then handle the back button click event in onBackPressed
PathSelectFragment selector = PathSelector.build(this, MConstants.BUILD_FRAGMENT)
        .setFrameLayoutId(R.id.fragment_select_show_area)//Load position, ID of FrameLayout
        .setMorePopupItemListeners(
                new CommonItemListener("OK") {
                    @Override
                    public boolean onClick(View v, TextView tv, List<FileBean> selectedFiles, String currentPath, BasePathSelectFragment pathSelectFragment) {

                        StringBuilder builder = new StringBuilder();
                        builder.append("you selected:\n");
                        for (FileBean fileBean : selectedFiles) {
                            builder.append(fileBean.getPath() + "\n");
                        }
                        
                        Mtools.toast(builder.toString());
                        return false;
                    }
                }
        )
        .show();
```

##### Step 3: Override the onBackPressed() method to let the path selector take precedence over the return button click event

<h5 style="color:red"> Very important!!!</h5>

<h5 style="color:red"> Very important!!!</h5>

<h5 style="color:red"> Very important!!!</h5>

##### Important things are to be repeated for 3 times

```java
@Override
public void onBackPressed() {

    //Let PathSelectFragment handle the return button click event first
    if (selector != null && selector.onBackPressed()) {
        return;
    }
    ......
    super.onBackPressed();
}
```

#### 3、Dialog build mode & common settings.

```java
//Get the PathSelectFragment instance and then handle the back button click event in onBackPressed
PathSelectFragment selector = PathSelector.build(this, MConstants.BUILD_DIALOG)
        //.setBuildType(MConstants.BUILD_DIALOG)//Already set in the build
        //.setContext(this)//Already set in the build
        .setRootPath("/storage/emulated/0/")//Initial path
        .setShowSelectStorageBtn(true)//Whether to display internal storage selection button
        .setShowTitlebarFragment(true)//Whether to display the title bar
        .setShowTabbarFragment(true)//Whether to show breadcrumbs
        .setAlwaysShowHandleFragment(true)//Whether to always show the long press pop-up option
        .setShowFileTypes("", "mp3", "mp4")//Show only files with (no suffix) or (mp3 suffix) or (mp4 suffix)
        .setSelectFileTypes("", "mp3")//Only files with (no suffix) or (mp3 suffix) can be selected
        .setMaxCount(3)//You can select up to 3 files. The default is - 1 unlimited
        .setRadio()//Single choice(Use setMaxCount(0) to replace it if you need a single-selected folder)
        .setSortType(MConstants.SORT_NAME_ASC)//Sort by name
        .setTitlebarMainTitle(new FontBean("My Selector"))//Set the title bar main title, you can also set the font size, color, etc.
        .setTitlebarBG(Color.GREEN)//Set the title bar background color
        .setFileItemListener(//Set the callback for the file item click (it will be called back only if it is a file, but not if it is a folder)
                new FileItemListener() {
                    @Override
                    public boolean onClick(View v, FileBean file, String currentPath, BasePathSelectFragment pathSelectFragment) {
                        Mtools.toast("you clicked path:\n" + file.getPath());
                        return false;
                    }
                }
        )
        .setMorePopupItemListeners(//Set the top right option callback
                new CommonItemListener("SelectAll") {
                    @Override
                    public boolean onClick(View v, TextView tv, List<FileBean> selectedFiles, String currentPath, BasePathSelectFragment pathSelectFragment) {
                        pathSelectFragment.selectAllFile(true);
                        return false;
                    }
                },
                new CommonItemListener("DeselectAll") {
                    @Override
                    public boolean onClick(View v, TextView tv, List<FileBean> selectedFiles, String currentPath, BasePathSelectFragment pathSelectFragment) {
                        pathSelectFragment.selectAllFile(false);
                        return false;
                    }
                }
        )
        .setHandleItemListeners(//Set long press pop-up option callback
                new CommonItemListener("OK") {
                    @Override
                    public boolean onClick(View v, TextView tv, List<FileBean> selectedFiles, String currentPath, BasePathSelectFragment pathSelectFragment) {
                        StringBuilder builder = new StringBuilder();
                        builder.append("you selected:\n");
                        for (FileBean fileBean : selectedFiles) {
                            builder.append(fileBean.getPath() + "\n");
                        }
                        Mtools.toast(builder.toString());
                        return false;
                    }
                },
                new CommonItemListener("cancel") {
                    @Override
                    public boolean onClick(View v, TextView tv, List<FileBean> selectedFiles, String currentPath, BasePathSelectFragment pathSelectFragment) {
                        pathSelectFragment.openCloseMultipleMode(false);
                        return false;
                    }
                }
        )
        .show();
```

## III. Advanced settings (custom UI)

#### UI：

![UiLayout.png](https://s2.loli.net/2022/12/14/Yw8bamgrtNC9yiW.png)

#### 1、custom option style (HandleItem as an example)

##### Way 1:Set the style by FontBean

```java
PathSelectFragment selector = PathSelector.build(this, MConstants.BUILD_DIALOG)
        .setHandleItemListeners(//Set long press pop-up option callback
                //FontBean can set the text, the size of the word, the color of the word, and the icon to the left of the word
    			//R.drawable.ic_test_mlh is your own image resource id
                new CommonItemListener(new FontBean("OK", 18, Color.RED, R.drawable.ic_test_mlh)) {
                    @Override
                    public boolean onClick(View v, TextView tv, List<FileBean> selectedFiles, String currentPath, BasePathSelectFragment pathSelectFragment) {
                        Mtools.toast("You Click");
                        return false;
                    }
                }
        )
        .show();
```

<h5 style="color:red">  What? This way is not enough for you, then try way 2</h5>

##### Way 2: Override CommonItemListener's setViewStyle method to customize the style

```java
PathSelectFragment selector = PathSelector.build(this, MConstants.BUILD_DIALOG)
        .setHandleItemListeners(
                //Override CommonItemListener's setViewStyle method to customize the style
                new CommonItemListener("OK") {
                    @Override
                    public boolean setViewStyle(RelativeLayout container, ImageView leftImg, TextView textView) {
                        textView.setTextSize(18);
                        textView.setTextColor(Color.RED);
                        //Icons are not displayed by default
                        leftImg.setVisibility(View.VISIBLE);
                        leftImg.setImageResource(R.drawable.ic_test_mlh);
                        leftImg.getLayoutParams().width = 90;
                        leftImg.getLayoutParams().height = 90;
                        return true;
                    }

                    @Override
                    public boolean onClick(View v, TextView tv, List<FileBean> selectedFiles, String currentPath, BasePathSelectFragment pathSelectFragment) {
                        Mtools.toast("You Click");
                        return false;
                    }
                }
        )
        .show();
```
<h4 style="color:red"> What? What? This way is not enough for you, then you write the UI it to help you add, try the highly customizable UI</h4>

#### 2、Highly customizable UI (using Titlebar as an example)：

##### Step 1: Create a new layout file, such as:fragment_custom_titlebar.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:orientation="horizontal">

    <Button
        android:id="@+id/my_btn1"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="btn1" />

    <Button
        android:id="@+id/my_btn2"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="selectAll" />
    
</LinearLayout>
```

##### Step 2: Create a new class such as:CustomTitlebarFragment.class so that it extends AbstractTitlebarFragment and associates the layout file in step 1

```java
public class CustomTitlebarFragment extends AbstractTitlebarFragment {
    private Button btn1;
    private Button btn2;
    
    @Override
    public int setFragmentViewId() {
        return R.layout.fragment_custom_titlebar;
    }

    @Override
    public void getComponents(View view) {
        btn1 = view.findViewById(R.id.my_btn1);
        btn2 = view.findViewById(R.id.my_btn2);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Mtools.toast("The current path is:\n" + psf.getCurrentPath());
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                psf.selectAllFile(true);
            }
        });
    }
}
```

##### Step 3: Write the code

```java
//Get the PathSelectFragment instance and then handle the back button click event in onBackPressed
PathSelectFragment selector = PathSelector.build(this, MConstants.BUILD_DIALOG)
        .setTitlebarFragment(new CustomTitlebarFragment())
        .show();
```

#### 3、Customize list item icons

```java
PathSelectFragment selector = PathSelector.build(this, MConstants.BUILD_DIALOG)
        .setFileBeanController(new AbstractFileBeanController() {
            @Override
            public int getFileBeanImageResource(boolean isDir, String extension, FileBean fileBean) {
                int resourceId;
                switch (extension) {
                    case "jpg":
                    case "jpeg":
                    case "png":
                        //Developer's own image resource id
                        resourceId = R.drawable.ic_launcher_foreground;
                        break;
                    case "mp3":
                        resourceId = R.drawable.ic_launcher_foreground;
                        break;
                    case "mp4":
                        //You can also use the default image resource id
                        resourceId = com.molihuan.pathselector.R.mipmap.movie;
                        break;
                    default:
                        if (isDir) {
                            //Developer's own image resource id
                            resourceId = R.drawable.ml192;
                        } else {
                            resourceId = R.drawable.ic_launcher_background;
                        }
                        break;
                }
                return resourceId;
            }
        })
        .show();
```

## IV.Interface and methods (try to see the source code, are written comments, lazy to write the document)

##### IConfigDataBuilder


| Method                                                       | Role                                                         | Comment                                                      |
| ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ |
| setFrameLayoutId(int id)                                     | Set load location FrameLayout ID                             | Must be set when the build mode is MConstants.BUILD_FRAGMENT |
| setRequestCode(int code)                                     | Set request code                                             | Must be set when build mode is MConstants.BUILD_ACTIVITY     |
| setRootPath(String path)                                     | Set default path to start                                    | Default is the internal storage root path                    |
| setMaxCount(int maxCount)                                    | Set the maximum number of selections                         | No setting default is -1 i.e. no limit                       |
| setShowFileTypes(String... fileTypes)                        | Set the display file type                                    | No suffix please use ""                                      |
| setSelectFileTypes(String... fileTypes)                      | Set the selection file type                                  | No suffix please use ""                                      |
| setSortType(int sortType)                                    | Set sorting rules                                            | See MConstants for types                                     |
| setRadio()                                                   | Set radio selection(Use setMaxCount(0) to replace it if you need a single-selected folder) | Default Multiple Choice                                      |
| setShowSelectStorageBtn(boolean var)                         | Set whether to display the internal storage selection button | Default true                                                 |
| setShowTitlebarFragment(boolean var)                         | Whether to display the title bar                             | Default true                                                 |
| setShowTabbarFragment(boolean var)                           | Whether to show breadcrumbs                                  | Default true                                                 |
| setAlwaysShowHandleFragment(boolean var)                     | Whether to always show the long press pop-up option          | Default false                                                |
| setTitlebarMainTitle(FontBean titlebarMainTitle)             | Set the main title of the title bar                          | You can also set the font size, color, etc.                  |
| setTitlebarBG(Integer titlebarBG)                            | Set the title bar background color                           |                                                              |
| setFileItemListener(FileItemListener fileItemListener)       | Set file item clickback                                      | The callback will be made only if you click on a file, but not if you click on a folder. |
| setMorePopupItemListeners(CommonItemListener... morePopupItemListener) | Set the top right option callback                            |                                                              |
| setHandleItemListeners(CommonItemListener... handleItemListener) | Set long press popup option callback                         |                                                              |
| setTitlebarFragment(AbstractTitlebarFragment titlebarFragment) | Set custom title bar UI                                      | Your own Fragment must extend AbstractTitlebarFragment       |
| setHandleFragment(AbstractHandleFragment handleFragment)     | Set long press to pop up custom UI                           | Your own Fragment must extend AbstractHandleFragment         |
| setLifeCycle(AbstractLifeCycle lifeCycle)                    | Set some life cycle hooks                                    | Just rewrite the corresponding method                        |
| start()                                                      | Start building                                               | Must be called                                               |
| ......                                                       | ......                                                       | ......                                                       |

## V. !!! Special attention !!!

#### Partition Storage

##### The library is also adapted to partitioned storage, no additional adaptation is needed, you just need to write your business code and leave the rest to it.

- Note that the library has been added to the library's `AndroidManifest.xml`：

  ```xml
  <!-- Write access to external storage -->
  <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
  <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
  <!-- Android 11 extra permissions -->
  <uses-permission android:name="android.permission.MANAGE_EXTERNAL_STORAGE"
      tools:ignore="ScopedStorage" />
  <!-- Partitioned storage features have been adapted -->
  <application
          android:preserveLegacyExternalStorage="true"
          android:requestLegacyExternalStorage="true"
          >
  ```

- Error may be reported:

  ```java
  Execution failed for task ':app:processDebugMainManifest'.
  > Manifest merger failed with multiple errors, see logs
  ```

  Please set the `AndroidManifest.xml` in your project to be consistent

#### Includes android.support library

- If your project uses Androidx library, because this library references the [getActivity/XXPermissions](https://github.com/getActivity/XXPermissions) library, which contains the android.support library, it will cause a conflict and report an error. The solution is to add the following to the gradle.properties in the project's home directory

  ```
  android.enableJetifier=true
  ```


#### Version Upgrade

- The new version often solves some of the problems of the old version, increases performance, scalability ...... It is recommended to upgrade to a new version
- Please note that the old version is not compatible with the new version due to the refactoring of the project. 1.0.x upgrade 1.1.x is a non-compatible upgrade, please pay attention to learn the new API


#### Excessive volume

- Already integrated with [Blankj/AndroidUtilCode](https://github.com/Blankj/AndroidUtilCode)

  If the project has strict requirements on size, please download the source code and streamline the AndroidUtilCode module.

#### Code obfuscation

- Generally no configuration is required, obfuscation rules are imported automatically

# Special Thanks

- [getActivity/XXPermissions](https://github.com/getActivity/XXPermissions)
- [CymChad/BaseRecyclerViewAdapterHelper](https://github.com/CymChad/BaseRecyclerViewAdapterHelper)
- [Blankj/AndroidUtilCode](https://github.com/Blankj/AndroidUtilCode)
- [xuexiangjys/XTask](https://github.com/xuexiangjys/XTask)
- [ZLYang110/FileSelector](https://github.com/ZLYang110/FileSelector)
- [zzy0516alex/FileSelectorRelease](https://github.com/zzy0516alex/FileSelectorRelease)
- [folderv/androidDataWithoutRootAPI33](https://github.com/folderv/androidDataWithoutRootAPI33)

Open source projects and their dependencies.

### LICENSE 

```
   Copyright [2020] molihuan

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
```
