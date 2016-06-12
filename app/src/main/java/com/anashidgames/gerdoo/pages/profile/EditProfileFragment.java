package com.anashidgames.gerdoo.pages.profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.anashidgames.gerdoo.R;
import com.anashidgames.gerdoo.core.service.GerdooServer;
import com.anashidgames.gerdoo.core.service.model.EditProfileResponse;
import com.anashidgames.gerdoo.pages.auth.FormFragment;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by psycho on 6/12/16.
 */
public class EditProfileFragment extends FormFragment{

    public static Fragment newInstance() {
        return new EditProfileFragment();
    }

    private EditText firstNameEdit;
    private EditText lastNameEdit;

    public EditProfileFragment() {
        super(R.id.messageView, R.id.submitButton);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        firstNameEdit = (EditText) rootView.findViewById(R.id.firstNameEdit);
        lastNameEdit = (EditText) rootView.findViewById(R.id.lastNameEdit);

        rootView.findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
            }
        });

        return rootView;
    }

    private void close() {
        getActivity().finish();
    }

    @Override
    protected void callServer(int formId, Callback callback) {
        String firstName = firstNameEdit.getText().toString();
        String lastName = lastNameEdit.getText().toString();

        Call<EditProfileResponse> call = GerdooServer.INSTANCE.editProfile(firstName, lastName);
        call.enqueue(callback);
    }

    @Override
    protected void submitted(Object result) {
        close();
    }
}
