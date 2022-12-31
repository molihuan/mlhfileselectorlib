package com.molihuan.pathselector.listener;

import com.molihuan.pathselector.entity.FontBean;

/**
 * @ClassName: BaseItemListener
 * @Author: molihuan
 * @Date: 2022/11/26/14:58
 * @Description:
 */
public class BaseItemListener {
    protected FontBean fontBean;

    public BaseItemListener(CharSequence text) {
        fontBean = new FontBean(text);
    }

    public BaseItemListener(FontBean fontBean) {
        this.fontBean = fontBean;
    }

    public FontBean getFontBean() {
        return fontBean;
    }

    public void setFontBean(FontBean fontBean) {
        this.fontBean = fontBean;
    }

}
