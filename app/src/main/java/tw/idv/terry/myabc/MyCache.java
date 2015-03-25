package tw.idv.terry.myabc;

import android.content.Context;
import android.graphics.Bitmap;

import tw.idv.terry.myabc.utils.MyLog;

/**
 * Created by wangtrying on 2015/3/21.
 */
public class MyCache {

    private final static String TAG = "MyCache";
    private MemoryCache mMemoryCache;

    public Bitmap getBitmapByPath(final String aPath, final int aWidthInPixel, final int aHeightInPixel) {
        return mMemoryCache.get(aPath, aWidthInPixel, aHeightInPixel);
    }

    public final static class Builder {

        private final static String TAG = "Builder";


        final static int MEGABYTE = 1024 * 1024;
        final static int DEFAULT_MEMORY_CACHE_SIZE = 3; // 3 MB
        final static int DEFAULT_MEMORY_CACHE_LOWERWATERMARK = 1; // 1MB

        private Context mContext;
        private int mMemCacheSize, mMemCacheLowerMark;




        public Builder(final Context aContext) {
            mContext = aContext;
            mMemCacheSize = DEFAULT_MEMORY_CACHE_SIZE * MEGABYTE;
            mMemCacheLowerMark = DEFAULT_MEMORY_CACHE_LOWERWATERMARK * MEGABYTE;
        }

        public Builder(final Context aContext, final int aMemoryCacheSizeInMegaBytes) {
            mContext = aContext;
            mMemCacheLowerMark = DEFAULT_MEMORY_CACHE_LOWERWATERMARK* MEGABYTE;
            if (aMemoryCacheSizeInMegaBytes < DEFAULT_MEMORY_CACHE_LOWERWATERMARK){
                mMemCacheSize = DEFAULT_MEMORY_CACHE_LOWERWATERMARK;
            }else {
                mMemCacheSize = aMemoryCacheSizeInMegaBytes;
            }
        }


        public MyCache build() {
            MyLog.v(TAG, "entering build");

            MyCache cache = new MyCache();
            cache.mMemoryCache = new MemoryCache(mMemCacheSize, mMemCacheLowerMark);

            return cache;
        }
    }

}
