package com.api.face.microsoft.microsoftfaceapi.tasks;

import android.graphics.Bitmap;

import com.microsoft.projectoxford.face.FaceServiceClient;
import com.microsoft.projectoxford.face.contract.Face;
import com.microsoft.projectoxford.face.rest.ClientException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.Callable;

/**
 * Created by Alina_Zhdanava on 10/13/2016.
 */

public class DetectImage implements Callable<Face[]> {

    private Bitmap mBitmap;

    public DetectImage(Bitmap bitmap) {
        mBitmap = bitmap;
    }

    @Override
    public Face[] call() throws Exception {
        try {
            return ImageHelper.faceServiceClient.detect(
                    convertBitmapToStream(mBitmap),
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