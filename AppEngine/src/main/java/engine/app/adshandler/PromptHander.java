package engine.app.adshandler;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;

import app.pnd.adshandler.R;
import engine.app.PrintLog;
import engine.app.server.v2.DataHubConstant;
import engine.app.server.v2.Slave;

public class PromptHander {

    void checkForNormalUpdate(Context context) {
        PrintLog.print("checking " + Slave.UPDATES_updateTYPE);
        if (Slave.UPDATES_updateTYPE.equals(Slave.IS_NORMAL_UPDATE)) {

            if (!getCurrentAppVersion(context).equals(
                    Slave.UPDATES_version)) {
                if (DataHubConstant.APP_LAUNCH_COUNT % 3 == 0) {
                    openNormalupdateAlert(context, Slave.UPDATES_prompttext, false);
                }

            }
        }

    }

    void checkForForceUpdate(Context context) {
        try {
            if (Slave.UPDATES_updateTYPE.equals(Slave.IS_FORCE_UPDATE)) {

                if (!getCurrentAppVersion(context).equals(
                        Slave.UPDATES_version)) {

                    openNormalupdateAlert(context, Slave.UPDATES_prompttext, true);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String getCurrentAppVersion(Context context) {
        try {
            PackageInfo pInfo;
            pInfo = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            int verCode = pInfo.versionCode;

            return "" + verCode;

        } catch (Exception e) {
            return "100";
        }

    }

    private void openNormalupdateAlert(final Context context, String msg, final boolean is_force) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        alertDialogBuilder.setTitle("Update App");
        alertDialogBuilder.setIcon(R.drawable.app_icon);
        alertDialogBuilder.setMessage(msg);
        // set positive button: Yes message
        alertDialogBuilder.setPositiveButton("UPDATE NOW",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(Slave.UPDATES_appurl));
                        context.startActivity(i);
                        dialog.cancel();
                        if (is_force) {
                            ((Activity) context).finish();
                        }
                    }
                });
        // set negative button: No message
        alertDialogBuilder.setNegativeButton("NO THANKS!",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // cancel the alert box and put a Toast to the user
                        dialog.cancel();
                        if (is_force) {
                            ((Activity) context).finish();
                        }

                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        // show alert
        alertDialog.show();
        if (is_force) {
            alertDialog.setCancelable(false);
        } else {
            alertDialog.setCancelable(true);
        }
    }

}
