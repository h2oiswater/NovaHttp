package com.idealsense.novahttpv3;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.idealsense.novahttpv3.annotation.NovaRequest;
import com.idealsense.novahttpv3.annotation.NovaResponse;
import com.idealsense.novahttpv3.annotation.NovaUrl;

public class MainActivity extends AppCompatActivity implements INetDream{

    final int GET_BAR_DETAIL = 0;

    @NovaUrl( requestID = GET_BAR_DETAIL)
    String URL = "http://app.lhave.cn/index.php?m=api&c=bar&a=show";

    @NovaRequest(name = "bar_id", requestID = GET_BAR_DETAIL)
    String mBarID = "6";

    @NovaResponse(mama = "data-name", requestID = GET_BAR_DETAIL)
    String mBarName;

    MasterFu mMasterFu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMasterFu = MasterFu.create();

        mMasterFu.bind(this);

        mMasterFu.go(GET_BAR_DETAIL);
    }

    @Override
    public void pre(int requestID) {

    }

    @Override
    public void done(int requestID, String result) {
        String msg = "mBarName = ";
        Toast.makeText(this, msg + mBarName, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void error(int requestID, String errMsg) {

    }
}
