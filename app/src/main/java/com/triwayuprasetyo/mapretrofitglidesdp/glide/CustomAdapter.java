package com.triwayuprasetyo.mapretrofitglidesdp.glide;

/**
 * Created by why on 7/20/16.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.triwayuprasetyo.mapretrofitglidesdp.GlideActivity;
import com.triwayuprasetyo.mapretrofitglidesdp.R;

public class CustomAdapter extends BaseAdapter {
    private static LayoutInflater inflater = null;
    String[] daftarNama;
    String[] daftarAlamat;
    String[] daftarUrlFoto;
    Context context;
    int[] imageId;

    public CustomAdapter(GlideActivity glideActivity, String[] daftarNama, String[] daftarAlamat, int[] prgmImages, String[] daftarUrlFoto) {
        // TODO Auto-generated constructor stub
        this.daftarNama = daftarNama;
        this.daftarAlamat = daftarAlamat;
        this.daftarUrlFoto = daftarUrlFoto;
        context = glideActivity;
        imageId = prgmImages;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return daftarNama.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View recycled, ViewGroup parent) {
        // TODO Auto-generated method stub
//        Holder holder = new Holder();
//        View rowView;
//        rowView = inflater.inflate(R.layout.layout_listview_glide, null);
//        holder.textViewName = (TextView) rowView.findViewById(R.id.textView_nama_listView_glide);
//        holder.textViewAlamat = (TextView) rowView.findViewById(R.id.textView_alamat_listView_glide);
//        //holder.img = (ImageView) rowView.findViewById(R.id.imageView_listView_glide);
//        holder.textViewName.setText(daftarNama[position]);
//        holder.textViewAlamat.setText(daftarAlamat[position]);
//        //holder.img.setImageResource(imageId[position]);
//        rowView.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                Toast.makeText(context, "You Clicked " + daftarNama[position], Toast.LENGTH_LONG).show();
//            }
//        });

        View rowView;
        rowView = inflater.inflate(R.layout.layout_listview_glide, null);
        final ImageView myImageView;
        myImageView = (ImageView) rowView.findViewById(R.id.imageView_listView_glide);

        String url = daftarUrlFoto[position];

        Glide.with(context)
                .load(url)
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .crossFade()
                .into(myImageView);

        return rowView;
    }

    public class Holder {
        TextView textViewName;
        TextView textViewAlamat;
        ImageView img;
    }

}