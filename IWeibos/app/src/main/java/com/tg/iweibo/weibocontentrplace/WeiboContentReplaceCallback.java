package com.tg.iweibo.weibocontentrplace;

/**
 * Created by jake（lian_weijian@126.com）
 * Date: 2015-01-30
 * Time: 11:34
 */

import android.graphics.Bitmap;
import android.text.SpannableStringBuilder;

/**
 *回调接口
 */
public interface WeiboContentReplaceCallback {

    //回调接口方法，刷新UI
    public void refresh(SpannableStringBuilder weiboContent);
}
