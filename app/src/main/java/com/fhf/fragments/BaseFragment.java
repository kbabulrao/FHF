package com.fhf.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fhf.constants.AppConstants;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Santosh on 11/6/2015.
 */
public class BaseFragment extends Fragment {

    protected String TAG = BaseFragment.class.getName();
    protected LayoutInflater inflater;
    public ProgressBar pgrBar;
    private Dialog errorDialog;
    InputMethodManager imm;
    protected Uri fileUri; // file url to store image
    protected TextView noDataSearchIcon, noDataMsg;

    private int PERMISSION_ALL = 0;
    /**
     * Permissions required to read and write storage and camera. Used by the {@link BaseFragment}.
     */
    protected String[] PERMISSIONS = {Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        inflater = LayoutInflater.from(getActivity().getBaseContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    /*public void showErrorDialog(String message, String title) {
        if (errorDialog == null) {
            errorDialog = new Dialog(getActivity(), 0);
            errorDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            errorDialog.setContentView(R.layout.error_dialog_layout);
            errorDialog.setCancelable(false);
        }

        TextView errorContent = (TextView) errorDialog.findViewById(R.id.t_error_dlg_content);
        errorContent.setText(message);
        TextView errorTitle = (TextView) errorDialog.findViewById(R.id.t_error_dlg_title);
        errorTitle.setText(title);
        errorDialog.findViewById(R.id.tv_error_dlg_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                errorDialog.dismiss();
            }
        });
        errorDialog.show();
    }*/

    public void hideKeyBoard(View view) {
        Log.v("hide keyboard", "hide keyboard");
        if (getActivity() != null && view != null) {
            imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            Log.v("hide keyboard", "hide keyboard");
            if (view instanceof AutoCompleteTextView || view instanceof EditText)
                view.setFocusable(true);
        }
    }

    public void showKeyBoard(View view) {
        Log.v("show keyboard", "show keyboard");
        if (getActivity() != null && view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            if (view instanceof EditText)
                view.setFocusable(true);
        }
    }

    protected void selectFile() {
        if (ActivityCompat.checkSelfPermission(getActivity(),
                PERMISSIONS[2])
                != PackageManager.PERMISSION_GRANTED) {
            requestStoragePermission(PERMISSIONS[2]);
        } else {
            Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
            photoPickerIntent.setType("*/*");
            startActivityForResult(photoPickerIntent, AppConstants.SELECT_PHOTO);
        }
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public void checkPermission() {
        // Check if the Camera permission is already available.
        if (!hasPermissions(getActivity(), PERMISSIONS)) {
            ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, PERMISSION_ALL);
        } else {
            // Camera permissions is already available, show the camera preview.
            Log.i(TAG,
                    "CAMERA permission has already been granted. Displaying camera preview.");
            captureImage();
        }
    }

    /**
     * Launching camera app to capture image
     */
    protected void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion <= android.os.Build.VERSION_CODES.M) {
            // Do something for lollipop and below versions
            fileUri = getOutputMediaFileUri(AppConstants.MEDIA_TYPE_IMAGE);
        } else {
            // do something for phones running an SDK above M
            fileUri = FileProvider.getUriForFile(getActivity(),
                    "com.yei.app.fileprovider",
                    getOutputMediaFile(AppConstants.MEDIA_TYPE_IMAGE));
        }

        if (fileUri != null) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

            // start the image capture Intent
            startActivityForResult(intent, AppConstants.CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
        }
    }

    /**
     * Creating file uri to store image/video
     */
    public Uri getOutputMediaFileUri(int type) {
        if (ActivityCompat.checkSelfPermission(getActivity(),
                PERMISSIONS[2])
                != PackageManager.PERMISSION_GRANTED) {
            requestStoragePermission(PERMISSIONS[2]);
        } else
            return Uri.fromFile(getOutputMediaFile(type));

        return null;
    }

    /**
     * returning image / video
     */
    private File getOutputMediaFile(int type) {
        File mediaFile = null;
        if (ActivityCompat.checkSelfPermission(getActivity(),
                PERMISSIONS[1])
                != PackageManager.PERMISSION_GRANTED) {
            requestStoragePermission(PERMISSIONS[1]);
        } else {
            // External sdcard location
            File mediaStorageDir = new File(
                    getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                    AppConstants.IMAGE_DIRECTORY_NAME);

            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    Log.d(TAG, "Oops! Failed create "
                            + AppConstants.IMAGE_DIRECTORY_NAME + " directory");
                    return null;
                }
            }

            // Create a media file username
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                    Locale.getDefault()).format(new Date());
            if (type == AppConstants.MEDIA_TYPE_IMAGE) {
                mediaFile = new File(mediaStorageDir.getPath() + File.separator
                        + "IMG_" + timeStamp + ".jpg");
                Log.d("media file", mediaFile.getPath());
            } else {
                return null;
            }
        }
        return mediaFile;
    }

    protected void requestStoragePermission(String permission) {
        Log.i(TAG, "STORAGE permission has NOT been granted. Requesting permission.");

        // BEGIN_INCLUDE(camera_permission_request)
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                permission)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example if the user has previously denied the permission.
            Log.i(TAG,
                    "Accessing storage permission rationale to provide additional context.");
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{permission},
                    100);
        } else {
            Log.i(TAG,
                    "Storage permission has not been granted yet. Request it directly.");
            // Camera permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(getActivity(), new String[]{permission},
                    100);
        }
        // END_INCLUDE(camera_permission_request)
    }

    /**
     * Requests the Camera permission.
     * If the permission has been denied previously, otherwise it is requested directly.
     */
    protected void requestCameraPermission() {
        Log.i(TAG, "CAMERA permission has NOT been granted. Requesting permission.");

        // BEGIN_INCLUDE(camera_permission_request)
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                Manifest.permission.CAMERA)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example if the user has previously denied the permission.
            Log.i(TAG,
                    "Displaying camera permission rationale to provide additional context.");
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.CAMERA},
                    AppConstants.CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
        } else {
            Log.i(TAG,
                    "Camera permission has not been granted yet. Request it directly.");
            // Camera permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA},
                    AppConstants.CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
        }
        // END_INCLUDE(camera_permission_request)
    }

    @SuppressLint("NewApi")
    public String getPath(final Context context, final Uri uri) {
        final boolean isKitKatOrAbove = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKatOrAbove && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

//                if ("primary".equalsIgnoreCase(type)) {
                return Environment.getExternalStorageDirectory() + "/" + split[1];
//                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public String getFileExt(String fileName) {
        if (fileName != null)
            return fileName.substring((fileName.lastIndexOf(".") + 1), fileName.length());
        else
            return "";
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
}
