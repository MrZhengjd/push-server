package com.zheng.constants;

import java.util.ResourceBundle;

public class Constants {
    public final static String ZOOKEEPER_CENTER = ResourceBundle.getBundle("zookeeper").getString("zookeeper.server.center");
    public final static String IM_SERVER_PATH = ResourceBundle.getBundle("zookeeper").getString("zookeeper.imserver.path");
    public final static int SystemPropertyAckMessageControllerTimeOutValue = 10000;
    public final static String TAG = "PUSH";
}
