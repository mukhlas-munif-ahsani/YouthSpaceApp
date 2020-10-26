package com.munifahsan.youthspaceapp.Beasiswa.pres;

import com.munifahsan.youthspaceapp.Beasiswa.BeasiswaEvent;
import com.munifahsan.youthspaceapp.Beasiswa.repo.DetailBeasiswaRepo;
import com.munifahsan.youthspaceapp.Beasiswa.repo.DetailBeasiswaRepoInt;
import com.munifahsan.youthspaceapp.Beasiswa.view.DetailBeasiswaViewInt;
import com.munifahsan.youthspaceapp.EventBus.EventBus;
import com.munifahsan.youthspaceapp.EventBus.GreenRobotEventBus;

import org.greenrobot.eventbus.Subscribe;

import static com.munifahsan.youthspaceapp.Beasiswa.BeasiswaEvent.onError;
import static com.munifahsan.youthspaceapp.Beasiswa.BeasiswaEvent.onSuccess;

public class DetailBeasiswaPres implements DetailBeasiswaPresInt {
    private DetailBeasiswaRepoInt mDetailBeasiswaRepo;
    private DetailBeasiswaViewInt mDetailBeasiswaView;
    private EventBus mEventBus;

    public DetailBeasiswaPres(DetailBeasiswaViewInt mDetailBeasiswaView) {
        this.mDetailBeasiswaView = mDetailBeasiswaView;
        mDetailBeasiswaRepo = new DetailBeasiswaRepo();
        mEventBus = GreenRobotEventBus.getInstance();
    }

    @Subscribe
    public void onEventMainThread(BeasiswaEvent event) {
        switch (event.getEventType()){
            case onSuccess:
                onSuccess(event.getImageHeader(), event.getKategori(), event.getTitle(), event.getLokasi(),
                        event.getDeadline(), event.getDesc1(), event.getDesc2(), event.getDesc3(), event.getKriteria(),
                        event.getProsesAplikasi(), event.getFormLink());
                break;
            case onError:
                onError();
        }
    }

    @Override
    public void onCreate(){
        mEventBus.register(this);
    }

    @Override
    public void onDestroy(){
        mDetailBeasiswaView = null;
        mEventBus.unregister(this);
    }

    @Override
    public void onError() {

    }

    @Override
    public void getData(String id){
        mDetailBeasiswaRepo.getData(id);
    }

    private void onSuccess(String imageHeaderUrl, String kategori, String title,
                           String lokasi, String deadline, String desc1, String desc2, String desc3,
                           String kriteria, String prosesAplikasi, String fromLink) {

        mDetailBeasiswaView.hideShimmer();

        mDetailBeasiswaView.setImageHeader(imageHeaderUrl);
        mDetailBeasiswaView.setKategori(kategori);
        mDetailBeasiswaView.setTitle(title);
        mDetailBeasiswaView.setLokasi(lokasi);
        mDetailBeasiswaView.setDeadline(deadline);
        mDetailBeasiswaView.setParagraf1(desc1);
        mDetailBeasiswaView.setParagraf2(desc2);
        mDetailBeasiswaView.setKriteria(kriteria);
        mDetailBeasiswaView.setProsesAplikasi(prosesAplikasi);
        mDetailBeasiswaView.setFormLink(fromLink);
    }
}
