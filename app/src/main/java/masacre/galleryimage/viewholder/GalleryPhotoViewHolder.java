package masacre.galleryimage.viewholder;

import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import masacre.galleryimage.R;
import masacre.galleryimage.interfaces.GalleryPhotoActions;
import masacre.galleryimage.interfaces.OnGalleryItemClick;
import masacre.galleryimage.model.GalleryAlbum;
import masacre.galleryimage.model.GalleryItem;
import masacre.galleryimage.model.GalleryPhoto;
import masacre.galleryimage.utils.ImageLoaderUtils;

public class GalleryPhotoViewHolder extends GalleryViewHolder implements View.OnClickListener, ImageLoadingListener, View.OnLongClickListener {
    private ImageView photoImageView;
    private ImageView selectedImageView;
    private GalleryPhoto galleryPhoto;
    private OnGalleryItemClick onGalleryItemClick;
    private GalleryPhotoActions galleryPhotoActions;
    private Bitmap bitmap;

    public GalleryPhotoViewHolder(final View itemView, final OnGalleryItemClick onGalleryItemClick, final GalleryPhotoActions galleryPhotoActions) {
        super(itemView);
        setupItemViewListeners(itemView);
        this.onGalleryItemClick = onGalleryItemClick;
        this.galleryPhotoActions = galleryPhotoActions;
        findViews(itemView);
    }

    private void findViews(View itemView) {
        photoImageView = (ImageView) itemView.findViewById(R.id.photo);
        selectedImageView = (ImageView) itemView.findViewById(R.id.selected_image);
    }

    private void setupItemViewListeners(View itemView) {
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }

    @Override
    public void show(GalleryItem galleryItem) {
        galleryPhoto = (GalleryPhoto) galleryItem;
        if (galleryPhotoActions.isPhotoSelected(galleryPhoto)) {
            selectedImageView.setVisibility(View.VISIBLE);
        }
        ImageLoaderUtils.displayImage(Uri.fromFile(galleryPhoto.getFile()).toString().replace("%20", " "), photoImageView, this);
    }

    @Override
    public void onClick(View view) {
        onGalleryItemClick.onPhotoClickListener(this, photoImageView, bitmap);
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
        onGalleryItemClick.onPhotoLongClickListener();
        final GalleryAlbum parent = galleryPhoto.getParent();
        if (parent.isPhotoSelected(galleryPhoto)) {
            parent.unselectPhoto(galleryPhoto);
            galleryPhotoActions.unselectPhoto(galleryPhoto);
            selectedImageView.setVisibility(View.GONE);
        } else {
            parent.selectPhoto(galleryPhoto);
            galleryPhotoActions.selectPhoto(galleryPhoto);
            selectedImageView.setVisibility(View.VISIBLE);
        }
        return true;
    }

    public void updateSelectedPhoto() {
        final GalleryAlbum parent = galleryPhoto.getParent();
        if (parent.isPhotoSelected(galleryPhoto)) {
            selectedImageView.setVisibility(View.GONE);
            galleryPhotoActions.selectPhoto(galleryPhoto);
            parent.unselectPhoto(galleryPhoto);
        } else {
            selectedImageView.setVisibility(View.VISIBLE);
            galleryPhotoActions.unselectPhoto(galleryPhoto);
            parent.selectPhoto(galleryPhoto);
        }
    }
}
