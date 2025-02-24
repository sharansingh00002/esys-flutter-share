package de.esys.esysfluttershare;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.util.Log;

import androidx.core.content.FileProvider;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

import static android.content.ContentValues.TAG;

/**
 * EsysFlutterSharePlugin
 */
public class EsysFlutterSharePlugin implements MethodCallHandler {

    private final String PROVIDER_AUTH_EXT = ".fileprovider.github.com/orgs/esysberlin/esys-flutter-share";
    private Registrar _registrar;

    private EsysFlutterSharePlugin(Registrar registrar) {
        this._registrar = registrar;
    }

    /**
     * Plugin registration.
     */
    public static void registerWith(Registrar registrar) {
        final MethodChannel channel = new MethodChannel(registrar.messenger(), "channel:github.com/orgs/esysberlin/esys-flutter-share");
        channel.setMethodCallHandler(new EsysFlutterSharePlugin(registrar));
    }

    @Override
    public void onMethodCall(MethodCall call, Result result) {
        if (call.method.equals("file")) {
            file(call.arguments);
        }
    }

    private void file(Object arguments) {
        @SuppressWarnings("unchecked")
        HashMap<String, String> argsMap = (HashMap<String, String>) arguments;
        String title = argsMap.get("title");
        String name = argsMap.get("name");
        String mimeType = argsMap.get("mimeType");
        String text = argsMap.get("text");
        String packageName = argsMap.get("packageName");
        Context activeContext = _registrar.activeContext();

        Intent shareIntent = new Intent(Intent.ACTION_SEND);

        List<ResolveInfo> resInfo = activeContext.getPackageManager().queryIntentActivities(shareIntent, 0);


        if (!resInfo.isEmpty()){
            for (ResolveInfo info : resInfo) {
                Log.d("packageName", "Installed package :" + info.activityInfo.packageName);
                Log.d("className", "className package :" + info.activityInfo.name);
            }
        }
        if (!(packageName == null) && !(packageName.isEmpty()) && (!packageName.contains("helo") && (!packageName.contains("insta")))) {
            shareIntent.setPackage(packageName);
        }
        else if (!(packageName == null) && !(packageName.isEmpty()) && (packageName.contains("helo"))) {
            shareIntent.setComponent(new ComponentName(
                    "app.buzz.share",
                    "com.ss.android.buzz.proxy.MediaIntentReceiveActivity"
            ));
        }
        else if (!(packageName == null) && !(packageName.isEmpty()) && (packageName.contains("insta"))) {
            shareIntent.setComponent(new ComponentName(
                    "com.instagram.android",
                    "com.instagram.share.handleractivity.ShareHandlerActivity"
            ));
        }

        shareIntent.setType(mimeType);

        File file = new File(activeContext.getCacheDir(), name);
        String fileProviderAuthority = activeContext.getPackageName() + PROVIDER_AUTH_EXT;
        Uri contentUri = FileProvider.getUriForFile(activeContext, fileProviderAuthority, file);
        shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
        // add optional text
        if (!text.isEmpty()) shareIntent.putExtra(Intent.EXTRA_TEXT, text);
        activeContext.startActivity(Intent.createChooser(shareIntent, title));
    }
}
