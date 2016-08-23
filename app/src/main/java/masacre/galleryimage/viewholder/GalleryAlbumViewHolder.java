package masacre.galleryimage.viewholder;

import android.view.View;
import android.widget.TextView;

import masacre.galleryimage.R;
import masacre.galleryimage.interfaces.OnGalleryItemClick;
import masacre.galleryimage.model.GalleryAlbum;
import masacre.galleryimage.model.GalleryItem;

public class GalleryAlbumViewHolder extends GalleryViewHolder implements View.OnClickListener{
    private final TextView name;
    private final OnGalleryItemClick onGalleryItemClick;
    private GalleryAlbum galleryAlbum;

    public GalleryAlbumViewHolder(View itemView, OnGalleryItemClick onGalleryItemClick) {
        super(itemView);
        itemView.setOnClickListener(this);
        this.onGalleryItemClick = onGalleryItemClick;
        this.name = (TextView) itemView.findViewById(R.id.album);
    }

    @Override
    public void show(GalleryItem galleryItem) {
        galleryAlbum = (GalleryAlbum) galleryItem;
        name.setText(galleryAlbum.getAlbumName());
    }

    @Override
    public void onClick(View view) {
        if (onGalleryItemClick != null) {
            onGalleryItemClick.onAlbumClick(view, galleryAlbum);
        }
    }
}
