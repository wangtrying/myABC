package tw.idv.terry.myabc;

import android.graphics.Bitmap;

import org.apache.solr.util.ConcurrentLRUCache;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * Created by wangtrying on 2015/3/22.
 */
public class MemoryCache {

    private ConcurrentLRUCache<String, Future<Bitmap>> mMemCache;
//    private LruCache<String, Future<Bitmap>> mMemCache;
    //private ConcurrentLRUCache<String ,Future<Bitmap>> mLruCache = new ConcurrentLRUCache<String, Future<Bitmap>>();

    public MemoryCache(final int aMemCacheSize, int aMemCacheLowerMark) {
        mMemCache = new ConcurrentLRUCache<String, Future<Bitmap>>(aMemCacheSize, aMemCacheLowerMark);
//        mMemCache = new LruCache<>(mMemCacheSize);
    }

    public synchronized Bitmap get(final String aPath, final int aWidth, final int aHeight) {
        final String key = aPath + ", " + aWidth + ", " + aHeight;
        while (true) {
            Future<Bitmap> future = mMemCache.get(key);
            if (future == null) {
                BitmapLoaderCallable myCallable = new BitmapLoaderCallable(aPath, aWidth, aHeight);
                FutureTask<Bitmap> newFuture = new FutureTask<Bitmap>(myCallable);
                future = mMemCache.put(key, newFuture);

                if (future == null) {
                    future = newFuture;
                    newFuture.run();
                }
            }
            // now future is not null
            try {
                return future.get();
            } catch (CancellationException e) {
                e.printStackTrace();
                mMemCache.remove(key);
            } catch (InterruptedException e) {
                e.printStackTrace();
                mMemCache.remove(key);
            } catch (ExecutionException e) {
                e.printStackTrace();
                mMemCache.remove(key);
            } catch (Exception e) {
                e.printStackTrace();
                mMemCache.remove(key);
            }
        }
    }

}
