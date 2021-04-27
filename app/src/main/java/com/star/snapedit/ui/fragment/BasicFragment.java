package com.star.snapedit.ui.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.PermissionRequest;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.star.snapedit.R;
import com.star.snapedit.cropview.CropImageView;
import com.star.snapedit.cropview.callback.CropCallback;
import com.star.snapedit.cropview.callback.LoadCallback;
import com.star.snapedit.cropview.callback.SaveCallback;
import com.star.snapedit.cropview.util.Logger;
import com.star.snapedit.cropview.util.Utils;
import com.star.snapedit.ui.activity.DashboardActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

//import permissions.dispatcher.NeedsPermission;
//import permissions.dispatcher.OnShowRationale;
//import permissions.dispatcher.PermissionRequest;
//import permissions.dispatcher.RuntimePermissions;

public class BasicFragment extends Fragment {
  protected static final String TAG = BasicFragment.class.getSimpleName();

  protected static final int REQUEST_PICK_IMAGE = 10011;
  protected static final int REQUEST_SAF_PICK_IMAGE = 10012;
  protected static final String PROGRESS_DIALOG = "ProgressDialog";
  protected static final String KEY_FRAME_RECT = "FrameRect";
  protected static final String KEY_SOURCE_URI = "SourceUri";

  // Views ///////////////////////////////////////////////////////////////////////////////////////
  protected CropImageView mCropView;
  protected Bitmap.CompressFormat mCompressFormat = Bitmap.CompressFormat.JPEG;
  protected RectF mFrameRect = null;
  protected Uri mSourceUri = null;

  public static BasicFragment newInstance() {
    BasicFragment fragment = new BasicFragment();
    Bundle args = new Bundle();
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setRetainInstance(true);
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    // bind Views
    bindViews(view);

    mCropView.setDebug(true);

    if (savedInstanceState != null) {
      // restore data
      mFrameRect = savedInstanceState.getParcelable(KEY_FRAME_RECT);
      mSourceUri = savedInstanceState.getParcelable(KEY_SOURCE_URI);
    }

    if (mSourceUri == null) {
      // default data
      mSourceUri = Uri.parse("android.resource://"+ getActivity().getPackageName() + R.drawable.app_preview);
      Log.e("aoki", "mSourceUri = "+mSourceUri);
    }
    // load image
    mCropView.load(mSourceUri)
        .initialFrameRect(mFrameRect)
        .useThumbnail(true)
        .execute(mLoadCallback);
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    // save data
    outState.putParcelable(KEY_FRAME_RECT, mCropView.getActualCropRect());
    outState.putParcelable(KEY_SOURCE_URI, mCropView.getSourceUri());
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent result) {
    super.onActivityResult(requestCode, resultCode, result);
    if (resultCode == Activity.RESULT_OK) {
      // reset frame rect
      mFrameRect = null;
      switch (requestCode) {
        case REQUEST_PICK_IMAGE:
          mSourceUri = result.getData();
          mCropView.load(mSourceUri)
              .initialFrameRect(mFrameRect)
              .useThumbnail(true)
              .execute(mLoadCallback);
          break;
        case REQUEST_SAF_PICK_IMAGE:
          mSourceUri = Utils.ensureUriPermission(getContext(), result);
          mCropView.load(mSourceUri)
              .initialFrameRect(mFrameRect)
              .useThumbnail(true)
              .execute(mLoadCallback);
          break;
      }
    }
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                         @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//    BasicFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
  }

  // Bind views //////////////////////////////////////////////////////////////////////////////////

  protected void bindViews(View view) {
    mCropView = (CropImageView) view.findViewById(R.id.picked_img);
  }

   public void pickImage() {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
      startActivityForResult(new Intent(Intent.ACTION_GET_CONTENT).setType("image/*"),
          REQUEST_PICK_IMAGE);
    } else {
      Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
      intent.addCategory(Intent.CATEGORY_OPENABLE);
      intent.setType("image/*");
      startActivityForResult(intent, REQUEST_SAF_PICK_IMAGE);
    }
  }

  public void cropImage() {
    mCropView.crop(mSourceUri).execute(mCropCallback);
  }

  public Uri createSaveUri() {
    return createNewUri(getContext(), mCompressFormat);
  }

  public static String getDirPath() {
    String dirPath = "";
    File imageDir = null;
    File extStorageDir = Environment.getExternalStorageDirectory();
    if (extStorageDir.canWrite()) {
      imageDir = new File(extStorageDir.getPath() + "/simplecropview");
    }
    if (imageDir != null) {
      if (!imageDir.exists()) {
        imageDir.mkdirs();
      }
      if (imageDir.canWrite()) {
        dirPath = imageDir.getPath();
      }
    }
    return dirPath;
  }



  public static Uri createNewUri(Context context, Bitmap.CompressFormat format) {
    long currentTimeMillis = System.currentTimeMillis();
    Date today = new Date(currentTimeMillis);
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
    String title = dateFormat.format(today);
    String dirPath = getDirPath();
    String fileName = "scv" + title + "." + getMimeType(format);
    String path = dirPath + "/" + fileName;
    File file = new File(path);
    ContentValues values = new ContentValues();
    values.put(MediaStore.Images.Media.TITLE, title);
    values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
    values.put(MediaStore.Images.Media.MIME_TYPE, "image/" + getMimeType(format));
    values.put(MediaStore.Images.Media.DATA, path);
    long time = currentTimeMillis / 1000;
    values.put(MediaStore.MediaColumns.DATE_ADDED, time);
    values.put(MediaStore.MediaColumns.DATE_MODIFIED, time);
    if (file.exists()) {
      values.put(MediaStore.Images.Media.SIZE, file.length());
    }

    ContentResolver resolver = context.getContentResolver();
    Uri uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    Logger.i("SaveUri = " + uri);
    return uri;
  }

  public static String getMimeType(Bitmap.CompressFormat format) {
    Logger.i("getMimeType CompressFormat = " + format);
    switch (format) {
      case JPEG:
        return "jpeg";
      case PNG:
        return "png";
    }
    return "png";
  }

  public static Uri createTempUri(Context context) {
    return Uri.fromFile(new File(context.getCacheDir(), "cropped"));
  }

  // Handle button event /////////////////////////////////////////////////////////////////////////

  /*protected final View.OnClickListener btnListener = new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
          case R.id.buttonDone:
            BasicFragmentPermissionsDispatcher.cropImageWithCheck(BasicFragment.this);
            break;
        case R.id.buttonPickImage:
          BasicFragmentPermissionsDispatcher.pickImageWithCheck(BasicFragment.this);
          break;
      }
    }
  };*/

  // Callbacks ///////////////////////////////////////////////////////////////////////////////////

  protected final LoadCallback mLoadCallback = new LoadCallback() {
    @Override
    public void onSuccess() {
    }

    @Override
    public void onError(Throwable e) {
    }
  };

  protected final CropCallback mCropCallback = new CropCallback() {
    @Override
    public void onSuccess(Bitmap cropped) {
      mCropView.save(cropped)
          .compressFormat(mCompressFormat)
          .execute(createSaveUri(), mSaveCallback);
    }

    @Override
    public void onError(Throwable e) {
    }
  };

  protected final SaveCallback mSaveCallback = new SaveCallback() {
    @Override
    public void onSuccess(Uri outputUri) {
//      dismissProgress();
      Intent intent = new Intent(getActivity(), DashboardActivity.class);
      intent.setData( outputUri);
      startActivity(intent);
    }

    @Override
    public void onError(Throwable e) {
//      dismissProgress();
    }
  };
}