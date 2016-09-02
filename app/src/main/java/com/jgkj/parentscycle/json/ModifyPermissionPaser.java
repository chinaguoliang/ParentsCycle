package com.jgkj.parentscycle.json;

import android.text.TextUtils;

import com.jgkj.parentscycle.bean.ModifyPassInfo;
import com.jgkj.parentscycle.bean.ModifyPermissionInfo;
import com.jgkj.parentscycle.net.JsonUtil;
import com.jgkj.parentscycle.net.NetBeanSuper;
import com.jgkj.parentscycle.net.NetListener;
import com.jgkj.parentscycle.net.PaserJson;

import org.json.JSONException;

/**
 * Created by chen on 16/8/29.
 */
public class ModifyPermissionPaser implements PaserJson {
    @Override
    public Object parseJSonObject(NetBeanSuper response) throws JSONException {
//        if (response.obj != null) {
//            ModifyPermissionInfo atatol=(ModifyPermissionInfo) JsonUtil.getObject(response.getObj().toString(), ModifyPermissionInfo.class);
//            response.setObj(atatol);
//            return response;
//        } else {
            response.setObj(new ModifyPermissionInfo());
            return response;
//        }

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
        meData.setObj(new ModifyPermissionInfo());
        return meData;
    }

    @Override
    public Object getNetNotConnectData() {
        NetBeanSuper meData = new NetBeanSuper();
        meData.setResult(NetListener.REQUEST_NET_NOT_CONNECT_CODE);
        meData.setMsg(NetListener.REQUEST_NOT_NET_ERROR_MSG);
        meData.setObj(new ModifyPermissionInfo());
        return meData;
    }
}
