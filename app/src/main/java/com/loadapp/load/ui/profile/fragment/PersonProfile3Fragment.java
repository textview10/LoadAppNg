package com.loadapp.load.ui.profile.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
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
import com.blankj.utilcode.util.ThreadUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.loadapp.load.BuildConfig;
import com.loadapp.load.R;
import com.loadapp.load.api.Api;
import com.loadapp.load.bean.AccountProfileBean;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import top.zibin.luban.CompressionPredicate;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class PersonProfile3Fragment extends BaseCommitFragment {
    private static final String TAG = "PersonProfile3Fragment";

    private FrameLayout flLeft, flRight;
    private EditTextContainer etName, etFatherName, etCnicNum;
    private SelectContainer selectBirth;
    private FrameLayout flCommit;
    private GenderCheckBox genderCheckBox;

    private String mBirthDate;
    private String mLeftPath1, mRightPath2, mRecognitionPath;
    private String nameStr;
    private String fatherNameStr;
    private String identityStr;
    private int genderPos = -1;

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
        if (getActivity() instanceof CommitProfileActivity) {
            ((CommitProfileActivity) getActivity()).setTitle(R.string.loan_person_profile_title);
        }
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
        revertData();
    }

    private void revertData(){
        if (selectBirth != null && !TextUtils.isEmpty(mBirthDate)){
            selectBirth.setData(mBirthDate);
        }
        if (etName != null && !TextUtils.isEmpty(nameStr)){
            etName.setEditTextAndSelection(nameStr);
        }
        if (etFatherName != null && !TextUtils.isEmpty(fatherNameStr)){
            etFatherName.setEditTextAndSelection(fatherNameStr);
        }
        if (etCnicNum != null && !TextUtils.isEmpty(identityStr)){
            etCnicNum.setEditTextAndSelection(identityStr);
        }
        if (genderCheckBox != null && genderPos != -1){
            genderCheckBox.setPos(genderPos);
        }
        if (etCnicNum != null && !TextUtils.isEmpty(identityStr)){
            etCnicNum.setEditTextAndSelection(identityStr);
        }
        if (ivLeft != null && !TextUtils.isEmpty(mLeftPath1)){
            Glide.with(ivLeft).load(mLeftPath1).into(ivLeft);
        }
        if (ivRight != null && !TextUtils.isEmpty(mRightPath2)){
            Glide.with(ivRight).load(mRightPath2).into(ivRight);
        }
        if (ivRecognition != null && !TextUtils.isEmpty(mRecognitionPath)) {
            Glide.with(ivRecognition).load(mRecognitionPath).into(ivRecognition);
        }
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
                    if (callBack != null){
                        callBack.success();
                    }
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
        nameStr = etName.getText().trim();
        fatherNameStr = etFatherName.getText().trim();
        identityStr = etCnicNum.getText().trim();
        genderPos = genderCheckBox.getCurPos();
        JSONObject jsonObject = BuildRequestJsonUtil.buildRequestJson();
        try {
            jsonObject.put("account_id", Constant.mAccountId);
            jsonObject.put("name", etName.getText().trim());
            jsonObject.put("father_name", etFatherName.getText().trim());
            //身份证号
            jsonObject.put("identity", etCnicNum.getText().trim());
            jsonObject.put("gender", genderCheckBox.getCurPos() + "");
            jsonObject.put("birthday", mBirthDate);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ThreadUtils.executeByCached(new ThreadUtils.SimpleTask<List<File>>() {
            @Override
            public List<File> doInBackground() throws Throwable {
                ArrayList<File> lists = new ArrayList<>();
                lists.add(new File(mLeftPath1));
                lists.add(new File(mRightPath2));
                lists.add(new File(mRecognitionPath));
                for (int i = 0; i < lists.size(); i++){
                    Log.i(TAG, " compress start = " + lists.get(i).getPath() +
                            " size = " +  FileUtils.getSize(lists.get(i)));
                }
                List<File> results = Luban.with(getContext()).load(lists).get();

                ArrayList<File> temp = new ArrayList<>();
                temp.addAll(results);
                return temp;
            }

            @Override
            public void onSuccess(List<File> result) {
                if (result.size() < 3){
                    ToastUtils.showShort("compress failure .");
                    return;
                }
                for (int i = 0; i < result.size(); i++){
                    Log.i(TAG, " compress result = " + result.get(i).getPath() +
                            " size = " +  FileUtils.getSize(result.get(i)));
                }
                mLeftPath1 = result.get(0).getPath();
                mRightPath2 = result.get(1).getPath();
                mRecognitionPath = result.get(2).getPath();

                OkGo.<String>post(Api.UPLOAD_IDENTITY).tag(TAG)
                        .params("data", jsonObject.toString())
                        //证件 正面照片
                        .params("identity_photo_front", result.get(0))
                        //证件 背面照片
                        .params("identity_photo_back", result.get(1))
                        //自拍照
                        .params("photo_self", result.get(2))
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                PhaseBean phaseBean = checkResponseSuccess(response, PhaseBean.class);
                                if (phaseBean == null) {
                                    Log.e(TAG, " upload contact error ." + response.body());
                                    return;
                                }
                                checkAndToPageByPhaseCode(phaseBean.getCurrent_phase());
                            }

                            @Override
                            public void onError(Response<String> response) {
                                super.onError(response);
                                Log.e(TAG, "upload contact failure = " + response.body());
                            }
                        });
            }
        });
    }

    @Override
    public void setProfileBean(AccountProfileBean.AccountProfile profileBean) {
        super.setProfileBean(profileBean);
        if (etName != null && !TextUtils.isEmpty(profileBean.getName())){
            etName.setEditTextAndSelection(profileBean.getName());
        }
        if (etFatherName != null && !TextUtils.isEmpty(profileBean.getFather_name())){
            etFatherName.setEditTextAndSelection(profileBean.getFather_name());
        }
        if (genderCheckBox != null && (profileBean.getGender() == 1
                || profileBean.getGender() == 2)){
            genderCheckBox.setPos(profileBean.getGender());
        }
//        if (etCnicNum != null && !TextUtils.isEmpty(profileBean.getN())) {
//            etCnicNum.setEditTextAndSelection();
//        }
        if (selectBirth != null && !TextUtils.isEmpty(profileBean.getBirthday())) {
            Log.e(TAG, " select birthday = " + profileBean.getBirthday());
//            selectBirth.setData();
        }
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
