package com.app.kk.screenrecorder;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Random;

import static com.app.kk.screenrecorder.MainActivity.listString;

public class Adapter extends ArrayAdapter<Item> {
    
    List<Item> list;
    Context context;
    int br;
    File file;
    public Adapter(Context context, int br, List<Item> list) {
        super(context, br, list);
        this.context = context;
        this.br = br;
        this.list = list;
    }
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(br, null, false);
        TextView ab = view.findViewById(R.id.vidTitle);
        TextView ac = view.findViewById(R.id.vidDuration);
        TextView ad = view.findViewById(R.id.vidSize);
        CardView cardView = view.findViewById(R.id.cardView);
        //cardView.setCardBackgroundColor(getRandom());

        final ImageView menuImg = view.findViewById(R.id.itemMenu);
        menuImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PopupMenu popupMenu = new PopupMenu(getContext(),menuImg);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.item_delete:


                                bdd(position);

                                return true;

                            case R.id.item_share:

                                bss(position);

                                return true;

                        }

                        popupMenu.dismiss();

                        return true;
                    }
                });

                popupMenu.show();
            }
        });


        Item A = list.get(position);

        //adding values to the list Item

        AssetManager assetManager = context.getAssets();

        try {
            InputStream inputStream = assetManager.open(A.getaa());

            Drawable drawable = Drawable.createFromStream(inputStream,"");

            ImageView imageView = (ImageView) view.findViewById(R.id.itemIcon);

            imageView.setImageDrawable(drawable);

        } catch (IOException e) {
            e.printStackTrace();
        }


        ab.setText(A.getab());
        ac.setText(A.getac());
        ad.setText(A.getad());




        return view;
    }

    public int getRandom(){
        Random random = new Random();

        return Color.argb(255, random.nextInt(256), random.nextInt(256),  random.nextInt(256));

    }


    private void bss(final  int position) {
        File file = new File(Environment.getExternalStorageDirectory() + "/Screen Recording/"+ listString.get(position));
        Uri uri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("video/*");
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, listString.get(position));
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(Intent.createChooser(shareIntent, "Share with"));
    }

    private void bdd(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirm Delete ?");
        builder.setMessage("Are you sure you want delete this !");

        builder.setIcon(R.drawable.ic_delete_black_24dp);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                file = new File(Environment.getExternalStorageDirectory() + "/Screen Recording/"+ listString.get(position));
                file.delete();
                list.remove(position);
                notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}