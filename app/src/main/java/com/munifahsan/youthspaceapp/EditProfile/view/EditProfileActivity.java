package com.munifahsan.youthspaceapp.EditProfile.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.munifahsan.youthspaceapp.EditProfile.pres.EditProfilePres;
import com.munifahsan.youthspaceapp.EditProfile.pres.EditProfilePresInt;
import com.munifahsan.youthspaceapp.R;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity implements EditProfileViewInt {

    private static final int PICK_IMAGE_REQUEST = 1;

    private Uri mImageUri;

    @BindView(R.id.progress_bar_editProfile)
    ProgressBar mProgress;
    @BindView(R.id.profile_image_editProfile)
    CircleImageView mProfileImage;
    @BindView(R.id.editText_email_editProfile)
    TextInputEditText mEmail;
    @BindView(R.id.editText_namaLengkap_editProfile)
    TextInputEditText mNama;
    @BindView(R.id.editText_noTlp_editProfile)
    TextInputEditText mNoTlp;
    //@BindView(R.id.editText_gender_editProfile)
    AutoCompleteTextView mGender;
    @BindView(R.id.button_save_editProfile)
    Button mSaveBtn;

    @BindView(R.id.textInput_email_editProfile)
    TextInputLayout mEmailLayout;
    @BindView(R.id.textInput_namaLengkap_editProfile)
    TextInputLayout mNamaLayout;
    @BindView(R.id.textInput_noTlp_editProfile)
    TextInputLayout mNoTlpLayout;
    @BindView(R.id.textInput_gender_editProfile)
    TextInputLayout mGenderLayout;
    @BindView(R.id.cardView_textProfileImage_editProfile)
    CardView mCardTextPhoto;
    @BindView(R.id.textView_textProfileImage_editProfile)
    TextView mTextPhoto;

    @BindView(R.id.loading_profileImage_editProfile)
    CircleImageView mLoadingProfileImage;
    @BindView(R.id.progress_profileImage_editProfile)
    ProgressBar mProgressProfileImage;
    @BindView(R.id.progressBar_email_editProfile)
    ProgressBar mProgressEmail;
    @BindView(R.id.progressBar_nama_editProfile)
    ProgressBar mProgressNama;
    @BindView(R.id.progressBar_noTlp_editProfile)
    ProgressBar mProgressNoTlp;
    @BindView(R.id.progressBar_gender_editProfile)
    ProgressBar mProgressGender;

    private EditProfilePresInt mEditProfilePresInt;

    private StorageTask mUploadTask;

    private FirebaseAuth mAuth;
    private String mCurrent_id;
    StorageReference reference = FirebaseStorage.getInstance().getReference("photoProfileUploads");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        ButterKnife.bind(this);
        mEditProfilePresInt = new EditProfilePres(this);
        mEditProfilePresInt.onCreate();

        mAuth = FirebaseAuth.getInstance();
        mCurrent_id = mAuth.getCurrentUser().getUid();

        mEditProfilePresInt.getData();

        String[] genderItems = new String[]{
                "LAKI-LAKI",
                "PEREMPUAN"
        };
        String[] items = new String[]{
                "laki",
                "pr"
        };

        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(
                EditProfileActivity.this, R.layout.dropdown_gender, genderItems
        );

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.dropdown_gender, items);

        mGender = findViewById(R.id.editText_gender_editProfile);

        mGender.setThreshold(1);
        mGender.setAdapter(genderAdapter);
