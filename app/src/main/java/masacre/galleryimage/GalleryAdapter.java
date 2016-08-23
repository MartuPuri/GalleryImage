package masacre.galleryimage;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import masacre.galleryimage.interfaces.GalleryPhotoActions;
import masacre.galleryimage.interfaces.OnGalleryItemClick;
import masacre.galleryimage.model.GalleryItem;
import masacre.galleryimage.viewholder.GalleryAlbumViewHolder;
import masacre.galleryimage.viewholder.GalleryPhotoViewHolder;
import masacre.galleryimage.viewholder.GalleryViewHolder;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryViewHolder> {
    private List<GalleryItem> galleryItems;
    private final OnGalleryItemClick onGalleryItemClick;
    private final GalleryPhotoActions galleryPhotoActions;

    public GalleryAdapter(List<GalleryItem> galleryItems, OnGalleryItemClick onGalleryItemClick, GalleryPhotoActions galleryPhotoActions) {
        this.galleryItems = new ArrayList<>();
        if (galleryItems != null) {
            this.galleryItems.addAll(galleryItems);
        }
        this.onGalleryItemClick = onGalleryItemClick;
        this.galleryPhotoActions = galleryPhotoActions;
    }

    @Override
    public GalleryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (GalleryType.PHOTO.ordinal() == viewType) {
            View view = inflater.inflate(R.layout.gallery_photo, parent, false);
            return new GalleryPhotoViewHolder(view, onGalleryItemClick, galleryPhotoActions);
        } else if (GalleryType.ALBUM.ordinal() == viewType) {
            View view = inflater.inflate(R.layout.gallery_album, parent, false);
            return new GalleryAlbumViewHolder(view, onGalleryItemClick);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        GalleryItem galleryItem = getItem(position);
        if (galleryItem == null) {
            return super.getItemViewType(position);
        }
        if (galleryItem.isAlbum()) {
            return GalleryType.ALBUM.ordinal();
        } else {
            return GalleryType.PHOTO.ordinal();
        }
    }

    @Override
    public void onBindViewHolder(GalleryViewHolder holder, int position) {
        GalleryItem galleryItem = getItem(position);
        if (galleryItem != null) {
            holder.show(galleryItem);
        }
    }

    private GalleryItem getItem(int position) {
        return galleryItems == null || galleryItems.size() <= position ? null : galleryItems.get(position);
    }

    @Override
    public int getItemCount() {
        return galleryItems == null ? 0 : galleryItems.size();
    }

    public void setGalleryItems(List<GalleryItem> galleryItems) {
        if (this.galleryItems == null) {
            this.galleryItems = new ArrayList<>();
        } else {
            this.galleryItems.clear();
        }
        this.galleryItems.addAll(galleryItems);
        notifyDataSetChanged();
    }

    public void addGalleryItem(GalleryItem galleryItem) {
        if (galleryItems == null) {
            galleryItems = new ArrayList<>();
        }
        galleryItems.add(galleryItem);
        notifyItemInserted(galleryItems.size() - 1);
    }
}
