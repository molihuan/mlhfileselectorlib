package com.molihuan.pathselector.entity;

import android.graphics.Color;

import androidx.annotation.DrawableRes;

import java.io.Serializable;

/**
 * @ClassName: FontBean
 * @Author: molihuan
 * @Date: 2022/11/26/12:39
 * @Description: 字---样式实体
 */
public class FontBean implements Serializable {
    private CharSequence text;//文本
    private Integer size;//大小
    private Integer color;//颜色
    private Integer leftIcoResId;//左边图片资源id

    public FontBean(CharSequence text) {
        this(text, 18);
    }

    public FontBean(CharSequence text, Integer size) {
        this(text, size, Color.BLACK);
    }

    public FontBean(CharSequence text, Integer size, Integer color) {
        this(text, size, color, null);
    }

    public FontBean(CharSequence text, Integer size, Integer color, @DrawableRes Integer leftIcoResId) {
        this.text = text;
        this.size = size;
        this.color = color;
        this.leftIcoResId = leftIcoResId;
    }

    public CharSequence getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getColor() {
        return color;
    }

    public void setColor(Integer color) {
        this.color = color;
    }

    public Integer getLeftIcoResId() {
        return leftIcoResId;
    }

    public void setLeftIcoResId(@DrawableRes Integer leftIcoResId) {
        this.leftIcoResId = leftIcoResId;
    }
}
