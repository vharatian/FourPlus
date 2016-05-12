package ir.pegahtech.backtory.http;

import com.google.gson.Gson;
import com.squareup.okhttp.CacheControl;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import ir.pegahtech.backtory.conf.SaaSConfiguration;

public class HttpConnectionUtility {
    //public static OkHttpClient httpClient = null;

    public static String getUrlWithPathParams(String url, Map<String, Object> pathParameters) {
        String resultUrl = url;
        if (pathParameters == null || pathParameters.size() == 0)
            return resultUrl;

        for (String key : pathParameters.keySet()) {
            String value = "";
            if (pathParameters.get(key) != null)
                value = pathParameters.get(key).toString();
            resultUrl = resultUrl.replace("{" + key + "}", value);
        }
        return resultUrl;
    }

    public static String getUrlWithQueryParams(String url, Map<String, Object> queryParameters) {
        if (queryParameters == null || queryParameters.size() == 0)
            return url;

        try {
            String resultUrl = url;
            if (!resultUrl.endsWith("?"))
                resultUrl += "?";

            String paramString = "";
            for (String key : queryParameters.keySet()) {
                Object value = queryParameters.get(key);
                if (key == null)
                    continue;
                paramString += key + "=" + URLEncoder.encode(value == null ? null : value.toString() + "&", "UTF-8");
            }
            resultUrl += paramString;
            return resultUrl;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] getByteArrayUrl
            (
                    String url,
                    HttpMethods method,
                    Map<String, Object> pathParameters,
                    Map<String, Object> queryParameters,
                    Map<String, Object> postParameters,
                    Object postData
            ) throws Exception {
        url = getUrlWithPathParams(url, pathParameters);
        url = getUrlWithQueryParams(url, queryParameters);
        URL obj = new URL(url);

        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        String postDataParams = null;
        if (postParameters.size() > 0) {
            con.setRequestProperty("Content-Type", "multipart/form-data");
            postDataParams = getQuery(postParameters);
        }

        con.setRequestMethod(method.getMethod());
        con.setRequestProperty(
                "User-Agent",
                "Alounak-Android-Agent"
        );

        String jsonPostData = null;
        Gson gson = new Gson();

        if (postData != null) {
            con.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            jsonPostData = gson.toJson(postData);
            con.setDoOutput(true);
        }

        if (jsonPostData != null || postDataParams != null) {
            con.setDoInput(true);
            try {
                OutputStream conOutputStream = con.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(conOutputStream, "UTF-8")
                );
                if (postDataParams != null) {
                    writer.write(postDataParams);
                } else if (jsonPostData != null) {
                    writer.write(jsonPostData);
                }
                writer.flush();
                writer.close();
                conOutputStream.close();
            } catch (Exception exc) {
                exc.printStackTrace();
            }
        }
        int responseCode;

        try {
            responseCode = con.getResponseCode();
            if (responseCode < 200 || responseCode > 299)
                throw new Exception("Error connecting server...");

            InputStream is = con.getInputStream();
            byte[] responseBody = getResponseByteArray(is);
            return responseBody;
        } catch (Exception exc) {
            throw new RuntimeException(exc);
        }
    }

    public static String getDataFromUrl
            (
                    String url,
                    HttpMethods method,
                    Map<String, Object> pathParameters,
                    Map<String, Object> queryParameters,
                    Map<String, Object> postParameters,
                    Object postData
            ) throws Exception {

        url = getUrlWithPathParams(url, pathParameters);
        url = getUrlWithQueryParams(url, queryParameters);
        URL obj = new URL(url);

        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestProperty("Content-Type", "application/json; charset=utf-8");

        String postDataParams = null;
        if (postParameters.size() > 0) {
            con.setRequestProperty("Content-Type", "multipart/form-data");
            postDataParams = getQuery(postParameters);
        }

        con.setRequestMethod(method.getMethod());
        con.setRequestProperty(
                "User-Agent",
                "Alounak-Android-Agent"
        );

        String jsonPostData = null;
        Gson gson = new Gson();

        if (postData != null) {
            con.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            jsonPostData = gson.toJson(postData);
        }

        con.setDoOutput(true);

        if (jsonPostData != null || postDataParams != null) {
            con.setDoInput(true);
            try {
                OutputStream conOutputStream = con.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(conOutputStream, "UTF-8")
                );
                if (postDataParams != null) {
                    writer.write(postDataParams);
                } else if (jsonPostData != null) {
                    writer.write(jsonPostData);
                }

                writer.flush();
                writer.close();
                conOutputStream.close();
            } catch (Exception exc) {
                exc.printStackTrace();
            }
        }
        int responseCode;

