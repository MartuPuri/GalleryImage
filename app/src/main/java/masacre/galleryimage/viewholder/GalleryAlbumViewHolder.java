package masacre.galleryimage.viewholder;

import android.view.View;
import android.widget.TextView;

import masacre.galleryimage.R;
import masacre.galleryimage.interfaces.OnAlbumClickListener;
import masacre.galleryimage.model.GalleryAlbum;
import masacre.galleryimage.model.GalleryItem;

public class GalleryAlbumViewHolder extends GalleryViewHolder implements View.OnClickListener{
    private final TextView name;
    private final OnAlbumClickListener albumClickListener;
    private GalleryAlbum galleryAlbum;

    public GalleryAlbumViewHolder(View itemView, OnAlbumClickListener albumClickListener) {
        super(itemView);
        itemView.setOnClickListener(this);
        this.albumClickListener = albumClickListener;
        this.name = (TextView) itemView.findViewById(R.id.album);
    }

    @Override
    public void show(GalleryItem galleryItem) {
        galleryAlbum = (GalleryAlbum) galleryItem;
        name.setText(galleryAlbum.getAlbumName());
    }

    @Override
    public void onClick(View view) {
        if (albumClickListener != null) {
            albumClickListener.onAlbumClick(view, galleryAlbum);
        }
    }
}
