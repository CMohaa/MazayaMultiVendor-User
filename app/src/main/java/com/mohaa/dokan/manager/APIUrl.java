package com.mohaa.dokan.manager;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class APIUrl {
    public static final String APP_URL            = "https://el-dokan.store/";
    public static final String BASE_URL = APP_URL + "Rova/public/";
    public static final String BASE_URL_AUTH =APP_URL +  "Auth/public/";
    public static final String BASE_WP_URL = APP_URL + "?rest_route=/wp/v2/";
    public static final String BASE_WP_REG_URL = APP_URL + "?rest_route=/wp/v2/users/";
    //  public static final String BASE_WC3_URL = APP_URL +  "wordpress/wc-api/v3/";
    public static final String BASE_WP3_URL = APP_URL +  "wp-json/wc/v3/";
    public static final String BASE_WP3Auth_URL = APP_URL + "wp-json/jwt-auth/v1/";
    public static final String BASE_URL_GCM = APP_URL + "gcm_chat/v1";
    public static final String BASE_URL_DOKAN = APP_URL + "wp-json/dokan/v1/";
    public static final String BASE_URL_Images = APP_URL + "Rova/public/uploadedFiles/";

    public static final String CONSUMERKEY        = "ck_813c3f015ad40bdc974604812f648b472021897a";//ck_de53d4ba9916e27295e5c8ca7dbf23488e358c4d
    public static final String CONSUMERSECRET     = "cs_5c49b1dea2f84230094f2cefb16b008893cdf447";//cs_15b80727372077ac1f5a2ad4cfa6c25864038394

    public static final String WP_API_VERSION     = "3";
    public static String encodeUrl(String url)
    {
        String encodedurl="";
        try {

            encodedurl = URLEncoder.encode(url,"UTF-8");
            Log.d("Encodeurl", encodedurl);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return encodedurl;
    }

}