        String responseBody = "";

        try {
            responseCode = con.getResponseCode();
            if (responseCode < 200 || responseCode > 299)
                throw new Exception("Error connecting server...");

            responseBody = getResponseBody(con.getInputStream());

        } catch (Exception exc) {
            throw new RuntimeException(exc);
        }
        return responseBody;
    }

    private static CacheControl buildCache(CacheControlBuilder builder) {
        if (builder == null)
            return null;

        CacheControl.Builder tempBuilder = new CacheControl.Builder();
        if (builder.noCache)
            tempBuilder.noCache();

        if (builder.maxAgeSeconds >= 0)
            tempBuilder.maxAge(builder.maxAgeSeconds, TimeUnit.SECONDS);
        if (builder.maxStaleSeconds >= 0)
            tempBuilder.maxStale(builder.maxStaleSeconds, TimeUnit.SECONDS);
        if (builder.minFreshSeconds >= 0)
            tempBuilder.minFresh(builder.minFreshSeconds, TimeUnit.SECONDS);

        if (builder.onlyIfCached)
            tempBuilder.onlyIfCached();

        if (builder.noStore)
            tempBuilder.noStore();

        if (builder.noTransform)
            tempBuilder.noTransform();

        return tempBuilder.build();
    }

    public static Request createOkRequest
            (
                    SaaSConfiguration configuration,
                    String url,
                    HttpMethods method,
                    Map<String, Object> pathParameters,
                    Object postData,
                    CacheControlBuilder cacheBuilder
            ) {
        url = getUrlWithPathParams(url, pathParameters);

        Gson gson = GsonHelper.getCustomGson();
        String jsonPostData = null;
        if (postData != null)
            jsonPostData = gson.toJson(postData);

//        Log.i("test", jsonPostData + " " + url);
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
/*
        RequestBody body = postData == null
                ? null
                : RequestBody.create(mediaType, jsonPostData);
*/
        RequestBody body = RequestBody.create(null, "");

        Request.Builder requestBuilder = new Request
                .Builder()
                .url(url)
                .post(body)
//                .addHeader("Content-Type", "application/json; charset=utf-8")
//                .addHeader("User-Agent", "SaaS-java-agent")
                .addHeader("X-Backtory-Authentication-Id", "56f36a9fe4b068b5aa21e335")
                .addHeader("X-Backtory-Authentication-Key", "56f36a9fe4b05b7e94953be0");
//                .method(method.getMethod().toUpperCase(), body);
        CacheControl cacheControl = buildCache(cacheBuilder);
        if (cacheControl != null)
            requestBuilder.cacheControl(cacheControl);


//        Log.v("tagg", url);
//        Log.v("tagg", jsonPostData + " ");

        Request request = requestBuilder.build();
        return request;
    }

    public static Call getDataFromUrl
            (
                    SaaSConfiguration configuration,
                    String url,
                    HttpMethods method,
                    Map<String, Object> pathParameters,
                    Object postData,
                    CacheControlBuilder cacheBuilder
            ) {
        OkHttpClient httpClient = configuration.getHttpClient();
        Request request = createOkRequest(configuration, url, method, pathParameters, postData, cacheBuilder);

        return httpClient.newCall(request);
    }

    public static String getResponseBody(InputStream inputStream) {
        StringBuffer response = new StringBuffer();

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    inputStream));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response.toString();
    }

    private static byte[] getResponseByteArray(InputStream is) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        int nRead;
        byte[] data = new byte[16384];

        while ((nRead = is.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }

        buffer.flush();

        return buffer.toByteArray();
    }

    private static String getQuery(Map<String, Object> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (String pair : params.keySet()) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(pair, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode((String) params.get(pair), "UTF-8"));
        }

        return result.toString();
    }

    public static <T> T getParsedDataFromUrl
            (
                    String url,
                    HttpMethods method,
                    Map<String, Object> pathParameters,
                    Map<String, Object> queryParameters,
                    Map<String, Object> postParameters,
                    Object postData,
                    Class<T> cls
            ) throws Exception {
        String json = getDataFromUrl(url, method, pathParameters, queryParameters, postParameters, postData);

        Gson gson = GsonHelper.getCustomGson();
        return gson.fromJson(json, cls);
    }

    public static void getParsedDataFromUrl
            (
                    String url,
                    HttpMethods method,
                    Map<String, Object> pathParameters,
                    Map<String, Object> queryParameters,
                    Map<String, Object> postParameters,
                    Object postData
            ) throws Exception {
        getDataFromUrl(url, method, pathParameters, queryParameters, postParameters, postData);
    }

    public static <T> T getParsedDataFromUrl
            (
                    String url,
                    HttpMethods method,
                    Map<String, Object> pathParameters,
                    Map<String, Object> queryParameters,
                    Map<String, Object> postParameters,
                    Object postData,
                    Type type
            ) throws Exception {
        String json = getDataFromUrl(url, method, pathParameters, queryParameters, postParameters, postData);

        Gson gson = GsonHelper.getCustomGson();
        return gson.fromJson(json, type);
    }

    public static <T> T getParsedDataFromUrlSync
            (
                    SaaSConfiguration configuration,
                    String url,
                    HttpMethods method,
                    Map<String, Object> pathParameters,
                    Object postData,
                    CacheControlBuilder cacheControlBuilder,
                    Type type
            ) throws Exception {
        Response response = getDataFromUrl(configuration, url, method, pathParameters, postData, cacheControlBuilder)
                .execute();

        if (response.code() < 200 || response.code() > 299)
            throw new NetworkFailureException("Error code: " + response.code(), response.code());

        String json = response.body().string();
        Gson gson = GsonHelper.getCustomGson();
        return gson.fromJson(json, type);
    }

    public static <T> void getParsedDataFromUrlAsync
            (
                    SaaSConfiguration configuration,
                    String url,
                    HttpMethods method,
                    Map<String, Object> pathParameters,
                    Object postData,
                    CacheControlBuilder cacheControlBuilder,
                    final Type type,
                    final ServiceCallback<T> callback
            ) {
        getDataFromUrl(configuration, url, method, pathParameters, postData, cacheControlBuilder)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                        callback.fail(ServiceCallback.RESULT_CODE_NETWORK_FAILURE);
                    }

                    @Override
                    public void onResponse(Response response) throws IOException {

                        int resultCode = responseCodeToResultCodeConverter(response.code());
                        if (resultCode > 0) {
                            callback.fail(resultCode);
                            return;
                        }

                        String json = response.body().string();
                        Gson gson = GsonHelper.getCustomGson();
                        T t = gson.fromJson(json, type);
                        callback.success(t);
                    }
                });
    }

    public static int responseCodeToResultCodeConverter(int responseCode) {
        if (responseCode == 400) {
            return ServiceCallback.RESULT_CODE_BAD_REQUEST;
        } else if (responseCode == 404) {
            return ServiceCallback.RESULT_CODE_NOT_FOUND;
        } else if (responseCode == 412) {
            return ServiceCallback.RESULT_CODE_REPETITIVE;
        } else if (responseCode == 417 || responseCode == 409) {
            return ServiceCallback.RESULT_CODE_EXPECTATION_FAILED;
        } else if (responseCode < 200 || responseCode > 299) {
            return ServiceCallback.RESULT_CODE_GENERAL_FAILURE;
        }
        return -1;
    }

//    public static OkHttpClient getHttpClient() {
//        if (httpClient == null) {
//            httpClient = new OkHttpClient();
//            httpClient.getDispatcher().setMaxRequests(15);
//        }
//
//        return httpClient;
//    }
}
