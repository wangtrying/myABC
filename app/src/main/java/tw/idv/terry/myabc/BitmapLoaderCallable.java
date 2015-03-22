package tw.idv.terry.myabc;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.concurrent.Callable;

import tw.idv.terry.myabc.utils.MyLog;

/**
 * Created by wangtrying on 2015/3/22.
 */
public class BitmapLoaderCallable implements Callable<Bitmap> {

    private final int mHeight;
    private final String mPath;
    private final int mWidth;

    public BitmapLoaderCallable(final String aPath, final int aWidth, final int aHeight){
        mPath = aPath;
        mWidth = aWidth;
        mHeight = aHeight;
    }

    @Override
    public Bitmap call() throws Exception {
        return prepareBitmapFromAndroid();
    }

    private Bitmap prepareBitmapFromAndroid() {

        try {
            BitmapFactory.Options option = prepareBitmapOption();
            Bitmap result = decodeBitmap(option);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            MyLog.catchIt(e);
        }
        return null;
    }

    private Bitmap decodeBitmap(BitmapFactory.Options option) throws FileNotFoundException {
        InputStream inStream = new FileInputStream(mPath);
        return BitmapFactory.decodeStream(inStream, null, option);
    }

    private BitmapFactory.Options prepareBitmapOption() throws Exception{
        BitmapFactory.Options option = new BitmapFactory.Options();
        option.inJustDecodeBounds = true;

        InputStream inStream = new FileInputStream(mPath);
        BitmapFactory.decodeStream(inStream, null, option);
        inStream.close();
        inStream = null;
        option.inSampleSize = calculateInSampleSize(option);
        return option;
    }

    private int calculateInSampleSize(BitmapFactory.Options aOption) {
        final int height = aOption.outHeight;
        final int width = aOption.outWidth;
        int inSampleSize = 1;

        if (height > mHeight || width > mWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > mHeight && (halfWidth / inSampleSize) > mWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;

    }
}
