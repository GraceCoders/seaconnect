package com.sea.seaconnect.controller.Utills;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.sea.seaconnect.R;
import com.sea.seaconnect.controller.utills.Constants;
import com.sea.seaconnect.model.ResponseModel.HomeApiRespone.UserDetail;
import com.sea.seaconnect.view.activity.ProfileDetailActivity;

import java.text.DecimalFormat;

import de.hdodenhof.circleimageview.CircleImageView;

public class BottomSheetDialog extends BottomSheetDialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
            ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet,
                container, false);

        LinearLayout ll_view_Profile = v.findViewById(R.id.ll_view_profile);
        TextView tvName = v.findViewById(R.id.tvName);
        TextView rating = v.findViewById(R.id.rating);
        CircleImageView cvUserImage = v.findViewById(R.id.cvUserImage);
        // Get data from argument
        UserDetail userData = (UserDetail) requireArguments().getParcelable("UserData");
        rating.setText(new DecimalFormat("##.#").format(userData.getAvg_rating()));

        tvName.setText(userData.getFirstName() + getString(R.string.say_hi));
        Glide.with(requireContext()).load(Constants.PROFILE_URL_API + userData.getProfileImage()).into(cvUserImage);

        ll_view_Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), ProfileDetailActivity.class);
                intent.putExtra(Constants.USERID, userData.getId());
                startActivity(intent);
                dismiss();
            }
        });
        return v;
    }
}
