# NovaHttp
一个我现在爱不释手的网络请求封装思路。

现在你的网络请求只要如此

1.标注出你的参数
	

    /**
     * url
     */
    @NovaUrl
    final String URL = Constants.BASE_URL.concat("&c=user&a=resetPassword");
    /**
     * request
     */
    @NovaRequest(name = "old_password")
    String reqOldPsw;
    @NovaRequest(name = "new_password")
    String reqNewPsw;
    @NovaRequest(name = "user_id")
    String reqUserID;

    /**
     * response
     */
    @NovaResponse(mama = "code")
    String repCode;
    @NovaResponse(mama = "msg")
    String repMsg;

2.继承，绑定

3.Go

## 下面是一个栗子


	package com.lhave.yuebar.ui.activity;

	import android.os.Bundle;
	import android.text.InputType;
	import android.text.TextUtils;
	import android.view.View;
	import android.widget.EditText;
	
	import com.alipay.sdk.encrypt.MD5;
	import com.lhave.yuebar.APP;
	import com.lhave.yuebar.R;
	import com.lhave.yuebar.data.business.Constants;
	import com.lhave.yuebar.data.novahttpv3.INetDream;
	import com.lhave.yuebar.data.novahttpv3.MasterFu;
	import com.lhave.yuebar.data.novahttpv3.annotation.NovaRequest;
	import com.lhave.yuebar.data.novahttpv3.annotation.NovaResponse;
	import com.lhave.yuebar.data.novahttpv3.annotation.NovaUrl;
	import com.lhave.yuebar.ui.base.BaseActivity;
	import com.lhave.yuebar.utils.LogUtils;
	import com.sea_monster.common.Md5;
	
	import butterknife.Bind;
	import butterknife.ButterKnife;
	import butterknife.OnClick;
	
	/**
	 * Created by gongjiangpeng on 15/10/23.
	 */
	public class ChangePSWActivity extends BaseActivity implements INetDream{

    @Bind(R.id.old_psw)
    EditText mEdtOldPsw;
    @Bind(R.id.new_psw)
    EditText mEdtNewPsw;
    @Bind(R.id.new_psw_again)
    EditText mEdtNewPswAgain;

    MasterFu mMasterFu;

    /**
     * url
     */
    @NovaUrl
    final String URL = Constants.BASE_URL.concat("&c=user&a=resetPassword");

    /**
     * request
     */
    @NovaRequest(name = "old_password")
    String reqOldPsw;
    @NovaRequest(name = "new_password")
    String reqNewPsw;
    @NovaRequest(name = "user_id")
    String reqUserID;

    /**
     * response
     */
    @NovaResponse(mama = "code")
    String repCode;
    @NovaResponse(mama = "msg")
    String repMsg;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        ButterKnife.bind(this);

        initTitle();

        mMasterFu = MasterFu.create();
        mMasterFu.bind(this);
    }

    private void initTitle() {
        setTitle(R.string.change_psw);
    }

    @OnClick({R.id.see_psw, R.id.see_new_psw, R.id.see_new_psw_again})
    public void onShowPSWClicked(View view){
        switch (view.getId()){
            case R.id.see_psw:
                mEdtOldPsw.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                break;
            case R.id.see_new_psw:
                mEdtNewPsw.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                break;
            case R.id.see_new_psw_again:
                mEdtNewPswAgain.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                break;
        }
    }

    @OnClick(R.id.right)
    public void onCommitClicked(View view){
        String newPsw = mEdtNewPsw.getText().toString();
        String newPswAgain = mEdtNewPswAgain.getText().toString();

        if(TextUtils.isEmpty(newPsw) || TextUtils.isEmpty(newPswAgain) || TextUtils.isEmpty(mEdtOldPsw.getText().toString())){
            LogUtils.toast(this, R.string.empty_password);
            return;
        }

        if( newPsw.equals(newPswAgain) ){
            mMasterFu.go();
        }else{
            LogUtils.toast(this, R.string.two_psw_not_same);
        }
    }


    @Override
    public void pre(int requestID) {
        reqOldPsw = Md5.encode(mEdtOldPsw.getText().toString());
        reqNewPsw = Md5.encode(mEdtNewPsw.getText().toString());
        reqUserID = APP.getInstance().getCurrentUser().userID;
    }

    @Override
    public void done(int requestID, String result) {
        if( Constants.CODE_SUCCESS.equals(repCode) ){
            LogUtils.toast(this, R.string.change_psw_success);
            APP.getInstance().getCurrentUser().password = reqNewPsw;
            APP.getInstance().updateCurrentUser();
            finish();
        }else{
            LogUtils.toast(this, repMsg);
        }
    }

    @Override
    public void error(int requestID, String errMsg) {
        LogUtils.toast(this, errMsg);
    }
	}
	
## 使用说明(待补)	