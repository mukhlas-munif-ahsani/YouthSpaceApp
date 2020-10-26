package com.munifahsan.youthspaceapp.Account.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.munifahsan.youthspaceapp.Account.pres.AccountPagePres;
import com.munifahsan.youthspaceapp.Account.pres.AccountPagePresInt;
import com.munifahsan.youthspaceapp.EditProfile.view.EditProfileActivity;
import com.munifahsan.youthspaceapp.Login.view.LoginActivity;
import com.munifahsan.youthspaceapp.R;

import java.text.DecimalFormat;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class AccountPageFragment extends Fragment implements AccountViewInt {
    // TODO: Rename parameter arguments, choose names that match
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    @BindView(R.id.profile_image)
    CircleImageView mProfileImage;
    @BindView(R.id.textView_nama_account)
    TextView mNama;
    @BindView(R.id.textView_email_account)
    TextView mEmail;
    @BindView(R.id.textView_noTlp_account)
    TextView mNoTlp;
    @BindView(R.id.textView_gender_account)
    TextView mGender;
    @BindView(R.id.cardView_textProfileImage_account)
    CardView mCardTextPhoto;
    @BindView(R.id.textView_textProfileImage_account)
    TextView mTextPhoto;

    @BindView(R.id.shimmer_profile_image)
    ShimmerFrameLayout mShimmerProfileImage;
    @BindView(R.id.shimmer_namaEmail)
    ShimmerFrameLayout mShimmerNamaEmail;
    @BindView(R.id.shimmer_noTlp)
    ShimmerFrameLayout mShimmerNotlp;
    @BindView(R.id.shimmer_gender)
    ShimmerFrameLayout mShimmerGender;

    private FirebaseAuth mAuth;
    private AccountPagePresInt mAccountPagePresInt;

    public AccountPageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        ButterKnife.bind(this, view);
        mAccountPagePresInt = new AccountPagePres(this);
        mAccountPagePresInt.onCreate();

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mCurrentUser = mAuth.getCurrentUser();

//        showMessage(mCurrentUser.getDisplayName());
//
//        setNama(Objects.requireNonNull(mCurrentUser.getDisplayName()));
//        setEmail(Objects.requireNonNull(mCurrentUser.getEmail()));
//       // setNoTlp(Objects.requireNonNull(mCurrentUser.getPhoneNumber()));
//        setGender("");

        getProfileData();

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAccountPagePresInt.onDestroy();
    }

    @OnClick(R.id.outlinedButton_ubahProfile_account)
    public void navigateToEditProfile() {
        Intent intent = new Intent(getActivity(), EditProfileActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.button_logout_account)
    public void onLogoutButtonClick() {
        showDialogOnLogOutBtnOnClick();
    }

    public void showDialogOnLogOutBtnOnClick() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

        // set title dialog
        alertDialogBuilder.setTitle("Apakah anda yakin ingin Logout dari aplikasi ?");

        // set pesan dari dialog
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // jika tombol diklik, maka akan menutup activity ini
                        mAuth.signOut();
                        navigateToLogin();
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // jika tombol ini diklik, akan menutup dialog
                        // dan tidak terjadi apa2
                        dialog.cancel();
                    }
                });

        // membuat alert dialog dari builder
        AlertDialog alertDialog = alertDialogBuilder.create();

        // menampilkan alert dialog
        alertDialog.show();

    }

    public void getProfileData() {
        mAccountPagePresInt.getProfileData();
    }

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
    public void setNama(String nama) {
        mNama.setText(nama);
    }

    @Override
    public void setEmail(String email) {
        mEmail.setText(email);
    }

    @Override
    public void setNoTlp(String noTlp) {
        DecimalFormat decimalFormat = new DecimalFormat("####,####,####");

        mNoTlp.setText(noTlp);
    }

    @Override
    public void setGender(String gender) {
        mGender.setText(gender);
    }

    private void navigateToLogin() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void showShimmer() {
        mShimmerProfileImage.setVisibility(View.VISIBLE);
        mShimmerNamaEmail.setVisibility(View.VISIBLE);
        mShimmerNotlp.setVisibility(View.VISIBLE);
        mShimmerGender.setVisibility(View.VISIBLE);

        mProfileImage.setVisibility(View.INVISIBLE);
        mEmail.setVisibility(View.INVISIBLE);
        mNama.setVisibility(View.INVISIBLE);
        mNoTlp.setVisibility(View.INVISIBLE);
        mGender.setVisibility(View.INVISIBLE);
    }

    @Override
    public void hideShimmer() {
        mShimmerProfileImage.setVisibility(View.INVISIBLE);
        mShimmerNamaEmail.setVisibility(View.INVISIBLE);
        mShimmerNotlp.setVisibility(View.INVISIBLE);
        mShimmerGender.setVisibility(View.INVISIBLE);

        mProfileImage.setVisibility(View.VISIBLE);
        mEmail.setVisibility(View.VISIBLE);
        mNama.setVisibility(View.VISIBLE);
        mNoTlp.setVisibility(View.VISIBLE);
        mGender.setVisibility(View.VISIBLE);
    }

    @Override
    public void showMessage(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }
}