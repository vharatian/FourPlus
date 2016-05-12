package ir.pegahtech.connectivity;

import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import ir.pegahtech.backtory.conf.SaaSConfiguration;
import ir.pegahtech.backtory.conf.SaaSDataProvider;
import ir.pegahtech.backtory.conf.SaaSPostProcess;
import ir.pegahtech.backtory.conf.SaaSServiceInfo;

public class WebSocketConfiguration implements SaaSConfiguration {
    //    public static final String serviceRootUrl = "http://31.25.89.72:48000/ContentServices_WAR/";
//    public static final String serviceRootUrl = "http://31.25.89.72:49000/ContentServices_Test/";
//    public static final String serviceRootUrl = "http://192.168.0.123:25034/";
    public static final String serviceRootUrl = "http://192.168.99.100:8045/";
    public static final String fileTableId = "46fb8ff8-c397-487b-a74d-7a7ccc2c5ed3";
    public static final String chatServiceUrl = "185.4.29.138";
    public static final String chatServiceName = "billboard";
    public static final int chatServicePort = 5222;

    private static WebSocketConfiguration _instance = new WebSocketConfiguration();

    private WebSocketConfiguration() {
    }

    private static SaaSDataProvider dataProvider;
    private static OkHttpClient httpClient;
    private List<SaaSPostProcess> postProcesses = new ArrayList<>();

    @Override
    public SaaSDataProvider getDataProvider() {
        return dataProvider;
    }

    public static void initialize(File cacheDirectory, SaaSDataProvider dp) {
        dataProvider = dp;
        httpClient = new OkHttpClient();
        httpClient.getDispatcher().setMaxRequests(15);
        httpClient.setCache(new Cache(cacheDirectory, 1024 * 1024 * 10));
//        RefreshTokenInterceptor refreshTokenInterceptor = new RefreshTokenInterceptor(instance());
//        httpClient.interceptors().add(refreshTokenInterceptor);
    }

    @Override
    public UUID getFileTableId() {
        return fileTableId == null || fileTableId.trim().equals("") ? null : UUID.fromString(fileTableId);
    }

    @Override
    public String getApiBaseUrl() {
        return serviceRootUrl;
    }

    //@Overrideه
    //public String getSecretKey() {
    //    return "";
    //}
    @Override
    public OkHttpClient getHttpClient() {
        return httpClient;
    }

    public static WebSocketConfiguration instance() {
        return _instance;
    }

    @Override
    public UUID getSchemaId() {
        return UUID.fromString("c4caeec7-cd94-4177-a535-35e7d1b15fdd");
    }

    @Override
    public String getLoggedInUsername() {
        return dataProvider.keyExists("SAAS_user_name")
                ? dataProvider.load("SAAS_user_name")
                : null;
    }

    @Override
    public String getLoggedInUserId() {
        return dataProvider.keyExists("SAAS_user_id")
                ? dataProvider.load("SAAS_user_id")
                : null;
    }

    @Override
    public void addPostProcess(SaaSPostProcess postProcess) {
        for (SaaSPostProcess process : postProcesses)
            if (process.getProcessId().equals(postProcess.getProcessId()))
                throw new RuntimeException("Duplicate process with same id: " + postProcess.getProcessId());

        postProcesses.add(postProcess);
    }

    @Override
    public void removePostProcess(String processId) {
        Iterator<SaaSPostProcess> iterator = postProcesses.iterator();
        while (iterator.hasNext()) {
            SaaSPostProcess next = iterator.next();
            if (next.getProcessId().equals(processId))
                iterator.remove();
        }
    }

    public void serviceStarted(Boolean isAsync, String className, String methodName, String url, String tableId, String schemaId) {
        for (SaaSPostProcess process : postProcesses)
            process.serviceStarted(new SaaSServiceInfo(isAsync, className, methodName, url, tableId, schemaId));
    }

    public void serviceFailed(Boolean isAsync, String className, String methodName, String url, String tableId, String schemaId) {
        for (SaaSPostProcess process : postProcesses)
            process.serviceFailed(new SaaSServiceInfo(isAsync, className, methodName, url, tableId, schemaId));
    }

    public void serviceSucceed(Boolean isAsync, String className, String methodName, String url, String tableId, String schemaId) {
        for (SaaSPostProcess process : postProcesses)
            process.serviceSucceed(new SaaSServiceInfo(isAsync, className, methodName, url, tableId, schemaId));
    }
}
