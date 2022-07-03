# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# 指定代码的压缩级别，值在0-7之间。一般设置5足矣
-optimizationpasses 5

# 打印混淆信息
-verbose

# 代码优化选项，不加该行会将没有用到的类删除，发布的是代码库这个选项需要
# 在做混淆之前最开始会默认对代码进行压缩，为了增加反编译的难度可以选择不压缩
-dontshrink

# 过滤泛型,出现类型转换错误时再启用这个。目前的项目暂时无泛型类型，我先注释了
-keepattributes Signature

#抛出异常时保留代码行数
-keepattributes SourceFile,LineNumberTable
# 不混淆指定类
-keep public class com.molihuan.pathselector.utils.Commons { *; }
-keep public class com.molihuan.pathselector.utils.FileTools { *; }