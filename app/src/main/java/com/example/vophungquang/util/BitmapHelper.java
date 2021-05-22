package com.example.vophungquang.util;
/**
 * Created by vophungquang
 */
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.vophungquang.R;

import java.io.ByteArrayOutputStream;

public class BitmapHelper {
    public static Bitmap convertArrayToBipmap(byte[] bytes) {
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    public static byte[] convertBipmapToArray(Context context) {
        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_no_image);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        return byteArray;
    }
}