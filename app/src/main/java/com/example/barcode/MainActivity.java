package com.example.barcode;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.github.arturogutierrez.Badges;
import com.github.arturogutierrez.BadgesNotSupportedException;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.leolin.shortcutbadger.ShortcutBadger;

import static androidx.core.app.NotificationCompat.BADGE_ICON_SMALL;

public class MainActivity extends AppCompatActivity {
    TextView TV_Header;
    Typeface font;
    ListView LV_Country;
    SimpleAdapter ADAhere;
    String TAG="Badges";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

            TV_Header=(TextView) findViewById(R.id.TV_Header);
            LV_Country=(ListView)findViewById(R.id.LV_Country);







        List<Map<String,String>> MyData = null;
        GetData mydata =new GetData();
        MyData= mydata.doInBackground();
        String[] fromwhere = { "item_desc" };

        int[] viewswhere = { R.id.lblcountryname};

        ADAhere = new SimpleAdapter(MainActivity.this, MyData,R.layout.itemplate, fromwhere, viewswhere);

        LV_Country.setAdapter(ADAhere);
        setBadge(MainActivity.this,2);
        ShortcutBadger.applyCount(MainActivity.this, 5);
        try {
            Badges.setBadge(MainActivity.this, 5);
        } catch (BadgesNotSupportedException badgesNotSupportedException) {
            Log.d(TAG, badgesNotSupportedException.getMessage());
        }




        LV_Country.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    HashMap<String,Object> obj=(HashMap<String,Object>)ADAhere.getItem(position);
                    String ID=(String)obj.get("item_desc");
                    Toast.makeText(MainActivity.this, ID, Toast.LENGTH_SHORT).show();
                    Bundle b = new Bundle();

                    // Storing data into bundle
                    b.putString("item_desc", ID);
                    Intent intent=new Intent(MainActivity.this,Main2Activity.class);
                    intent.putExtras(b);
                    startActivity(intent);
                }
            });






        }




    public static void setBadge(Context context, int count) {
        String launcherClassName = getLauncherClassName(context);
        if (launcherClassName == null) {
            return;
        }
        Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
        intent.putExtra("badge_count", count);
        intent.putExtra("badge_count_package_name", context.getPackageName());
        intent.putExtra("badge_count_class_name", launcherClassName);
        context.sendBroadcast(intent);
    }

    public static String getLauncherClassName(Context context) {

        PackageManager pm = context.getPackageManager();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : resolveInfos) {
            String pkgName = resolveInfo.activityInfo.applicationInfo.packageName;
            if (pkgName.equalsIgnoreCase(context.getPackageName())) {
                String className = resolveInfo.activityInfo.name;
                return className;
            }
        }
        return null;
    }

/* public void createNotification (View view) {
     count ++ ;
     Intent notificationIntent = new Intent(getApplicationContext() , MainActivity. class ) ;
     notificationIntent.putExtra( "fromNotification" , true ) ;
     notificationIntent.setFlags(Intent. FLAG_ACTIVITY_CLEAR_TOP | Intent. FLAG_ACTIVITY_SINGLE_TOP ) ;
     PendingIntent pendingIntent = PendingIntent. getActivity ( this, 0 , notificationIntent , 0 ) ;
     NotificationManager mNotificationManager = (NotificationManager) getSystemService( NOTIFICATION_SERVICE ) ;
     NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext() , default_notification_channel_id ) ;
     mBuilder.setContentTitle( "My Notification" ) ;
     mBuilder.setContentIntent(pendingIntent) ;
     mBuilder.setContentText( "Notification Listener Service Example" ) ;
     mBuilder.setSmallIcon(R.drawable. ic_launcher_foreground ) ;
     mBuilder.setAutoCancel( true ) ;
     mBuilder.setBadgeIconType( BADGE_ICON_SMALL ) ;
     mBuilder.setNumber( count ) ;
     if (android.os.Build.VERSION. SDK_INT >= android.os.Build.VERSION_CODES. O ) {
         int importance = NotificationManager. IMPORTANCE_HIGH ;
         NotificationChannel notificationChannel = new NotificationChannel( NOTIFICATION_CHANNEL_ID , "NOTIFICATION_CHANNEL_NAME" , importance) ;
         mBuilder.setChannelId( NOTIFICATION_CHANNEL_ID ) ;
         assert mNotificationManager != null;
         mNotificationManager.createNotificationChannel(notificationChannel) ;
     }
     assert mNotificationManager != null;
     mNotificationManager.notify(( int ) System. currentTimeMillis () , mBuilder.build()) ;
 }*/

}
