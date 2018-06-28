package medic.mockrdt;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.io.File;

import static medic.mockrdt.MedicLog.info;
import static medic.mockrdt.MedicLog.trace;
import static medic.mockrdt.MedicLog.warn;

/**
 * @see https://developer.android.com/guide/topics/media/camera
 */
public class VerifyActivity extends Activity {
	private Camera camera;

	private PictureCallback pictureCallback = new PictureCallback() {
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			info("Picture taken with %s bytes of data.  Returning to calling app...", data.length);
			try {
				Intent i = new Intent();

				i.putExtra("data", data);

				setResult(Activity.RESULT_OK, i);
				finish();
			} catch (Exception ex) {
				warn(ex, "Problem taking picture.");
			}
		}
	};

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		trace(this, "Started.");

		setTitle("MRDT : " + getTitle());

		setContentView(R.layout.tsk_verify);

		requestRequiredPermission();

		// Create an instance of Camera
		camera = getCameraInstance();

		// Create our Preview view and set it as the content of our activity.
		FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
		preview.addView(new CameraPreview(this, camera));
	}

//> CUSTOM EVENT LISTENERS
	public void btnOk_onClick(View v) {
		camera.takePicture(null, null, pictureCallback);
	}

//> PRIVATE HELPERS
	private void requestRequiredPermission() {
		String[] permissions = { android.Manifest.permission.CAMERA };
		requestPermissions(permissions, 1);
	}

//> STATIC HELPERS
	private static Camera getCameraInstance() {
		Camera c = null;
		try {
			c = Camera.open(); // attempt to get a Camera instance
		} catch (Exception ex) {
			warn(ex, "Could not get camera instance.");
			// Camera is not available (in use or does not exist)
		}
		return c; // returns null if camera is unavailable
	}
}

