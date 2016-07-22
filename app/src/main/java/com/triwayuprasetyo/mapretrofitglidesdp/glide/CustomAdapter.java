package com.triwayuprasetyo.mapretrofitglidesdp.glide;

/**
 * Created by why on 7/20/16.
 */

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.triwayuprasetyo.mapretrofitglidesdp.GlideActivity;
import com.triwayuprasetyo.mapretrofitglidesdp.R;

public class CustomAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {
    private LayoutInflater inflater;
    private String[] daftarNama, daftarAlamat, daftarUrlFoto;
    private Context context;

    public CustomAdapter(GlideActivity glideActivity, String[] daftarNama, String[] daftarAlamat, String[] daftarUrlFoto) {
        this.daftarNama = daftarNama;
        this.daftarAlamat = daftarAlamat;
        this.daftarUrlFoto = daftarUrlFoto;
        context = glideActivity;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.layout_listview_glide, null);
        TextView textViewName = (TextView) convertView.findViewById(R.id.textView_nama_listView_glide);
        TextView textViewAlamat = (TextView) convertView.findViewById(R.id.textView_alamat_listView_glide);
        textViewName.setText(daftarNama[position]);
        textViewAlamat.setText(daftarAlamat[position]);
        ImageView myImageView = (ImageView) convertView.findViewById(R.id.imageView_listView_glide);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(context, daftarNama[position]+" : "+daftarAlamat[position], Toast.LENGTH_LONG).show();
            }
        });

        Log.i("URL FOTO",daftarUrlFoto[position]);
        Glide.with(context)
                .load(daftarUrlFoto[position].trim())
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .crossFade()
                .into(myImageView);
        return convertView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}