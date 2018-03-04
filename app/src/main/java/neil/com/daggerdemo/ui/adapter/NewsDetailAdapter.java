package neil.com.daggerdemo.ui.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import neil.com.daggerdemo.R;
import neil.com.daggerdemo.bean.NewsDetail;
import neil.com.daggerdemo.utils.GlideImageLoader;

/**
 * 新闻Adapter
 * Created by neil on 2018/3/3 0003.
 */

public class NewsDetailAdapter extends
        BaseMultiItemQuickAdapter<NewsDetail.ItemBean, BaseViewHolder> {
    private Context mContext;

    public NewsDetailAdapter(List<NewsDetail.ItemBean> data, Context context) {
        super(data);
        this.mContext = context;
        // 显示形式单图
        addItemType(NewsDetail.ItemBean.TYPE_DOC_TITLEIMG, R.layout.item_detail_doc);
        // 显示形式多图
        addItemType(NewsDetail.ItemBean.TYPE_DOC_SLIDEIMG, R.layout.item_detail_doc_slideimg);
        // 显示广告 加内容
        addItemType(NewsDetail.ItemBean.TYPE_ADVERT_TITLEIMG, R.layout.item_detail_advert);
        // 广告多图
        addItemType(NewsDetail.ItemBean.TYPE_ADVERT_SLIDEIMG, R.layout.item_detail_advert_slideimg);
        // 广告长图
        addItemType(NewsDetail.ItemBean.TYPE_ADVERT_LONGIMG, R.layout.item_detail_advert_longimage);
        // 长图咨询详情
        addItemType(NewsDetail.ItemBean.TYPE_SLIDE, R.layout.item_detail_slide);
        // 视频
        addItemType(NewsDetail.ItemBean.TYPE_PHVIDEO, R.layout.item_detail_phvideo);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, NewsDetail.ItemBean bean) {
        switch (baseViewHolder.getItemViewType()) {
            case NewsDetail.ItemBean.TYPE_DOC_TITLEIMG:
                baseViewHolder.setText(R.id.tv_title, bean.getTitle());
                baseViewHolder.setText(R.id.tv_source, bean.getSource());
                baseViewHolder.setText(R.id.tv_commnetsize,
                        String.format(mContext.getResources().getString(R.string.news_commentsize), bean.getCommentsall()));
                GlideImageLoader.loadImage(mContext, bean.getThumbnail(), (ImageView) baseViewHolder.getView(R.id.iv_logo));
                baseViewHolder.addOnClickListener(R.id.iv_close);
                break;
            case NewsDetail.ItemBean.TYPE_DOC_SLIDEIMG:
                baseViewHolder.setText(R.id.tv_title, bean.getTitle());
                baseViewHolder.setText(R.id.tv_source, bean.getSource());
                baseViewHolder.setText(R.id.tv_commnetsize,
                        String.format(mContext.getResources().getString(R.string.news_commentsize), bean.getCommentsall()));
                try {
                    GlideImageLoader.loadImage(mContext, bean.getStyle().getImages().get(0), (ImageView) baseViewHolder.getView(R.id.iv_1));
                    GlideImageLoader.loadImage(mContext, bean.getStyle().getImages().get(1), (ImageView) baseViewHolder.getView(R.id.iv_2));
                    GlideImageLoader.loadImage(mContext, bean.getStyle().getImages().get(2), (ImageView) baseViewHolder.getView(R.id.iv_3));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                baseViewHolder.addOnClickListener(R.id.iv_close);
                break;
            case NewsDetail.ItemBean.TYPE_ADVERT_TITLEIMG:
                baseViewHolder.setText(R.id.tv_title, bean.getTitle());
                GlideImageLoader.loadImage(mContext, bean.getThumbnail(), (ImageView) baseViewHolder.getView(R.id.iv_logo));
                baseViewHolder.addOnClickListener(R.id.iv_close);
                break;
            case NewsDetail.ItemBean.TYPE_ADVERT_SLIDEIMG:
                baseViewHolder.setText(R.id.tv_title, bean.getTitle());
                try {
                    GlideImageLoader.loadImage(mContext, bean.getStyle().getImages().get(0), (ImageView) baseViewHolder.getView(R.id.iv_1));
                    GlideImageLoader.loadImage(mContext, bean.getStyle().getImages().get(1), (ImageView) baseViewHolder.getView(R.id.iv_2));
                    GlideImageLoader.loadImage(mContext, bean.getStyle().getImages().get(2), (ImageView) baseViewHolder.getView(R.id.iv_3));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                baseViewHolder.addOnClickListener(R.id.iv_close);
                break;
            case NewsDetail.ItemBean.TYPE_ADVERT_LONGIMG:
                baseViewHolder.setText(R.id.tv_title, bean.getTitle());
                GlideImageLoader.loadImage(mContext, bean.getThumbnail(), (ImageView) baseViewHolder.getView(R.id.iv_logo));
                baseViewHolder.addOnClickListener(R.id.iv_close);
                break;
            case NewsDetail.ItemBean.TYPE_SLIDE:
                baseViewHolder.setText(R.id.tv_title, bean.getTitle());
                baseViewHolder.setText(R.id.tv_source, bean.getSource());
                baseViewHolder.setText(R.id.tv_commnetsize,
                        String.format(mContext.getResources().getString(R.string.news_commentsize), bean.getCommentsall()));
                GlideImageLoader.loadImage(mContext, bean.getThumbnail(), (ImageView) baseViewHolder.getView(R.id.iv_logo));
                baseViewHolder.addOnClickListener(R.id.iv_close);
                break;
            case NewsDetail.ItemBean.TYPE_PHVIDEO:
                baseViewHolder.setText(R.id.tv_title, bean.getTitle());
                baseViewHolder.setText(R.id.tv_source, bean.getSource());
                baseViewHolder.setText(R.id.tv_commnetsize,
                        String.format(mContext.getResources().getString(R.string.news_commentsize), bean.getCommentsall()));
                baseViewHolder.addOnClickListener(R.id.iv_close);
                GlideImageLoader.loadImage(mContext, bean.getThumbnail(), (ImageView) baseViewHolder.getView(R.id.iv_logo));
                break;
        }
    }

}
