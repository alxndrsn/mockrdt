package medic.mockrdt;

import android.content.Context;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import static medic.mockrdt.MedicLog.warn;

/**
 * @see https://developer.android.com/guide/topics/media/camera
 */
class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
	private SurfaceHolder holder;
	private Camera camera;

	public CameraPreview(Context context, Camera camera) {
		super(context);
		this.camera = camera;

		// Install a SurfaceHolder.Callback so we get notified when the
		// underlying surface is created and destroyed.
		holder = getHolder();
		holder.addCallback(this);
		// deprecated setting, but required on Android versions prior to 3.0
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	public void surfaceCreated(SurfaceHolder holder) {
		// The Surface has been created, now tell the camera where to draw the preview.
		try {
			camera.setPreviewDisplay(holder);
			camera.startPreview();
		} catch (Exception ex) {
			warn(ex, "Error setting camera preview.");
		}
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		// empty. Take care of releasing the Camera preview in your activity.
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		// If your preview can change or rotate, take care of those events here.
		// Make sure to stop the preview before resizing or reformatting it.

		if (holder.getSurface() == null) {
		  // preview surface does not exist
		  return;
		}

		// stop preview before making changes
		try {
			camera.stopPreview();
		} catch (Exception ex) {
		  // ignore: tried to stop a non-existent preview
		}

		// set preview size and make any resize, rotate or
		// reformatting changes here

		// start preview with new settings
		try {
			camera.setPreviewDisplay(holder);
			camera.startPreview();

		} catch (Exception ex) {
			warn(ex, "Error starting camera preview.");
		}
	}
}