//        mGender.showDropDown();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mEditProfilePresInt.onDestroy();
    }

    @OnClick(R.id.profile_image_editProfile)
    public void onProfileImageClick() {
        openFileChooser();
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();
            hideTextPhoto();
            Glide.with(this).load(mImageUri).into(mProfileImage);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFileAndUpadateProfileData() {
        if (mImageUri != null) {
            setInputEnabled(false);
            showShimmer();
            mProgress.setVisibility(View.VISIBLE);

            StorageReference fileReference = FirebaseStorage.getInstance()
                    .getReference("photoProfileUploads")
                    .child(mCurrent_id + "." + getFileExtension(mImageUri));

            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            mProgress.setVisibility(View.INVISIBLE);
                            showMessage("berhasil upload");

                            getImageUrl();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            showMessage(e.getMessage());
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                            mProgress.setProgress((int) progress);
                        }
                    });

        } else {
            //showMessage("no file selected");
            showShimmer();
            setInputEnabled(false);
            updateProfileData(null);
        }
    }

    public void getImageUrl() {
        reference.child(mCurrent_id + ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.d("IMAGE_URL", uri.toString());
                updateProfileData(uri.toString());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("IMAGE_URL_error", e.getMessage());
            }
        });
    }

    public void updateProfileData(String imageUrl) {
        mEditProfilePresInt.updateProfileData(imageUrl,
                mEmail.getText().toString(),
                mNama.getText().toString(),
                mNoTlp.getText().toString(),
                mGender.getText().toString());
    }

    @OnClick(R.id.button_save_editProfile)
    public void onButtonSaveOnClick() {
        //showMessage("bisa");

        uploadFileAndUpadateProfileData();
    }


//    @OnClick(R.id.containerlayEmail)
//    @Override
//    public void onEmailOnClick(boolean showMessage) {
//        if (!showMessage){
//            showMessage("Email tidak dapat diubah");
//        }
//
//    }

    @Override
    public void showTextPhoto() {
        mCardTextPhoto.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideTextPhoto() {
        mCardTextPhoto.setVisibility(View.INVISIBLE);
    }

    @Override
    public void setTextPhoto(String msg) {
        mTextPhoto.setText(msg);
    }

    @Override
    public void setProfileImage(String url) {
        Glide.with(this).load(url).into(mProfileImage);
    }

    @Override
    public void setEmail(String email) {
        mEmail.setText(email);
    }

    @Override
    public void setNama(String nama) {
        mNama.setText(nama);
    }

    @Override
    public void setNoTlp(String noTlp) {
        mNoTlp.setText(noTlp);
    }

    @Override
    public void setGender(String gender) {
        mGender.setText(gender);
    }

    @Override
    public void setEmailError(String msg) {
        mEmailLayout.setError(msg);
    }

    @Override
    public void setNamaError(String msg) {
        mNamaLayout.setError(msg);
    }

    @Override
    public void setNoTlpError(String msg) {
        mNoTlpLayout.setError(msg);
    }

    @Override
    public void setGenderError(String msg) {
        mGenderLayout.setError(msg);
    }

    @Override
    public void setEmailEnabled(boolean enabled) {
        mEmail.setEnabled(enabled);
    }

    @Override
    public void setInputEnabled(boolean enabled) {
        mEmail.setEnabled(enabled);
        mNama.setEnabled(enabled);
        mNoTlp.setEnabled(enabled);
        mProfileImage.setEnabled(enabled);
        mGender.setEnabled(enabled);
        mSaveBtn.setEnabled(enabled);
    }

    @Override
    public void showShimmer() {
        mLoadingProfileImage.setVisibility(View.VISIBLE);
        mProgressProfileImage.setVisibility(View.VISIBLE);
        mProgressEmail.setVisibility(View.VISIBLE);
        mProgressNama.setVisibility(View.VISIBLE);
        mProgressNoTlp.setVisibility(View.VISIBLE);
        mProgressGender.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideShimmer() {
        mLoadingProfileImage.setVisibility(View.INVISIBLE);
        mProgressProfileImage.setVisibility(View.INVISIBLE);
        mProgressEmail.setVisibility(View.INVISIBLE);
        mProgressNama.setVisibility(View.INVISIBLE);
        mProgressNoTlp.setVisibility(View.INVISIBLE);
        mProgressGender.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}