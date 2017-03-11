package org.fingerblox.fingerblox;

import android.content.Context;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.util.AttributeSet;
import android.util.Log;

import org.opencv.android.JavaCameraView;

import java.util.ArrayList;
import java.util.Collections;


public class CameraView extends JavaCameraView implements PictureCallback {
    private static final String TAG = "cameraView";

    public CameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void takePicture() {
        Log.i(TAG, "Taking picture");

        // Postview and jpeg are sent in the same buffers if the queue is not empty when performing a capture.
        // Clear up buffers to avoid mCamera.takePicture to be stuck because of a memory issue
        mCamera.setPreviewCallback(null);

        // PictureCallback is implemented by the current class
        mCamera.takePicture(null, null, this);
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        Log.i(TAG, "Picture taken!");
        // The camera preview was automatically stopped. Start it again.
        mCamera.startPreview();
        mCamera.setPreviewCallback(this);

        // TODO: Do something with image
    }

    @Override
    protected boolean initializeCamera(int width, int height) {
        boolean res = super.initializeCamera(width, height);
        setFixedFocusDistance();
        return res;
    }

    protected void setFixedFocusDistance() {
        Rect focusRect = new Rect();
        focusRect.set(
                CameraOverlayView.PADDING,
                CameraOverlayView.PADDING,
                getWidth() - CameraOverlayView.PADDING,
                getHeight() - CameraOverlayView.PADDING
        );
        Camera.Area focusArea = new Camera.Area(focusRect, 1000);
        ArrayList<Camera.Area> focusAreaList = new ArrayList<>(Collections.singletonList(focusArea));

        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        parameters.setFocusAreas(focusAreaList);
    }
}
