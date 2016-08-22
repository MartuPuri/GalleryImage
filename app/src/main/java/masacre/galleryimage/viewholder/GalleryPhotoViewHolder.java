package masacre.galleryimage.viewholder;

import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import masacre.galleryimage.R;
import masacre.galleryimage.activities.GalleryActivity;
import masacre.galleryimage.interfaces.OnPhotoClickListener;
import masacre.galleryimage.model.GalleryItem;
import masacre.galleryimage.model.GalleryPhoto;
import masacre.galleryimage.utils.ImageLoaderUtils;

public class GalleryPhotoViewHolder extends GalleryViewHolder implements View.OnClickListener, ImageLoadingListener, View.OnLongClickListener {
    private final ImageView photoImageView;
    private final ImageView selectedImageView;
    private final GalleryActivity galleryActivity;
    private GalleryPhoto galleryPhoto;
    private OnPhotoClickListener onPhotoClickListener;
    private Bitmap bitmap;

    public GalleryPhotoViewHolder(View itemView, GalleryActivity galleryActivity, OnPhotoClickListener onPhotoClickListener) {
        super(itemView);
        this.galleryActivity = galleryActivity;
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
        this.onPhotoClickListener = onPhotoClickListener;
        photoImageView = (ImageView) itemView.findViewById(R.id.photo);
        selectedImageView = (ImageView) itemView.findViewById(R.id.selected_image);
    }

    @Override
    public void show(GalleryItem galleryItem) {
        galleryPhoto = (GalleryPhoto) galleryItem;
        if (galleryActivity.isPhotoSelected(galleryPhoto)) {
            selectedImageView.setVisibility(View.VISIBLE);
        }
        ImageLoaderUtils.displayImage(Uri.fromFile(galleryPhoto.getFile()).toString().replace("%20", " "), photoImageView, this);
    }

    @Override
    public void onClick(View view) {
        if (galleryActivity.isEditModeEnabled()) {
            if (galleryActivity.isPhotoSelected(galleryPhoto)) {
                galleryActivity.removePhotoSelected(galleryPhoto);
                selectedImageView.setVisibility(View.GONE);
                galleryPhoto.getParent().removePhotoSelected(galleryPhoto);
                if (!galleryPhoto.getParent().hasPhotoSelected()) {
                    galleryActivity.disableEditMode();
                }
            } else {
                galleryActivity.addPhotoSelected(galleryPhoto);
                selectedImageView.setVisibility(View.VISIBLE);
                galleryPhoto.getParent().addPhotoSelected(galleryPhoto);
            }
        } else {
            onPhotoClickListener.onPhotoClickListener(photoImageView, bitmap);
        }
    }


    @Override
    public void onLoadingStarted(String imageUri, View view) {
        
    }

    @Override
    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

    }

    @Override
    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
        this.bitmap = loadedImage;
    }

    @Override
    public void onLoadingCancelled(String imageUri, View view) {

    }

    @Override
    public boolean onLongClick(View view) {
        galleryActivity.enableEditMode();
        if (galleryActivity.isPhotoSelected(galleryPhoto)) {
            galleryActivity.removePhotoSelected(galleryPhoto);
            selectedImageView.setVisibility(View.GONE);
        } else {
            galleryActivity.addPhotoSelected(galleryPhoto);
            selectedImageView.setVisibility(View.VISIBLE);
        }
        return true;
    }
}
