# mlhfileselectorlib

[![Maven Central](https://img.shields.io/maven-central/v/io.github.molihuan/mlhfileselectorlib.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22io.github.molihuan%22%20AND%20a:%22mlhfileselectorlib%22)

提供文件路径选择，支持自动申请读取权限支持安卓11，支持自动获取Android/data目录访问权限（文件浏览还在适配），有非常高的扩展性，支持自定义UI。

## 非常感谢

[ZLYang110/FileSelector: Android 文件选择器，指定选择文件夹还是文件，根据后缀名过滤，支持多选 (github.com)](https://github.com/ZLYang110/FileSelector)

[zzy0516alex/FileSelectorRelease: lib (github.com)](https://github.com/zzy0516alex/FileSelectorRelease)

[getActivity/XXPermissions: Android 权限请求框架，已适配 Android 12 (github.com)](https://github.com/getActivity/XXPermissions)

开源项目以及其依赖项目。

## 1、如何使用

#### 第一步：添加仓库:

- ##### 如果你的项目 Gradle 配置是在 `7.0 以下`，需要在 `build.gradle` 文件中加入

```java
allprojects {
    repositories {
        ...
        mavenCentral()
    }
}
```

- ##### 如果你的 Gradle 配置是 `7.0 及以上`，则需要在 `settings.gradle` 文件中加入

```
dependencyResolutionManagement {
    repositories {
    	...
        mavenCentral()
    }
}
```

#### 第二步：添加远程依赖:

- ##### 配置完远程仓库后，在项目 app 模块下的 `build.gradle` 文件中加入远程依赖

```java
dependencies {
    ...
    // 版本请自行选择
    implementation 'io.github.molihuan:mlhfileselectorlib:1.0.3'
}
```

#### 第三步：基本Java用法示范:

```java
//如果没有权限会自动申请权限
FileSelector.from(this)
    		.requestCode(100) //设置请求码
    		.start();
```

#### 第四步：获取返回的数据:

```java
@Override
protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == 100) {//请求码
        if(data!=null){
            ArrayList<String> essFileList = data.getStringArrayListExtra(Const.EXTRA_RESULT_SELECTION);
            StringBuilder builder = new StringBuilder();
            for (String file : essFileList) {
                builder.append(file).append("");
            }
            ToastUtils.make().show(builder.toString());
        }
    }
}
```

## 2、类与方法（尽量看源码，都写了注释，懒得写文档）

##### SelectCreator

| 方法                                                         | 注释                                           | 备注 |
| ------------------------------------------------------------ | ---------------------------------------------- | ---- |
| setMoreOPtions(String[] optionsName , boolean [] optionsNeedCallBack ,SelectOptions.IToolbarOptionsListener ...onOptionClicks) | 设置toolbar更多选项以及监听（有重载）          |      |
| setFileItemDispose(SelectOptions.IOnFileItemListener onFileItem) | 设置FileItem点击/长按回调                      |      |
| setMaxCount(int maxCount)                                    | 设置最大选择数量                               |      |
| setToolbar(Fragment toolbarFragment)                         | 设置自定义标题栏（建议继承包下的BaseFragment） |      |
| setTargetPath(String path)                                   | 设置开始默认路径                               |      |
| setFileTypes(String... fileTypes)                            | 设置选择文件类型                               |      |
| setSortType(int sortType)                                    | 设置排序规则                                   |      |
| isSingle()                                                   | 设置单选                                       |      |
| onlyShowFolder()                                             | 设置只显示文件夹                               |      |
| onlySelectFolder()                                           | 设置只选择文件夹                               |      |
| requestCode(int requestCode)                                 | 设置请求码                                     |      |
| ......                                                       | ......                                         |      |

#### 进阶：

```java
FileSelector.from(this)
    .onlyShowFolder()  //只显示文件夹
    .onlySelectFolder()  //只能选择文件夹
    .isSingle() // 只能选择一个
    .setMaxCount(5) //设置最大选择数
    .setFileTypes("png", "doc","apk", "mp3", "gif", "txt", "mp4") //设置文件类型
    .setSortType(FileSelector.BY_NAME_ASC) //设置名字排序
    //.setSortType(FileSelector.BY_TIME_ASC) //设置时间排序
    //.setSortType(FileSelector.BY_SIZE_DESC) //设置大小排序
    .requestCode(100) //设置返回码
    .setTargetPath("/storage/emulated/0/") //设置默认目录
    .setToolbar(new EmptyFragment())//自定义Toolbar建议继承库中的BaseFragment
    //自定义Toolbar更多按钮以及监听事件
    .setMoreOPtions(new String[]{"选择","删除"},
                    new boolean[]{true,false},
                    new SelectOptions.IToolbarOptionsListener() {
                        @Override
                        public void onOptionClick(Context context, int position, String currentPath, ArrayList<FileBean> selectedFileList, ArrayList<String> selectedFilePathList, FileListAdapter adapter) {
                            ToastUtils.make().show("选择"+currentPath);
                        }
                    },
                    new SelectOptions.IToolbarOptionsListener() {
                        @Override
                        public void onOptionClick(Context context, int position, String currentPath, ArrayList<FileBean> selectedFileList, ArrayList<String> selectedFilePathList, FileListAdapter adapter) {
                            ToastUtils.make().show("删除"+currentPath);
                        }
                    }
                   )
    //item点击or长按回调
    .setFileItemDispose(new SelectOptions.IOnFileItemListener() {
        @Override
        public void onFileItemClick(Context context, int position, String fileAbsolutePath, ArrayList<FileBean> selectedFileList, ArrayList<String> selectedFilePathList, FileListAdapter adapter) {
            ToastUtils.make().show("点击"+fileAbsolutePath);
        }
        @Override
        public void onLongFileItemClick(Context context, int position, String fileAbsolutePath, ArrayList<FileBean> selectedFileList, ArrayList<String> selectedFilePathList, FileListAdapter adapter) {
            ToastUtils.make().show("长按"+fileAbsolutePath);
        }
    })
    .start();
```



## 3、！！！特别注意！！！

#### 分区存储

##### 该库以及适配了分区存储，不需要额外适配，你只需要写你的业务代码即可，其他的交给它。

- 注意该库已经在库的`AndroidManifest.xml`中添加了：

  ```xml
  <!-- 外部存储的写权限 -->
  <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
  <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
  <!-- 安卓11额外权限 -->
  <uses-permission android:name="android.permission.MANAGE_EXTERNAL_STORAGE"
      tools:ignore="ScopedStorage" />
  <!-- 已经适配了分区存储特性 -->
  <application
          android:preserveLegacyExternalStorage="true"
          android:requestLegacyExternalStorage="true"
          >
  ```

- 可能会报错：

  ```java
  Execution failed for task ':app:processDebugMainManifest'.
  > Manifest merger failed with multiple errors, see logs
  ```

  请在你的项目中的`AndroidManifest.xml`设置一致
