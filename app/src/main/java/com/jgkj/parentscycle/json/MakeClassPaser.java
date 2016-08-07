package com.jgkj.parentscycle.json;

import android.text.TextUtils;
import android.util.Log;

import com.jgkj.parentscycle.bean.CreateClassInfo;
import com.jgkj.parentscycle.bean.LoginInfo;
import com.jgkj.parentscycle.net.JsonUtil;
import com.jgkj.parentscycle.net.NetBeanSuper;
import com.jgkj.parentscycle.net.NetListener;
import com.jgkj.parentscycle.net.PaserJson;

import org.json.JSONException;

/**
 * Created by chen on 16/8/7.
 */
public class MakeClassPaser implements PaserJson {
    @Override
    public Object parseJSonObject(NetBeanSuper response) throws JSONException {
        response.setObj(new CreateClassInfo());
        return response;
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
        meData.setObj(new CreateClassInfo());
        return meData;
    }

    @Override
    public Object getNetNotConnectData() {
        NetBeanSuper meData = new NetBeanSuper();
        meData.setResult(NetListener.REQUEST_NET_NOT_CONNECT_CODE);
        meData.setMsg(NetListener.REQUEST_NOT_NET_ERROR_MSG);
        meData.setObj(new CreateClassInfo());
        return meData;
    }
}
