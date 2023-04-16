package com.mohaa.dokan.models.wp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WpmlLanguage {

    @SerializedName("code")
    @Expose
    public String  code;
    @SerializedName("id")
    @Expose
    public String  id;
    @SerializedName("native_name")
    @Expose
    public String  nativeName;
    @SerializedName("active")
    @Expose
    public int     active;
    @SerializedName("default_locale")
    @Expose
    public String  defaultLocale;
    @SerializedName("translated_name")
    @Expose
    public String  translatedName;
    @SerializedName("language_code")
    @Expose
    public String  languageCode;
    @SerializedName("disp_language")
    @Expose
    public String  dispLanguage;
    @SerializedName("site_language")
    @Expose
    public String  siteLanguage;
    @SerializedName("is_rtl")
    @Expose
    public boolean isRtl;

    public WpmlLanguage withCode(String code) {
        this.code = code;
        return this;
    }

    public WpmlLanguage withId(String id) {
        this.id = id;
        return this;
    }

    public WpmlLanguage withNativeName(String nativeName) {
        this.nativeName = nativeName;
        return this;
    }

    public WpmlLanguage withActive(int active) {
        this.active = active;
        return this;
    }

    public WpmlLanguage withDefaultLocale(String defaultLocale) {
        this.defaultLocale = defaultLocale;
        return this;
    }

    public WpmlLanguage withTranslatedName(String translatedName) {
        this.translatedName = translatedName;
        return this;
    }

    public WpmlLanguage withLanguageCode(String languageCode) {
        this.languageCode = languageCode;
        return this;
    }

    public WpmlLanguage withDispLanguage(String dispLanguage) {
        this.dispLanguage = dispLanguage;
        return this;
    }

    public WpmlLanguage withSiteLanguage(String siteLanguage) {
        this.siteLanguage = siteLanguage;
        return this;
    }

    public WpmlLanguage withIsRtl(boolean isRtl) {
        this.isRtl = isRtl;
        return this;
    }
}
