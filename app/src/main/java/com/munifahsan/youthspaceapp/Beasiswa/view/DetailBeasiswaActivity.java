package com.munifahsan.youthspaceapp.Beasiswa.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.munifahsan.youthspaceapp.Beasiswa.pres.DetailBeasiswaPres;
import com.munifahsan.youthspaceapp.Beasiswa.pres.DetailBeasiswaPresInt;
import com.munifahsan.youthspaceapp.R;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.BlurTransformation;

public class DetailBeasiswaActivity extends AppCompatActivity implements DetailBeasiswaViewInt {

    private DetailBeasiswaPresInt mDetailBeasiswaPres;

    @BindView(R.id.imageView_imageHeader_detailBeasiswa)
    ImageView mImageHeader;
    @BindView(R.id.textView_kategori_detailBeasiswa)
    TextView mKategori;
    @BindView(R.id.textView_title_detailBeasiswa)
    TextView mTitle;
    @BindView(R.id.textView_lokasi_detailBeasiswa)
    TextView mLokasi;
    @BindView(R.id.textView_deadline_detailBeasiswa)
    TextView mDeadline;
    @BindView(R.id.textView_descParagraf1_detailBeasiswa)
    TextView mParagraf1;
    @BindView(R.id.textView_descParagraf2_detailBeasiswa)
    TextView mParagraf2;
    @BindView(R.id.textView_kriteria_detailBeasiswa)
    TextView mKriteria;
    @BindView(R.id.textView_prosesApliksai_detailBeasiswa)
    TextView mProsesAplilkasi;
    @BindView(R.id.button_daftarSekarang_detailBeasiswa)
    Button mDaftarSekarang;
    @BindView(R.id.shimmer_detailBeasiswa)
    ShimmerFrameLayout mContentShimmer;
    @BindView(R.id.linearLay_content_detailBeasiswa)
    LinearLayout mContent;

    private String formLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_beasiswa);

        ButterKnife.bind(this);
        mDetailBeasiswaPres = new DetailBeasiswaPres(this);
        mDetailBeasiswaPres.onCreate();

        Intent intent = getIntent();
        String mBeasiswaId = intent.getStringExtra("BEASISWA_ID");
        //showMessage(mBeasiswaId);
        mDetailBeasiswaPres.getData(mBeasiswaId);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDetailBeasiswaPres.onDestroy();
    }

    @OnClick(R.id.button_daftarSekarang_detailBeasiswa)
    public void onDaftarSekarangClick(){
        Intent intent = new Intent(this, FormActivity.class);
        intent.putExtra("FORM_LINK", formLink);
        startActivity(intent);
    }

    @OnClick(R.id.floatingActionButton_back_detailBeasiswa)
    public void onBackButtonClick(){
        finish();
    }

    @Override
    public void setImageHeader(String imageUrl){
        Glide.with(this)
                .load(imageUrl)
                .transform(new BlurTransformation())
                .fitCenter()
                .into(mImageHeader);
    }

    @Override
    public void setKategori(String kategori){
        mKategori.setText(kategori);
    }

    @Override
    public void setTitle(String title){
        mTitle.setText(title);
    }

    @Override
    public void setLokasi(String lokasi){
        mLokasi.setText(lokasi);
    }

    @Override
    public void setDeadline(String deadline){
        mDeadline.setText(deadline);
    }

    @Override
    public void setParagraf1(String paragraf1){
        mParagraf1.setText(paragraf1);
    }

    @Override
    public void setParagraf2(String paragraf2){
        mParagraf2.setVisibility(View.VISIBLE);
        mParagraf2.setText(paragraf2);
    }

    @Override
    public void setKriteria(String kriteria){
        mKriteria.setText(kriteria);
    }

    @Override
    public void setProsesAplikasi(String prosesAplikasi){
        mProsesAplilkasi.setText(prosesAplikasi);
    }

    @Override
    public void showShimmer(){
        mContentShimmer.setVisibility(View.VISIBLE);
        mContent.setVisibility(View.INVISIBLE);
    }

    @Override
    public void hideShimmer(){
        mContentShimmer.setVisibility(View.INVISIBLE);
        mContent.setVisibility(View.VISIBLE);
    }

    @Override
    public void setFormLink(String formLink){
        //showMessage(formLink);
        this.formLink = formLink;
    }

    @Override
    public void showMessage(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}