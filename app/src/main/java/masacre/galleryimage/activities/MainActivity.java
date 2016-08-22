package masacre.galleryimage.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import masacre.galleryimage.model.GalleryAlbum;
import masacre.galleryimage.model.GalleryItem;
import masacre.galleryimage.utils.GalleryUtils;

import static android.os.Environment.getExternalStoragePublicDirectory;

public class MainActivity extends AppCompatActivity {
    private static final int READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_ID = 5412;
    private static final String WHATSAPP_DIR = "WhatsApp/Media/WhatsApp Images";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_ID);
        } else {
            ArrayList<GalleryItem> galleryItems = retrieveGallery();
            startActivity(GalleryActivity.getIntent(this, galleryItems));
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_ID: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    ArrayList<GalleryItem> galleryItems = retrieveGallery();
                    startActivity(GalleryActivity.getIntent(this, galleryItems));
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private ArrayList<GalleryItem> retrieveGallery() {
        ArrayList<GalleryItem> galleryItems = new ArrayList<>();
        List<GalleryItem> dcim = GalleryUtils.retrieveFileFromDirectory(getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM));
        List<GalleryItem> pictures = GalleryUtils.retrieveFileFromDirectory(getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES));

        galleryItems.addAll(dcim);
        galleryItems.addAll(pictures);
        galleryItems.add(new GalleryAlbum(getExternalStoragePublicDirectory(WHATSAPP_DIR) ));

        return galleryItems;
    }
}
