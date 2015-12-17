package com.idealsense.novahttpv3;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.idealsense.novahttpv3.annotation.NovaRequest;
import com.idealsense.novahttpv3.annotation.NovaResponse;
import com.idealsense.novahttpv3.annotation.NovaUrl;
import com.idealsense.novahttpv3.model.Bar;

import java.util.List;

public class MainActivity extends AppCompatActivity implements INetDream{

    final int GET_BAR_DETAIL = 0;
    final int GET_BAR_LIST = 1;

    @NovaUrl( requestID = GET_BAR_DETAIL)
    String URL = "http://www.nb-moonlight.com/index.php?m=api&c=bar&a=show";

    @NovaUrl( requestID = GET_BAR_LIST )
    String mURL = "http://app.lhave.cn/index.php?m=api&c=bar&a=lists";

    @NovaRequest(name = "bar_id", requestID = GET_BAR_DETAIL)
    String mBarID = "6";

    @NovaResponse(mama = "data-name", requestID = GET_BAR_DETAIL)
    String mBarName;

    @NovaResponse(mama = "data-bar_lists", requestID = GET_BAR_LIST)
    List<Bar> mBarList;

    @NovaResponse(mama = "data-page", requestID = GET_BAR_LIST)
    String mResutlPage;

    @NovaResponse(mama = "msg", requestID = GET_BAR_LIST)
    String mResultMsg;


    MasterFu mMasterFu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMasterFu = MasterFu.create();

        mMasterFu.bind(this);

        mMasterFu.go(GET_BAR_LIST);
    }

    @Override
    public void pre(int requestID) {

    }

    @Override
    public void done(int requestID, String result) {
        Toast.makeText(this, "done!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void error(int requestID, String errMsg) {

    }
}
