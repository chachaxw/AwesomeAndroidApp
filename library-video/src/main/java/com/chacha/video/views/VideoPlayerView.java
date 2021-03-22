package com.chacha.video.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.chacha.video.R;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYBaseVideoPlayer;

/**
 * @author chacha
 * @project AwesomeAndroidApp
 * @package_name com.chacha.video.helper
 * @date 3/21/21
 * @time 2:25 PM
 *
 * <p>
 *     自定义带封面带视频播放器界面
 * </p>
 */
public class VideoPlayerView extends StandardGSYVideoPlayer {
    int defaultRes;

    ImageView coverImage;

    String coverOriginalUrl;

    public RelativeLayout videoContent;

    public VideoPlayerView(Context context, Boolean fullFlag) {
        super(context, fullFlag);
    }

    public VideoPlayerView(Context context) {
        super(context);
    }

    public VideoPlayerView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override
    public int getLayoutId() {
        return R.layout.video_cover_layout;
    }

    @Override
    protected void init(Context context) {
        super.init(context);

        coverImage = findViewById(R.id.thumbImage);
        videoContent = findViewById(R.id.surface_container);
    }

    /**
     * 设置视频url
     *
     * @param url String
     */
    public void setVideoUrl(String url) {
        mUrl = url;
        mOriginUrl = url;
    }

    /**
     * 视频是否缓存
     *
     * @param cache boolean
     */
    public void setVideoCache(boolean cache) {
        mCache = cache;
    }

    /**
     * 视频标题
     *
     * @param title String
     */
    public void setVideoTitle(String title) {
        mTitle = title;
    }

    /**
     * 保存播放状态
     *
     * @return VideoPlayerView
     */
    public VideoPlayerView savePlayerState() {
        VideoPlayerView view = new VideoPlayerView(getContext());
        cloneParams(this, view);
        return view;
    }

    public void cloneState(VideoPlayerView view) {
        cloneParams(view, this);
    }

    @Override
    protected void cloneParams(GSYBaseVideoPlayer from, GSYBaseVideoPlayer to) {
        super.cloneParams(from, to);
        VideoPlayerView viewFrom = (VideoPlayerView)from;
        VideoPlayerView viewTo = (VideoPlayerView)to;
        viewFrom.mShowFullAnimation = viewTo.mShowFullAnimation;
    }

    /**
     * 加载视频封面图片
     *
     * @param url String
     * @param res int
     */
    public void loadCoverImage(String url, int res) {
        coverOriginalUrl = url;
        defaultRes = res;
        Glide.with(getContext().getApplicationContext())
                .load(url)
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(40)))
                .into(coverImage);
    }
}
