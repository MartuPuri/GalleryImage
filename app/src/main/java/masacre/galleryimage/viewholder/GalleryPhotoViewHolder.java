package masacre.galleryimage.viewholder;

import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import masacre.galleryimage.R;
import masacre.galleryimage.activities.GalleryActivity;
import masacre.galleryimage.model.GalleryItem;
import masacre.galleryimage.model.GalleryPhoto;
import masacre.galleryimage.utils.ImageLoaderUtils;

public class GalleryPhotoViewHolder extends GalleryViewHolder implements View.OnClickListener{
    private final ImageView photoImageView;
    private final ImageView selectedImageView;
    private final GalleryActivity galleryActivity;
    private GalleryPhoto galleryPhoto;

    public GalleryPhotoViewHolder(View itemView, GalleryActivity galleryActivity) {
        super(itemView);
        this.galleryActivity = galleryActivity;
        itemView.setOnClickListener(this);
        photoImageView = (ImageView) itemView.findViewById(R.id.photo);
        selectedImageView = (ImageView) itemView.findViewById(R.id.selected_image);
    }

    @Override
    public void show(GalleryItem galleryItem) {
        galleryPhoto = (GalleryPhoto) galleryItem;
        if (galleryActivity.isPhotoSelected(galleryPhoto)) {
            selectedImageView.setVisibility(View.VISIBLE);
        }
        ImageLoaderUtils.displayImage(Uri.fromFile(galleryPhoto.getFile()).toString().replace("%20", " "), photoImageView);
    }

    @Override
    public void onClick(View view) {
        if (galleryActivity.isPhotoSelected(galleryPhoto)) {
            galleryActivity.removePhotoSelected(galleryPhoto);
            selectedImageView.setVisibility(View.GONE);
        } else {
            galleryActivity.addPhotoSelected(galleryPhoto);
            selectedImageView.setVisibility(View.VISIBLE);
        }
    }
}
