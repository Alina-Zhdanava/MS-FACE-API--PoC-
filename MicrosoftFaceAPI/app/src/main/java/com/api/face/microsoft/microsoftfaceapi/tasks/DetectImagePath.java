package com.api.face.microsoft.microsoftfaceapi.tasks;

import android.content.Context;
import android.graphics.Bitmap;

import com.bumptech.glide.Glide;
import com.microsoft.projectoxford.face.FaceServiceClient;
import com.microsoft.projectoxford.face.contract.Face;
import com.microsoft.projectoxford.face.rest.ClientException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

/**
 * Created by Alina_Zhdanava on 10/13/2016.
 */

public class DetectImagePath implements Callable<Face[]> {

    private String mImagePath;
    private Context mContext;
    private int mDpWidth;
    private int mDpHeight;


    public DetectImagePath(String imagePath, Context context, int dpWidth, int dpHeight) {
        mImagePath=imagePath;
        mContext =  context;
        mDpWidth = dpWidth;
        mDpHeight = dpHeight;
    }

    @Override
    public Face[] call() throws Exception {
       Bitmap bitmap = Glide.with(mContext).load(mImagePath).asBitmap().into(mDpHeight, mDpHeight / 4).get();
        try {
            return ImageHelper.faceServiceClient.detect(
                    convertBitmapToStream(bitmap),
                    true,       /* Whether to return face ID */
                    true,       /* Whether to return face landmarks */
                                    /* Which face attributes to analyze, currently we support:
                                       age,gender,headPose,smile,facialHair */
                    new FaceServiceClient.FaceAttributeType[]{
                            FaceServiceClient.FaceAttributeType.Age,
                            FaceServiceClient.FaceAttributeType.Gender,
                            FaceServiceClient.FaceAttributeType.Glasses,
                            FaceServiceClient.FaceAttributeType.Smile,
                            FaceServiceClient.FaceAttributeType.HeadPose
                    });
        } catch (ClientException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new Face[0];
    }

    private ByteArrayInputStream convertBitmapToStream(Bitmap image) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 0, outputStream);
        byte[] bitmapdata = outputStream.toByteArray();
        return new ByteArrayInputStream(bitmapdata);
    }

}


