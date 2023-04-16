package com.mohaa.dokan.manager;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mohaa.dokan.manager.wp.services.HMACSha1SignatureService;
import com.mohaa.dokan.manager.wp.services.TimestampServiceImpl;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.mohaa.dokan.manager.APIUrl.CONSUMERKEY;
import static com.mohaa.dokan.manager.APIUrl.CONSUMERSECRET;
import static com.mohaa.dokan.manager.APIUrl.encodeUrl;


public class RetrofitApi {

    public static Retrofit retrofitWrite()
    {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();


        return new Retrofit.Builder()
                .baseUrl(APIUrl.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }
    public static Retrofit retrofitWriteRegister()
    {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();


        return new Retrofit.Builder()
                .baseUrl(APIUrl.APP_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }
    public static Retrofit retrofitWriteWP3AUTH()
    {

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        return new Retrofit.Builder()
                .baseUrl(APIUrl.BASE_WP3Auth_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }
    public static Retrofit retrofitWriteWP3Comments(String endpoint)
    {

        final String nonce=new TimestampServiceImpl().getNonce();
        final String timestamp=new TimestampServiceImpl().getTimestampInSeconds();
        String firstEncodedString ="POST&"+encodeUrl(APIUrl.BASE_WP3_URL + endpoint);
        String product_id = "5364";
        String review = "Very Gooood";
        String reviewer = "Mohamed El Sayed";
        String reviewer_email = "mohaa,coder@yahoo.com";
        String rating = "5";
        String parameterString="product_id=" +product_id+"review=" +review+"reviewer=" +reviewer+"reviewer_email=" +reviewer_email+"rating=" +rating+ "&oauth_consumer_key="+ CONSUMERKEY+"&oauth_nonce="+nonce+"&oauth_signature_method=HMAC-SHA1&oauth_timestamp="+timestamp+"&oauth_version=1.0";
        String secoundEncodedString="&"+encodeUrl(parameterString);

        String baseString=firstEncodedString+secoundEncodedString;
        //THE BASE STRING AND COSTUMER_SECRET KEY IS USED FOR GENERATING SIGNATURE
        String signature=new HMACSha1SignatureService().getSignature(baseString, CONSUMERSECRET,"");

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient oauthsignature = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();
                        HttpUrl httpUrl = original.url();

                        HttpUrl newHttpurl = httpUrl.newBuilder()
                                .addQueryParameter("product_id" ,product_id)
                                .addQueryParameter("review" ,review)
                                .addQueryParameter("reviewer" ,reviewer)
                                .addQueryParameter("reviewer_email" ,reviewer_email)
                                .addQueryParameter("rating" ,rating)
                                .addQueryParameter("oauth_consumer_key" , CONSUMERKEY)
                                .addQueryParameter("oauth_signature_method" ,"HMAC-SHA1")
                                .addQueryParameter("oauth_timestamp" ,timestamp)
                                .addQueryParameter("oauth_nonce" ,nonce )
                                .addQueryParameter("oauth_version" ,"1.0")
                                .addQueryParameter("oauth_signature" ,signature)
                                .build();
                        Request.Builder requestBuilder = original.newBuilder().url(newHttpurl);
                        Request request = requestBuilder.build();

                        return chain.proceed(request);
                    }
                })
                .addInterceptor(interceptor)
                .build();
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        return new Retrofit.Builder()
                .baseUrl(APIUrl.BASE_WP3_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(oauthsignature)
                .build();
    }
    public static Retrofit retrofitWriteWP3Body(String endpoint)
    {

        final String nonce=new TimestampServiceImpl().getNonce();
        final String timestamp=new TimestampServiceImpl().getTimestampInSeconds();
        String firstEncodedString ="POST&"+encodeUrl(APIUrl.BASE_WP3_URL + endpoint);

        String parameterString="oauth_consumer_key="+ CONSUMERKEY+"&oauth_nonce="+nonce+"&oauth_signature_method=HMAC-SHA1&oauth_timestamp="+timestamp+"&oauth_version=1.0";
        String secoundEncodedString="&"+encodeUrl(parameterString);

        String baseString=firstEncodedString+secoundEncodedString;
        //THE BASE STRING AND COSTUMER_SECRET KEY IS USED FOR GENERATING SIGNATURE
        String signature=new HMACSha1SignatureService().getSignature(baseString, CONSUMERSECRET,"");

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient oauthsignature = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();
                        HttpUrl httpUrl = original.url();

                        HttpUrl newHttpurl = httpUrl.newBuilder()

                                .addQueryParameter("oauth_consumer_key" , CONSUMERKEY)
                                .addQueryParameter("oauth_signature_method" ,"HMAC-SHA1")
                                .addQueryParameter("oauth_timestamp" ,timestamp)
                                .addQueryParameter("oauth_nonce" ,nonce )
                                .addQueryParameter("oauth_version" ,"1.0")
                                .addQueryParameter("oauth_signature" ,signature)
                                .build();
                        Request.Builder requestBuilder = original.newBuilder().url(newHttpurl);
                        Request request = requestBuilder.build();

                        return chain.proceed(request);
                    }
                })
                .addInterceptor(interceptor)
                .build();
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        return new Retrofit.Builder()
                .baseUrl(APIUrl.BASE_WP3_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(oauthsignature)
                .build();
    }
    public static Retrofit retrofitRead()
    {

        return new Retrofit.Builder()
            .baseUrl(APIUrl.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    }
    public static Retrofit retrofitReadAuth()
    {

        return new Retrofit.Builder()
                .baseUrl(APIUrl.BASE_URL_AUTH)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
    public static Retrofit retrofitReadWP()
    {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        return new Retrofit.Builder()
                .baseUrl(APIUrl.BASE_WP_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }
    public static Retrofit retrofitReadWD()
    {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();
        return new Retrofit.Builder()
                .baseUrl(APIUrl.BASE_URL_DOKAN)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();
    }


    public static Retrofit retrofitReadWP3Z()
    {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        return new Retrofit.Builder()
                .baseUrl(APIUrl.BASE_WP3_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();
    }
    public static Retrofit retrofitReadWP3Z(String endpoint)
    {

        final String nonce=new TimestampServiceImpl().getNonce();
        final String timestamp=new TimestampServiceImpl().getTimestampInSeconds();
        String firstEncodedString ="GET&"+encodeUrl(APIUrl.BASE_WP3_URL + endpoint);
        String parameterString="oauth_consumer_key="+ CONSUMERKEY+"&oauth_nonce="+nonce+"&oauth_signature_method=HMAC-SHA1&oauth_timestamp="+timestamp+"&oauth_version=1.0";
        String secoundEncodedString="&"+encodeUrl(parameterString);

        String baseString=firstEncodedString+secoundEncodedString;
        //THE BASE STRING AND COSTUMER_SECRET KEY IS USED FOR GENERATING SIGNATURE
        String signature=new HMACSha1SignatureService().getSignature(baseString, CONSUMERSECRET,"");

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient oauthsignature = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();
                        HttpUrl httpUrl = original.url();

                        HttpUrl newHttpurl = httpUrl.newBuilder()
                                .addQueryParameter("oauth_consumer_key" , CONSUMERKEY)
                                .addQueryParameter("oauth_signature_method" ,"HMAC-SHA1")
                                .addQueryParameter("oauth_timestamp" ,timestamp)
                                .addQueryParameter("oauth_nonce" ,nonce )
                                .addQueryParameter("oauth_version" ,"1.0")
                                .addQueryParameter("oauth_signature" ,signature)
                                .build();
                        Request.Builder requestBuilder = original.newBuilder().url(newHttpurl);
                        Request request = requestBuilder.build();

                        return chain.proceed(request);
                    }
                })
                .addInterceptor(interceptor)
                .build();
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        return new Retrofit.Builder()
                .baseUrl(APIUrl.BASE_WP3_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(oauthsignature)
                .build();
    }
    public static Retrofit retrofitReadWP3Product(String endpoint)
    {


        final String nonce=new TimestampServiceImpl().getNonce();
        final String timestamp=new TimestampServiceImpl().getTimestampInSeconds();
        String firstEncodedString ="GET&"+encodeUrl(APIUrl.BASE_WP3_URL + endpoint);
        String parameterString= "oauth_consumer_key="+ CONSUMERKEY+"&oauth_nonce="+nonce+"&oauth_signature_method=HMAC-SHA1&oauth_timestamp="+timestamp+"&oauth_version=1.0"  ;
        String secoundEncodedString="&"+encodeUrl(parameterString);
        //String paramString = encodeUrl("&category=18");

        String baseString=firstEncodedString+secoundEncodedString ;//+ paramString
        Log.d("baseString",baseString);
        //THE BASE STRING AND COSTUMER_SECRET KEY IS USED FOR GENERATING SIGNATURE
        String signature=new HMACSha1SignatureService().getSignature(baseString, CONSUMERSECRET,"");

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient oauthsignature = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();
                        HttpUrl httpUrl = original.url();

                        HttpUrl newHttpurl = httpUrl.newBuilder()

                                .addQueryParameter("oauth_consumer_key" , CONSUMERKEY)
                                .addQueryParameter("oauth_signature_method" ,"HMAC-SHA1")
                                .addQueryParameter("oauth_timestamp" ,timestamp)
                                .addQueryParameter("oauth_nonce" ,nonce )
                                .addQueryParameter("oauth_version" ,"1.0")
                                .addQueryParameter("oauth_signature" ,signature)
                                .build();
                        Request.Builder requestBuilder = original.newBuilder().url(newHttpurl);
                        Request request = requestBuilder.build();

                        return chain.proceed(request);
                    }
                })
                .addInterceptor(interceptor)
                .build();
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        return new Retrofit.Builder()
                .baseUrl(APIUrl.BASE_WP3_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(oauthsignature)
                .build();
    }
    public static Retrofit retrofitReadWP3ProductsDefault(String endpoint  , String orderby , String pagenumber)
    {


        final String nonce=new TimestampServiceImpl().getNonce();
        final String timestamp=new TimestampServiceImpl().getTimestampInSeconds();
        String firstEncodedString ="GET&"+encodeUrl(APIUrl.BASE_WP3_URL + endpoint);
        String parameterString= "oauth_consumer_key="+ CONSUMERKEY+"&oauth_nonce="+nonce+"&oauth_signature_method=HMAC-SHA1&oauth_timestamp="+timestamp+"&oauth_version=1.0&orderby=" +orderby +"&page=" + pagenumber ;
        String secoundEncodedString="&"+encodeUrl(parameterString);
        //String paramString = encodeUrl("&category=18");

        String baseString=firstEncodedString+secoundEncodedString ;//+ paramString
        Log.d("baseString",baseString);
        //THE BASE STRING AND COSTUMER_SECRET KEY IS USED FOR GENERATING SIGNATURE
        String signature=new HMACSha1SignatureService().getSignature(baseString, CONSUMERSECRET,"");

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient oauthsignature = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();
                        HttpUrl httpUrl = original.url();

                        HttpUrl newHttpurl = httpUrl.newBuilder()

                                .addQueryParameter("oauth_consumer_key" , CONSUMERKEY)
                                .addQueryParameter("oauth_signature_method" ,"HMAC-SHA1")
                                .addQueryParameter("oauth_timestamp" ,timestamp)
                                .addQueryParameter("oauth_nonce" ,nonce )
                                .addQueryParameter("oauth_version" ,"1.0")
                                .addQueryParameter("oauth_signature" ,signature)
                                .addQueryParameter("orderby" ,orderby)
                                .addQueryParameter("page" ,pagenumber)
                                .build();
                        Request.Builder requestBuilder = original.newBuilder().url(newHttpurl);
                        Request request = requestBuilder.build();

                        return chain.proceed(request);
                    }
                })
                .addInterceptor(interceptor)
                .build();
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        return new Retrofit.Builder()
                .baseUrl(APIUrl.BASE_WP3_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(oauthsignature)
                .build();
    }
    public static Retrofit retrofitReadWP3ProductsInclude(String endpoint  , String includes , String pagenumber)
    {


        final String nonce=new TimestampServiceImpl().getNonce();
        final String timestamp=new TimestampServiceImpl().getTimestampInSeconds();
        String firstEncodedString ="GET&"+encodeUrl(APIUrl.BASE_WP3_URL + endpoint);
        String parameterString= "include=" + includes + "&oauth_consumer_key="+ CONSUMERKEY+"&oauth_nonce="+nonce+"&oauth_signature_method=HMAC-SHA1&oauth_timestamp="+timestamp+"&oauth_version=1.0"  ;
        String secoundEncodedString="&"+encodeUrl(parameterString);
        //String paramString = encodeUrl("&category=18");

        String baseString=firstEncodedString+secoundEncodedString ;//+ paramString
        Log.d("baseString",baseString);
        //THE BASE STRING AND COSTUMER_SECRET KEY IS USED FOR GENERATING SIGNATURE
        String signature=new HMACSha1SignatureService().getSignature(baseString, CONSUMERSECRET,"");

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient oauthsignature = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();
                        HttpUrl httpUrl = original.url();

                        HttpUrl newHttpurl = httpUrl.newBuilder()
                                .addQueryParameter("include" ,includes)
                                .addQueryParameter("oauth_consumer_key" , CONSUMERKEY)
                                .addQueryParameter("oauth_signature_method" ,"HMAC-SHA1")
                                .addQueryParameter("oauth_timestamp" ,timestamp)
                                .addQueryParameter("oauth_nonce" ,nonce )
                                .addQueryParameter("oauth_version" ,"1.0")
                                .addQueryParameter("oauth_signature" ,signature)


                                .build();
                        Request.Builder requestBuilder = original.newBuilder().url(newHttpurl);
                        Request request = requestBuilder.build();

                        return chain.proceed(request);
                    }
                })
                .addInterceptor(interceptor)
                .build();
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        return new Retrofit.Builder()
                .baseUrl(APIUrl.BASE_WP3_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(oauthsignature)
                .build();
    }
    public static Retrofit retrofitReadWP3ProductsSearch(String endpoint , String search , String orderby , String pagenumber)
    {


        final String nonce=new TimestampServiceImpl().getNonce();
        final String timestamp=new TimestampServiceImpl().getTimestampInSeconds();
        String firstEncodedString ="GET&"+encodeUrl(APIUrl.BASE_WP3_URL + endpoint);
        String parameterString= "oauth_consumer_key="+ CONSUMERKEY+"&oauth_nonce="+nonce+"&oauth_signature_method=HMAC-SHA1&oauth_timestamp="+timestamp+"&oauth_version=1.0&search=" +search ;
        String secoundEncodedString="&"+encodeUrl(parameterString);
        //String paramString = encodeUrl("&category=18");

        String baseString=firstEncodedString+secoundEncodedString ;//+ paramString
        Log.d("baseString",baseString);
        //THE BASE STRING AND COSTUMER_SECRET KEY IS USED FOR GENERATING SIGNATURE
        String signature=new HMACSha1SignatureService().getSignature(baseString, CONSUMERSECRET,"");

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient oauthsignature = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();
                        HttpUrl httpUrl = original.url();

                        HttpUrl newHttpurl = httpUrl.newBuilder()

                                .addQueryParameter("oauth_consumer_key" , CONSUMERKEY)
                                .addQueryParameter("oauth_signature_method" ,"HMAC-SHA1")
                                .addQueryParameter("oauth_timestamp" ,timestamp)
                                .addQueryParameter("oauth_nonce" ,nonce )
                                .addQueryParameter("oauth_version" ,"1.0")
                                .addQueryParameter("oauth_signature" ,signature)
                                .addQueryParameter("search" ,search)


                                .build();
                        Request.Builder requestBuilder = original.newBuilder().url(newHttpurl);
                        Request request = requestBuilder.build();

                        return chain.proceed(request);
                    }
                })
                .addInterceptor(interceptor)
                .build();
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        return new Retrofit.Builder()
                .baseUrl(APIUrl.BASE_WP3_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(oauthsignature)
                .build();
    }
    public static Retrofit retrofitReadWP3ProductsByCategory(String endpoint , String category , String orderby , String pagenumber)
    {


        final String nonce=new TimestampServiceImpl().getNonce();
        final String timestamp=new TimestampServiceImpl().getTimestampInSeconds();
        String firstEncodedString ="GET&"+encodeUrl(APIUrl.BASE_WP3_URL + endpoint);
        String parameterString= "category=" +category+ "&oauth_consumer_key="+ CONSUMERKEY+"&oauth_nonce="+nonce+"&oauth_signature_method=HMAC-SHA1&oauth_timestamp="+timestamp+"&oauth_version=1.0&orderby=" +orderby +"&page=" + pagenumber ;
        String secoundEncodedString="&"+encodeUrl(parameterString);
        //String paramString = encodeUrl("&category=18");

        String baseString=firstEncodedString+secoundEncodedString ;//+ paramString
        Log.d("baseString",baseString);
        //THE BASE STRING AND COSTUMER_SECRET KEY IS USED FOR GENERATING SIGNATURE
        String signature=new HMACSha1SignatureService().getSignature(baseString, CONSUMERSECRET,"");

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient oauthsignature = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();
                        HttpUrl httpUrl = original.url();

                        HttpUrl newHttpurl = httpUrl.newBuilder()
                                .addQueryParameter("category" ,category)
                                .addQueryParameter("oauth_consumer_key" , CONSUMERKEY)
                                .addQueryParameter("oauth_signature_method" ,"HMAC-SHA1")
                                .addQueryParameter("oauth_timestamp" ,timestamp)
                                .addQueryParameter("oauth_nonce" ,nonce )
                                .addQueryParameter("oauth_version" ,"1.0")
                                .addQueryParameter("oauth_signature" ,signature)
                                .addQueryParameter("orderby" ,orderby)
                                .addQueryParameter("page" ,pagenumber)
                                .build();
                        Request.Builder requestBuilder = original.newBuilder().url(newHttpurl);
                        Request request = requestBuilder.build();

                        return chain.proceed(request);
                    }
                })
                .addInterceptor(interceptor)
                .build();
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        return new Retrofit.Builder()
                .baseUrl(APIUrl.BASE_WP3_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(oauthsignature)
                .build();
    }
    public static Retrofit retrofitReadWP3Coupons(String endpoint , String code)
    {


        final String nonce=new TimestampServiceImpl().getNonce();
        final String timestamp=new TimestampServiceImpl().getTimestampInSeconds();
        String firstEncodedString ="GET&"+encodeUrl(APIUrl.BASE_WP3_URL + endpoint);
        String parameterString= "code=" +code+ "&oauth_consumer_key="+ CONSUMERKEY+"&oauth_nonce="+nonce+"&oauth_signature_method=HMAC-SHA1&oauth_timestamp="+timestamp+"&oauth_version=1.0"  ;
        String secoundEncodedString="&"+encodeUrl(parameterString);
        //String paramString = encodeUrl("&category=18");

        String baseString=firstEncodedString+secoundEncodedString ;//+ paramString
        Log.d("baseString",baseString);
        //THE BASE STRING AND COSTUMER_SECRET KEY IS USED FOR GENERATING SIGNATURE
        String signature=new HMACSha1SignatureService().getSignature(baseString, CONSUMERSECRET,"");

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient oauthsignature = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();
                        HttpUrl httpUrl = original.url();

                        HttpUrl newHttpurl = httpUrl.newBuilder()
                                .addQueryParameter("code" ,code)
                                .addQueryParameter("oauth_consumer_key" , CONSUMERKEY)
                                .addQueryParameter("oauth_signature_method" ,"HMAC-SHA1")
                                .addQueryParameter("oauth_timestamp" ,timestamp)
                                .addQueryParameter("oauth_nonce" ,nonce )
                                .addQueryParameter("oauth_version" ,"1.0")
                                .addQueryParameter("oauth_signature" ,signature)

                                .build();
                        Request.Builder requestBuilder = original.newBuilder().url(newHttpurl);
                        Request request = requestBuilder.build();

                        return chain.proceed(request);
                    }
                })
                .addInterceptor(interceptor)
                .build();
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        return new Retrofit.Builder()
                .baseUrl(APIUrl.BASE_WP3_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(oauthsignature)
                .build();
    }

    public static Retrofit retrofitReadWP3Categories(String endpoint , String parent , String orderby )
    {


        final String nonce=new TimestampServiceImpl().getNonce();
        final String timestamp=new TimestampServiceImpl().getTimestampInSeconds();
        String firstEncodedString ="GET&"+encodeUrl(APIUrl.BASE_WP3_URL + endpoint);
        String parameterString= "oauth_consumer_key="+ CONSUMERKEY+"&oauth_nonce="+nonce+"&oauth_signature_method=HMAC-SHA1&oauth_timestamp="+timestamp+"&oauth_version=1.0&parent=" +parent  ;
        String secoundEncodedString="&"+encodeUrl(parameterString);
        //String paramString = encodeUrl("&category=18");

        String baseString=firstEncodedString+secoundEncodedString ;//+ paramString
        Log.d("baseString",baseString);
        //THE BASE STRING AND COSTUMER_SECRET KEY IS USED FOR GENERATING SIGNATURE
        String signature=new HMACSha1SignatureService().getSignature(baseString, CONSUMERSECRET,"");

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient oauthsignature = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();
                        HttpUrl httpUrl = original.url();

                        HttpUrl newHttpurl = httpUrl.newBuilder()
                               // .addQueryParameter("parent" ,parent)
                                .addQueryParameter("oauth_consumer_key" , CONSUMERKEY)
                                .addQueryParameter("oauth_signature_method" ,"HMAC-SHA1")
                                .addQueryParameter("oauth_timestamp" ,timestamp)
                                .addQueryParameter("oauth_nonce" ,nonce )
                                .addQueryParameter("oauth_version" ,"1.0")
                                .addQueryParameter("oauth_signature" ,signature)
                                .addQueryParameter("parent" ,parent)

                                .build();
                        Request.Builder requestBuilder = original.newBuilder().url(newHttpurl);
                        Request request = requestBuilder.build();

                        return chain.proceed(request);
                    }
                })
                .addInterceptor(interceptor)
                .build();
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        return new Retrofit.Builder()
                .baseUrl(APIUrl.BASE_WP3_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(oauthsignature)
                .build();
    }

    public static Retrofit retrofitReadWP3ProductsReviews(String endpoint , String product , String pageno )
    {


        final String nonce=new TimestampServiceImpl().getNonce();
        final String timestamp=new TimestampServiceImpl().getTimestampInSeconds();
        String firstEncodedString ="GET&"+encodeUrl(APIUrl.BASE_WP3_URL + endpoint);
        String parameterString= "oauth_consumer_key="+ CONSUMERKEY+"&oauth_nonce="+nonce+"&oauth_signature_method=HMAC-SHA1&oauth_timestamp="+timestamp+"&oauth_version=1.0&product=" +product  ;
        String secoundEncodedString="&"+encodeUrl(parameterString);
        //String paramString = encodeUrl("&category=18");

        String baseString=firstEncodedString+secoundEncodedString ;//+ paramString
        Log.d("baseString",baseString);
        //THE BASE STRING AND COSTUMER_SECRET KEY IS USED FOR GENERATING SIGNATURE
        String signature=new HMACSha1SignatureService().getSignature(baseString, CONSUMERSECRET,"");

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient oauthsignature = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();
                        HttpUrl httpUrl = original.url();

                        HttpUrl newHttpurl = httpUrl.newBuilder()
                                 //.addQueryParameter("product" ,product)
                                .addQueryParameter("oauth_consumer_key" , CONSUMERKEY)
                                .addQueryParameter("oauth_signature_method" ,"HMAC-SHA1")
                                .addQueryParameter("oauth_timestamp" ,timestamp)
                                .addQueryParameter("oauth_nonce" ,nonce )
                                .addQueryParameter("oauth_version" ,"1.0")
                                .addQueryParameter("oauth_signature" ,signature)
                                .addQueryParameter("product" ,product)

                                .build();
                        Request.Builder requestBuilder = original.newBuilder().url(newHttpurl);
                        Request request = requestBuilder.build();

                        return chain.proceed(request);
                    }
                })
                .addInterceptor(interceptor)
                .build();
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        return new Retrofit.Builder()
                .baseUrl(APIUrl.BASE_WP3_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(oauthsignature)
                .build();
    }
    public static Retrofit retrofitReadWP3OrdersByCustomer(String endpoint , String customer , String orderby , String pagenumber)
    {

        final String nonce=new TimestampServiceImpl().getNonce();
        final String timestamp=new TimestampServiceImpl().getTimestampInSeconds();
        String firstEncodedString ="GET&"+encodeUrl(APIUrl.BASE_WP3_URL + endpoint);
        String parameterString= "customer=" +customer+ "&oauth_consumer_key="+ CONSUMERKEY+"&oauth_nonce="+nonce+"&oauth_signature_method=HMAC-SHA1&oauth_timestamp="+timestamp+"&oauth_version=1.0&orderby=" +orderby +"&page=" + pagenumber ;
        String secoundEncodedString="&"+encodeUrl(parameterString);
        //String paramString = encodeUrl("&category=18");

        String baseString=firstEncodedString+secoundEncodedString ;//+ paramString
        Log.d("baseString",baseString);
        //THE BASE STRING AND COSTUMER_SECRET KEY IS USED FOR GENERATING SIGNATURE
        String signature=new HMACSha1SignatureService().getSignature(baseString, CONSUMERSECRET,"");

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient oauthsignature = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();
                        HttpUrl httpUrl = original.url();

                        HttpUrl newHttpurl = httpUrl.newBuilder()
                                .addQueryParameter("customer" ,customer)
                                .addQueryParameter("oauth_consumer_key" , CONSUMERKEY)
                                .addQueryParameter("oauth_signature_method" ,"HMAC-SHA1")
                                .addQueryParameter("oauth_timestamp" ,timestamp)
                                .addQueryParameter("oauth_nonce" ,nonce )
                                .addQueryParameter("oauth_version" ,"1.0")
                                .addQueryParameter("oauth_signature" ,signature)
                                .addQueryParameter("orderby" ,orderby)
                                .addQueryParameter("page" ,pagenumber)
                                .build();
                        Request.Builder requestBuilder = original.newBuilder().url(newHttpurl);
                        Request request = requestBuilder.build();

                        return chain.proceed(request);
                    }
                })
                .addInterceptor(interceptor)
                .build();
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        return new Retrofit.Builder()
                .baseUrl(APIUrl.BASE_WP3_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(oauthsignature)
                .build();
    }
}
