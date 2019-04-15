package ylz.android.engine.paging.main.utils;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

public class GlideUtils {

    private GlideUtils() {
    }

    public static GlideUtils getInstance() {
        return GlideUtils.Singleton.instance;
    }

    public void load(ImageView targetImageView, String url, boolean isCircle, int... defaultResId) {
        RequestOptions options = this.createRequestOptions(isCircle);
        if (defaultResId != null && defaultResId.length > 0) {
            options.placeholder(defaultResId[0]).error(defaultResId[0]);
        }

        Glide.with(targetImageView.getContext()).load(url).apply(options).into(targetImageView);
    }

    public void load(ImageView targetImageView, Drawable drawable, boolean isCircle) {
        Glide.with(targetImageView.getContext()).asDrawable().load(drawable).apply(this.createRequestOptions(isCircle)).into(targetImageView);
    }

    public void load(ImageView targetImageView, String url, int ainmId, int duration, boolean isCircle) {
        Glide.with(targetImageView.getContext()).load(url).apply(this.createRequestOptions(isCircle)).into(targetImageView);
    }

    public void load(ImageView targetImageView, int resId, boolean isCircle) {
        Glide.with(targetImageView.getContext()).load(resId).apply(this.createRequestOptions(isCircle)).into(targetImageView);
    }

    public void loadRoundCorners(ImageView targetImageView, String url, int radius, int... defaultResId) {
        RequestOptions options = this.createRoundCornersOptions(radius);
        if (defaultResId != null && defaultResId.length > 0) {
            options.placeholder(defaultResId[0]).error(defaultResId[0]);
        }

        Glide.with(targetImageView.getContext()).load(url).apply(options).into(targetImageView);
    }

    public void loadRoundCornersNoCache(ImageView targetImageView, String url, int radius, int... defaultResId) {
        RequestOptions options = this.createRoundCornersOptionsNoCache(radius);
        if (defaultResId != null && defaultResId.length > 0) {
            options.placeholder(defaultResId[0]).error(defaultResId[0]);
        }

        Glide.with(targetImageView.getContext()).load(url).apply(options).into(targetImageView);
    }

    private RequestOptions createRequestOptions(boolean isCircle) {
        RequestOptions options = (new RequestOptions()).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        if (isCircle) {
            options.circleCrop();
        }

        return options;
    }

    private RequestOptions createRoundCornersOptions(int radius) {
        RequestOptions options = RequestOptions.bitmapTransform(new RoundedCorners(radius));
        return options.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
    }

    private RequestOptions createRoundCornersOptionsNoCache(int radius) {
        RequestOptions options = RequestOptions.bitmapTransform(new RoundedCorners(radius));
        return options.diskCacheStrategy(DiskCacheStrategy.NONE);
    }

    private static class Singleton {
        private static GlideUtils instance = new GlideUtils();

        private Singleton() {
        }
    }
}
