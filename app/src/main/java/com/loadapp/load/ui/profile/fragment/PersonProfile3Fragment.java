package com.loadapp.load.ui.profile.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.loadapp.load.BuildConfig;
import com.loadapp.load.R;
import com.loadapp.load.api.Api;
import com.loadapp.load.bean.PhaseBean;
import com.loadapp.load.global.Constant;
import com.loadapp.load.ui.profile.CommitProfileActivity;
import com.loadapp.load.ui.profile.widget.EditTextContainer;
import com.loadapp.load.ui.profile.widget.GenderCheckBox;
import com.loadapp.load.ui.profile.widget.SelectContainer;
import com.loadapp.load.util.BuildRequestJsonUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PersonProfile3Fragment extends BaseCommitFragment {
    private static final String TAG = "PersonProfile3Fragment";

    private FrameLayout flLeft, flRight;
    private EditTextContainer etName, etFatherName, etCnicNum;
    private SelectContainer selectBirth;
    private FrameLayout flCommit;
    private GenderCheckBox genderCheckBox;
    private String mBirthDate;
    private String mLeftPath1, mRightPath2, mRecognitionPath;

    private static final int REQUEST_CAMERA_LEFT = 111;
    private static final int REQUEST_CAMERA_RIGHT = 112;
    private static final int REQUEST_CAMERA_RECOGNITION = 113;
    private ImageView ivLeft, ivRight ,ivRecognition;

    private String mCurPath;
    private FrameLayout flRecognition;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_person_profile3, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        flLeft = view.findViewById(R.id.fl_person_profile3_left_cnic);
        flRight= view.findViewById(R.id.fl_person_profile3_right_cnic);
        ivLeft = view.findViewById(R.id.iv_person_profile3_left_cnic);
        ivRight= view.findViewById(R.id.iv_person_profile3_right_cnic);
        ivRecognition= view.findViewById(R.id.iv_person_profile3_recognition);

        etName = view.findViewById(R.id.edittext_container_profile3_name);
        etFatherName = view.findViewById(R.id.edittext_container_profile3_father_name);
        genderCheckBox = view.findViewById(R.id.gender_container_profile3_gender);
        etCnicNum = view.findViewById(R.id.edittext_container_profile3_cnic_num);
        selectBirth = view.findViewById(R.id.select_container_profile3_birth);

        flRecognition = view.findViewById(R.id.fl_person_profile3_recognition);

        flCommit = view.findViewById(R.id.fl_person_profile3_commit);
        initializeListener();
    }

    private void initializeListener(){
        selectBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePicker(new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        String datef = sdf.format(date);
                        mBirthDate = datef;
                        selectBirth.setData(mBirthDate);
                    }
                });
            }
        });
        flLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPermission(new CallBack() {
                    @Override
                    public void success() {
                        startCamera(REQUEST_CAMERA_LEFT, false);
                    }
                });
            }
        });
        flRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPermission(new CallBack() {
                    @Override
                    public void success() {
                        startCamera(REQUEST_CAMERA_RIGHT, false);
                    }
                });
            }
        });
        flRecognition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPermission(new CallBack() {
                    @Override
                    public void success() {
                        startCamera(REQUEST_CAMERA_RECOGNITION, true);
                    }
                });
            }
        });
        flCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkCommitAvailable()){
                    uploadContact();
                }
            }
        });
    }

    private void requestPermission(CallBack callBack) {
        boolean hasReadContactPermission = PermissionUtils.isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA);
        if (!hasReadContactPermission) {
            PermissionUtils.permission(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA).callback(new PermissionUtils.SimpleCallback() {
                @Override
                public void onGranted() {

                }

                @Override
                public void onDenied() {

                }
            }).request();
        } else {
            if (callBack != null){
                callBack.success();
            }
        }
    }

    private void startCamera(int requestCode, boolean isFront){
        String path = getActivity().getFilesDir().getPath()
                + "/photo/" + System.currentTimeMillis() + ".jpg";
        File file = new File(path);
        FileUtils.createOrExistsFile(path);
        if (!file.exists()) {
            ToastUtils.showShort("can not create file");
            return;
        }
        Uri imageFileUri;
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);//跳转到相机Activity
        intent.putExtra("camerasensortype", isFront ? 2 : 1); // 调用前置摄像头
        intent.putExtra("autofocus", true); // 自动对焦

        mCurPath = path;
        //判断是否是AndroidN以及更高的版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            imageFileUri = FileProvider.getUriForFile(getContext(), BuildConfig.APPLICATION_ID + ".fileProvider", file);
            intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageFileUri);//告诉相机拍摄完毕输出图片到指定的Uri
        } else {
            imageFileUri = Uri.fromFile(file);//获取文件的Uri;
            intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageFileUri);//告诉相机拍摄完毕输出图片到指定的Uri
        }
        getActivity().startActivityForResult(intent, requestCode);
    }

    private boolean checkCommitAvailable(){
        if (etName == null || etName.isEmptyText()) {
            ToastUtils.showShort(" name is null");
            return false;
        }
        if (etFatherName == null || etFatherName.isEmptyText()) {
            ToastUtils.showShort("father name is null");
            return false;
        }
        if (genderCheckBox == null){
            ToastUtils.showShort("unChoice gender");
            return false;
        }

        if (etCnicNum == null || etCnicNum.isEmptyText()) {
            ToastUtils.showShort("CNIC name is null");
            return false;
        }
        if (selectBirth == null || mBirthDate == null) {
            ToastUtils.showShort("birthday is null");
            return false;
        }
        if (mLeftPath1 == null || !FileUtils.isFileExists(mLeftPath1)) {
            ToastUtils.showShort("CNIC is null");
            return false;
        }
        if (mRightPath2 == null || !FileUtils.isFileExists(mRightPath2)) {
            ToastUtils.showShort("CNIC is null");
            return false;
        }
        if (mRecognitionPath == null || !FileUtils.isFileExists(mRecognitionPath)) {
            ToastUtils.showShort("recognition is null");
            return false;
        }
        if(!FileUtils.isFileExists(mLeftPath1)){
            ToastUtils.showShort("file 1 is null");
            return false;
        }
        if(!FileUtils.isFileExists(mRightPath2)){
            ToastUtils.showShort("file 2 is null");
            return false;
        }
        if(!FileUtils.isFileExists(mRecognitionPath)){
            ToastUtils.showShort("recognition file is null");
            return false;
        }
        return true;
    }

    private void uploadContact() {
        JSONObject jsonObject = BuildRequestJsonUtil.buildRequestJson();
        try {
            jsonObject.put("account_id", Constant.mAccountId);
            jsonObject.put("name", etName.getText().trim());
            jsonObject.put("father_name", etFatherName.getText().trim());
            //身份证号
            jsonObject.put("identity", etCnicNum.getText().trim());
            jsonObject.put("gender", genderCheckBox.getCurPos() + "");
            jsonObject.put("birthday", mBirthDate);
            //证件 正面照片
//            jsonObject.put("identity_photo_front", mLeftPath1);
//            //证件 背面照片
//            jsonObject.put("identity_photo_back", mRightPath2);
//            //自拍照
//            jsonObject.put("photo_self", mRecognitionPath);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkGo.<String>post(Api.UPLOAD_IDENTITY).tag(TAG)
                .params("data", jsonObject.toString())
                .params("identity_photo_front", new File(mLeftPath1))
                .params("identity_photo_back", new File(mRightPath2))
                .params("photo_self", new File(mRecognitionPath))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        PhaseBean phaseBean = checkResponseSuccess(response, PhaseBean.class);
                        if (phaseBean == null) {
                            Log.e(TAG, " upload contact error ." + response.body());
                            return;
                        }
                        if (getActivity() instanceof CommitProfileActivity) {
                            ((CommitProfileActivity) getActivity()).switchFragment(3);
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Log.e(TAG, "upload contact failure = " + response.body());
                    }
                });
    }

    protected void showTimePicker(OnTimeSelectListener listener) {
        if (KeyboardUtils.isSoftInputVisible(getActivity())) {
            KeyboardUtils.hideSoftInput(getActivity());
        }
        //时间选择器
        TimePickerView pvTime = new TimePickerBuilder(getContext(), listener).setSubmitText("ok")
                .setCancelText("cancel").build();
        pvTime.show();
    }

    public void OnActivityResultInternal(int requestCode, Intent data){
        switch (requestCode){
            case REQUEST_CAMERA_LEFT:
                mLeftPath1 = mCurPath;
                Glide.with(ivLeft.getContext()).load(mLeftPath1).into(ivLeft);
                break;
            case REQUEST_CAMERA_RIGHT:
                mRightPath2 = mCurPath;
                Glide.with(ivLeft.getContext()).load(mRightPath2).into(ivRight);
                break;
            case REQUEST_CAMERA_RECOGNITION:
                mRecognitionPath = mCurPath;
                Glide.with(ivRecognition.getContext()).load(mRecognitionPath).into(ivRecognition);
                break;
        }
    }

    public interface CallBack{
        void success();
    }
}
