package com.munifahsan.youthspaceapp.Beasiswa.view;

public interface DetailBeasiswaViewInt {
    void setImageHeader(String imageUrl);

    void setKategori(String kategori);

    void setTitle(String title);

    void setLokasi(String lokasi);

    void setDeadline(String deadline);

    void setParagraf1(String paragraf1);

    void setParagraf2(String paragraf2);

    void setKriteria(String kriteria);

    void setProsesAplikasi(String prosesAplikasi);

    void showShimmer();

    void hideShimmer();

    void setFormLink(String formLink);

    void showMessage(String msg);
}
