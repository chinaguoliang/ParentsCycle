package com.videogo.bean;

import android.text.TextUtils;
import android.util.Log;


import net.JsonUtil;
import net.NetBeanSuper;
import net.NetListener;
import net.PaserJson;

import org.json.JSONException;

/**
 * Created by chen on 16/10/23.
 */
public class GetVideoControlTimeInfoPaser implements PaserJson {
    @Override
    public Object parseJSonObject(NetBeanSuper response) throws JSONException {
        Log.d("result", "the response code:" + response);
        if (response.obj != null) {
            GetVideoControlTimeInfo atatol=(GetVideoControlTimeInfo) JsonUtil.getObject(response.getObj().toString(), GetVideoControlTimeInfo.class);
            response.setObj(atatol);
            return response;
        } else {
            response.setObj(new GetVideoControlTimeInfo());
            return response;
        }

    }

    @Override
    public Object getErrorBeanData(String msg) {
        NetBeanSuper meData = new NetBeanSuper();
        meData.setResult(NetListener.REQUEST_NET_ERROR_CODE);
        if (TextUtils.isEmpty(msg)) {
            meData.setMsg(NetListener.REQUEST_NET_ERROR_MSG);
        } else {
            meData.setMsg(msg);
        }
        meData.setObj(new GetVideoControlTimeInfo());
        return meData;
    }

    @Override
    public Object getNetNotConnectData() {
        NetBeanSuper meData = new NetBeanSuper();
        meData.setResult(NetListener.REQUEST_NET_NOT_CONNECT_CODE);
        meData.setMsg(NetListener.REQUEST_NOT_NET_ERROR_MSG);
        meData.setObj(new GetVideoControlTimeInfo());
        return meData;
    }
}
