package com.munifahsan.youthspaceapp.Beasiswa.repo;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.munifahsan.youthspaceapp.Beasiswa.BeasiswaEvent;
import com.munifahsan.youthspaceapp.Beasiswa.BeasiswaModel;
import com.munifahsan.youthspaceapp.EventBus.EventBus;
import com.munifahsan.youthspaceapp.EventBus.GreenRobotEventBus;

import java.util.List;

public class DetailBeasiswaRepo implements DetailBeasiswaRepoInt {

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference beasiswaRef = firebaseFirestore.collection("BEASISWA");

    public DetailBeasiswaRepo() {
    }

    @Override
    public void getData(String beasiswaId) {
        beasiswaRef.document(beasiswaId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    BeasiswaModel model = documentSnapshot.toObject(BeasiswaModel.class);
                    postEvent(BeasiswaEvent.onSuccess,
                            null,
                            model.getnImageHeader(),
                            model.getnKategori(),
                            model.getnTitle(),
                            model.getnLokasi(),
                            model.getnDeadline(),
                            model.getnDesc1(),
                            model.getnDesc2(),
                            null,
                            model.getnKriteria(),
                            model.getnProsesAplikasi(),
                            model.getnFormLink());
                } else {
                    postEvent(BeasiswaEvent.onError, "Data tidak di temukan");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                postEvent(BeasiswaEvent.onError, e.getMessage());
            }
        });
    }

    public void postEvent(int type, String errorMessage, String imageHeaderUrl, String kategori, String title,
                          String lokasi, String deadline, String desc1, String desc2, String desc3,
                          String kriteria, String prosesAplikasi, String fromLink) {
        BeasiswaEvent events = new BeasiswaEvent();
        events.setEventType(type);

        if (errorMessage != null) {
            events.setError(errorMessage);
        }

        events.setImageHeader(imageHeaderUrl);
        events.setKategori(kategori);
        events.setTitle(title);
        events.setLokasi(lokasi);
        events.setDeadline(deadline);
        events.setDesc1(desc1);
        events.setDesc2(desc2);
        events.setKriteria(kriteria);
        events.setProsesAplikasi(prosesAplikasi);
        events.setFormLink(fromLink);

        EventBus eventBus = GreenRobotEventBus.getInstance();
        eventBus.post(events);
    }

    public void postEvent(int type, String errorMessage) {
        postEvent(type, errorMessage, null, null, null, null, null, null, null, null, null, null, null);
    }
}
