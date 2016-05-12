package ir.pegahtech.connectivity;

import com.google.gson.Gson;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

import ir.pegahtech.backtory.conf.SaaSConfiguration;
import ir.pegahtech.backtory.http.GsonHelper;
import ir.pegahtech.backtory.http.ServiceCallback;
import ir.pegahtech.backtory.models.TokenObject;
import ir.pegahtech.backtory.models.LogInRequest;

/**
 * Created by Mohammad on 5/2/16 AD.
 */
public class Authentication {

    public static void login(final SaaSConfiguration configuration, final LogInRequest logInRequest,
                             final ServiceCallback<TokenObject> callback) {

        OkHttpClient httpClient = configuration.getHttpClient();
        RequestBody requestBody = RequestBody.create(null, "");

        HttpUrl url = new HttpUrl.Builder()
                .scheme("http")
                .host(CommonConfiguration.AUTH_BASE_URL)
//                .port(8045)
                .addPathSegment("api")
                .addPathSegment("auth")
                .addPathSegment("login")
                .addQueryParameter("username", logInRequest.getUsername())
                .addQueryParameter("password", logInRequest.getPassword())
                .build();

        Request request = new Request
                .Builder()
                .url(url)
                .post(requestBody)
                .addHeader("X-Backtory-Authentication-Id", "57163ee5e4b0cad8c4dd1844")
                .addHeader("X-Backtory-Authentication-Key", "57163ee5e4b093ed2821a011")
//                .addHeader("X-Backtory-Authentication-Id", "56f36a9fe4b068b5aa21e335")
//                .addHeader("X-Backtory-Authentication-Key", "56f36a9fe4b05b7e94953be0")
                .build();

        Call newCall = httpClient.newCall(request);
        newCall.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                callback.fail(0);
            }

            @Override
            public void onResponse(Response response) throws IOException, RuntimeException {
                if (response.code() != 200) {
                    callback.fail(response.code());
                    return;
                }

                String json = response.body().string();
                Gson gson = GsonHelper.getCustomGson();
                TokenObject tokenObject = gson.fromJson(json, TokenObject.class);
                callback.success(tokenObject);
            }
        });
    }

}
