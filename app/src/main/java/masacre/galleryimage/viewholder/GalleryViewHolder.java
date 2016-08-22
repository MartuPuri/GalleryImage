package masacre.galleryimage.viewholder;


import android.support.v7.widget.RecyclerView;
import android.view.View;

import masacre.galleryimage.model.GalleryItem;

public abstract class GalleryViewHolder extends RecyclerView.ViewHolder {
    public GalleryViewHolder(View itemView) {
        super(itemView);
    }

    public abstract void show(GalleryItem galleryItem);
}
