package neil.com.daggerdemo.ui.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import fm.jiecao.jcvideoplayer_lib.JCUserAction;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import neil.com.daggerdemo.R;
import neil.com.daggerdemo.bean.VideoDetailBean;
import neil.com.daggerdemo.utils.GlideImageLoader;

/**
 * 视频item 适配器
 *
 * @author neil
 * @date 2018/3/9
 */
public class VideoDetailAdapter extends BaseQuickAdapter<VideoDetailBean.ItemBean, BaseViewHolder> {

    Context mContext;

    public VideoDetailAdapter(Context context, int layoutResId, @Nullable List<VideoDetailBean.ItemBean> data) {
        super(layoutResId, data);
        this.mContext = context;
    }

    @Override
    protected void convert(final BaseViewHolder viewHolder, VideoDetailBean.ItemBean itemBean) {
        JCVideoPlayerStandard jcVideoPlayerStandard = viewHolder.getView(R.id.videoplayer);
        jcVideoPlayerStandard.setUp(
                itemBean.getVideo_url(), JCVideoPlayer.CURRENT_STATE_NORMAL, itemBean.getTitle());
        jcVideoPlayerStandard.setJcUserAction(new JCUserAction() {
            @Override
            public void onEvent(int type, String url, int screen, Object... objects) {
                switch (type) {
                    case JCUserAction.ON_CLICK_START_ICON:
                        viewHolder.getView(R.id.tv_videoduration).setVisibility(View.GONE);
                }
            }
        });

        GlideImageLoader.loadImage(mContext, itemBean.getImage(), jcVideoPlayerStandard.thumbImageView);
        viewHolder.setText(R.id.tv_videoduration, conversionTime(itemBean.getDuration()));
        if (!TextUtils.isEmpty(itemBean.getPlayTime())) {
            viewHolder.setText(R.id.tv_playtime, conversionPlayTime(
                    Integer.valueOf(itemBean.getPlayTime())));
        }

    }

    private String conversionTime(int duration) {
        int minutes = duration / 60;
        int seconds = duration - minutes * 60;
        String m = sizeOf(minutes) > 1 ? String.valueOf(minutes) : "0" + minutes;
        String s = sizeOf(seconds) > 1 ? String.valueOf(seconds) : "0" + seconds;
        return m + ":" + s;
    }

    /**
     * 视频已经播放次数
     *
     * @param playtime
     * @return
     */
    private String conversionPlayTime(int playtime) {
        if (sizeOf(playtime) > 4) {
            return accuracy(playtime, 10000, 1) + "万";
        } else {
            return String.valueOf(playtime);
        }
    }

    public static String accuracy(double num, double total, int digit) {
        DecimalFormat df = (DecimalFormat) NumberFormat.getInstance();
        //可以设置精确几位小数
        df.setMaximumFractionDigits(digit);
        //模式 例如四舍五入
        df.setRoundingMode(RoundingMode.HALF_UP);
        double accuracy_num = num / total;
        return df.format(accuracy_num);
    }

    /**
     * 判断是几位数字
     *
     * @param size
     * @return
     */
    private int sizeOf(int size) {
        final int[] sizeTable = {9, 99, 999, 9999, 99999, 999999, 9999999,
                99999999, 999999999, Integer.MAX_VALUE};
        for (int i = 0; ; i++)
            if (size <= sizeTable[i]) {
                return i + 1;
            }
    }
}
