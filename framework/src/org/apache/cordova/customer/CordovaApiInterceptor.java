package org.apache.cordova.customer;

/**
 * @author : yuzhiqang
 * @description: Cordova API 拦截器接口
 */
public interface CordovaApiInterceptor {

    /**
     * 判断是否允许指定 API 调用
     *
     * @param url     当前 WebView 加载的 URL
     * @param service 插件名 (如 "Geolocation")
     * @param action  方法名 (如 "getLocation")
     * @return true=允许调用, false=拦截
     */
    boolean shouldAllowApi(String url, String service, String action);
}
