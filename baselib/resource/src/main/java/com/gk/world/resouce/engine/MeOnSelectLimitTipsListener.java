package com.gk.world.resouce.engine;

import android.content.Context;

import com.gk.world.resource.R;
import com.luck.picture.lib.config.PictureSelectionConfig;
import com.luck.picture.lib.config.SelectLimitType;
import com.luck.picture.lib.interfaces.OnSelectLimitTipsListener;
import com.luck.picture.lib.utils.ToastUtils;

/**
 * 版权 ： gxd
 *
 * @author: gxd
 * @date: 2022/3/2
 * @time: 17:31
 * @description：
 */
public class MeOnSelectLimitTipsListener implements OnSelectLimitTipsListener {
    @Override
    public boolean onSelectLimitTips(Context context, PictureSelectionConfig config, int limitType) {
        if (limitType == SelectLimitType.SELECT_MAX_VIDEO_SELECT_LIMIT) {
            ToastUtils.showToast(context, context.getString(R.string.ps_message_video_max_num, String.valueOf(config.maxVideoSelectNum)));
            return true;
        }
        return false;
    }
}
