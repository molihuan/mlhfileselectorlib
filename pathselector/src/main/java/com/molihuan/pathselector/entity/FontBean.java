package com.molihuan.pathselector.entity;

import android.graphics.Color;

import java.io.Serializable;

/**
 * @ClassName: FontBean
 * @Author: molihuan
 * @Date: 2022/11/26/12:39
 * @Description: 字---样式实体
 */
public class FontBean implements Serializable {
    private String text;//文本
    private Integer size;//大小
    private Integer color;//颜色

    public FontBean(String text) {
        this(text, 18, Color.BLACK);
    }

    public FontBean(String text, Integer size) {
        this(text, size, Color.BLACK);
    }

    public FontBean(String text, Integer size, Integer color) {
        this.text = text;
        this.size = size;
        this.color = color;
    }

    public String getText() {
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
}
