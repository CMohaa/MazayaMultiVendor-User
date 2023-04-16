package com.mohaa.dokan.Utils;

import android.util.Log;


import com.mohaa.dokan.models.wp.WpmlLanguage;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Constant {

    public static final String MyPREFERENCES = "ccom.mohaa.dokan"; // Add your package name

    public static final String PACKAGE_NAME = "com.mohaa.dokan";  // Add your package name

    public static final String DeepLinkDomain = "https://www.facebook.com/Mohamed.Elareeg";// Add your Domain URL


    public static final int PlaystoreMinimumVersion = 1; // add your playstore version


    public static final String AppleAppStoreId = "1234567890";  // Apple Application AppStoreId

    public static final String AppleAppVersion = "1.0"; // Apple App version

    public static final String DEVICE_TYPE = "2";

    public static final String MOBILE_COUNTRY_CODE = "+20"; // Add your mobile country Code

    public static final int DISTANCERANGE = 500;

    public static final String PRIMARY_COLOR                 = "#04d39f";
    public static final String CIYASHOP_                     = "dokan";
    public static final String CIYASHOP_CODE                 = "dokan";
    public static String INFO_PAGE_DATA                = "";
    public static       boolean                IS_WISH_LIST_ACTIVE           = false;
    public static       boolean                IS_CURRENCY_SWITCHER_ACTIVE   = false;
    public static       boolean                IS_ORDER_TRACKING_ACTIVE      = false;
    public static       boolean                IS_GUEST_CHECKOUT_ACTIVE      = false;
    public static       boolean                IS_REWARD_POINT_ACTIVE        = false;
    public static       boolean                IS_WPML_ACTIVE                = false;
    public static       boolean                IS_LOGIN_SHOW                 = true;
    public static       boolean                IS_ADD_TO_CART_ACTIVE         = true;
    public static       boolean                IS_YITH_FEATURED_VIDEO_ACTIVE = false;
    public static       int                    Decimal                       = 2;
    public static String THOUSANDSSEPRETER             = ",";
    public static String DECIMALSEPRETER               = ".";
    public static       boolean                IS_CURRENCY_SET               = false;
    public static String SECONDARY_COLOR               = "#04d39f";

    public static String CURRENCYSYMBOL;
    public static String CURRENCYSYMBOLPref            = "";
    public static String CURRENCYSYMBOLPOSTION         = "left";

    public static String APPLOGO                       = "";
    public static String CURRENCYCODE                  = "";
    public static String APPLOGO_LIGHT                 = "";
    public static String DEVICE_TOKEN                  = "";
    public static String APP_VER                       = "app-ver";
    public static String APP_COLOR                     = "primary_color";
    public static String SECOND_COLOR                  = "secondary_color";
    public static String HEADER_COLOR                  = "header_color";
    public static String HEAD_COLOR                    = "#04d39f";
    public static String ADDRESS_LINE1                 = "address_line1";
    public static String ADDRESS_LINE2                 = "address_line2";
    public static String PHONE                         = "phone_number";
    public static String WHATSAPP                      = "whatsapp_number";
    public static String WHATSAPPENABLE                = "whatsappFloatingButton";
    public static String EMAIL                         = "email_address";
    public static String RTL                           = "is_rtl";
    public static       float                  LAT;
    public static       float                  LNG;
    public static String APP_TRANSPARENT               = "colorPrimaryTransperent";
    public static String APP_TRANSPARENT_VERY_LIGHT    = "colorPrimaryVeryLight";
    public static List<String> CurrencyList                  = new ArrayList<>();




    public static List<WpmlLanguage> LANGUAGELIST = new ArrayList<>();



    public static List<String> CheckoutURL = new ArrayList<>();

    public static List<String> CancelUrl = new ArrayList<>();


    public static String setDecimalTwo(Double digit) {
        return new DecimalFormat("##.##").format(digit);
    }

    public static String setDecimalOne(Double digit) {
        return new DecimalFormat("##.#").format(digit);
    }

    public static String setDecimal(Double digit) {

        String decimal = "#,##0.";

        if (Constant.Decimal == 0) {
            decimal = "#,##0";
        }
        if ((digit == Math.floor(digit)) && !Double.isInfinite(digit)) {
            // integer type
            for (int i = 0; i < Constant.Decimal; i++) {
                decimal = decimal + "0";
            }
        } else {
            for (int i = 0; i < Constant.Decimal; i++) {
                decimal = decimal + "#";
            }
        }


        DecimalFormatSymbols unusualSymbols = new DecimalFormatSymbols(Locale.US);
        if (Constant.Decimal != 0 && !Constant.THOUSANDSSEPRETER.equals("")) {
            unusualSymbols.setDecimalSeparator((char) Constant.DECIMALSEPRETER.charAt(0));
        }

        if (!Constant.THOUSANDSSEPRETER.equals("")) {
            unusualSymbols.setGroupingSeparator(Constant.THOUSANDSSEPRETER.charAt(0));
        }

//        String strange = "#,##0.000";
        DecimalFormat weirdFormatter = new DecimalFormat(decimal, unusualSymbols);

        weirdFormatter.setGroupingSize(3);

        String data = weirdFormatter.format(digit);
        Log.e("data is ", data + "");

        return data + "";
    }

    public static String setDate(String str) {
        try {
            DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
            DateFormat targetFormat = new SimpleDateFormat("MMM dd, yyyy");
            Date date = originalFormat.parse(str);
            String formattedDate = targetFormat.format(date);  // 20120821
            return formattedDate;
        } catch (ParseException e) {
            Log.e("error", e.getMessage());
        }
        return null;
    }


}
