package com.jgkj.parentscycle.json;

import android.text.TextUtils;
import android.util.Log;

import com.jgkj.parentscycle.bean.LoginInfo;
import com.jgkj.parentscycle.global.BgGlobal;
import com.jgkj.parentscycle.net.JsonUtil;
import com.jgkj.parentscycle.net.NetBeanSuper;
import com.jgkj.parentscycle.net.NetListener;
import com.jgkj.parentscycle.net.PaserJson;

import org.json.JSONException;

/**
 * Created by chen on 16/7/7.
 */
public class LoginPaser implements PaserJson {
    @Override
    public Object parseJSonObject(NetBeanSuper response) throws JSONException {
        Log.d("result","the response code:" + response);
//        String resultStr = "{\"msg\":\"登录成功！\",\"obj\":{\"company\":\"\",\"id\":\"1\",\"iocimg\":\"\",\"name\":\"\",\"openId\":\"\",\"page\":0,\"phone\":\"15810697038\",\"position\":\"\",\"qq\":\"\",\"rows\":0,\"url\":\"\",\"username\":\"\"},\"success\":true}";
//        NetBeanSuper as = (NetBeanSuper) JsonUtil.getTopObject(resultStr, NetBeanSuper.class);
//        String str = as.obj.toString();
        LoginInfo atatol=(LoginInfo) JsonUtil.getObject(response.getObj().toString(), LoginInfo.class);
        response.setObj(atatol);
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

        return meData;
    }

    @Override
    public Object getNetNotConnectData() {
        NetBeanSuper meData = new NetBeanSuper();
        meData.setResult(NetListener.REQUEST_NET_NOT_CONNECT_CODE);
        meData.setMsg(NetListener.REQUEST_NOT_NET_ERROR_MSG);
        return meData;
    }
}
