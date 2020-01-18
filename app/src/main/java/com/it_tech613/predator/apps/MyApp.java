package com.it_tech613.predator.apps;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.exoplayer2.offline.DownloadAction;
import com.google.android.exoplayer2.offline.DownloadManager;
import com.google.android.exoplayer2.offline.DownloaderConstructorHelper;
import com.google.android.exoplayer2.offline.ProgressiveDownloadAction;
import com.google.android.exoplayer2.source.dash.offline.DashDownloadAction;
import com.google.android.exoplayer2.source.hls.offline.HlsDownloadAction;
import com.google.android.exoplayer2.source.smoothstreaming.offline.SsDownloadAction;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.FileDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.upstream.cache.Cache;
import com.google.android.exoplayer2.upstream.cache.CacheDataSource;
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.NoOpCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;
import com.google.android.exoplayer2.util.Util;
import com.it_tech613.predator.BuildConfig;
import com.it_tech613.predator.models.CategoryModel;
import com.it_tech613.predator.models.EPGChannel;
import com.it_tech613.predator.models.FirstServer;
import com.it_tech613.predator.models.FullModel;
import com.it_tech613.predator.models.LoginModel;
import com.it_tech613.predator.models.MovieModel;
import com.it_tech613.predator.models.SeasonModel;
import com.it_tech613.predator.models.SeriesModel;
import com.it_tech613.predator.utils.DownloadTracker;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import iptvclient.ApiClient;
import iptvclient.Iptvclient;

public class MyApp extends Application {
    public static SeasonModel selectedSeasonModel;
    public static EPGChannel selectedChannel;
    private MyPreference preference;
    public static MyApp instance;
    public static LoginModel loginModel;
    public static List<FullModel> fullModels = new ArrayList<>();
    public static List<CategoryModel> vod_categories = new ArrayList<>();
    public static List<CategoryModel> live_categories = new ArrayList<>();
    public static List<CategoryModel> series_categories = new ArrayList<>();
    public static List<FullModel> fullModels_filter = new ArrayList<>();
    public static List<CategoryModel> vod_categories_filter = new ArrayList<>();
    public static List<CategoryModel> live_categories_filter = new ArrayList<>();
    public static List<CategoryModel> series_categories_filter = new ArrayList<>();

    public static List<MovieModel> movieModels = new ArrayList<>();
    public static List<MovieModel> favMovieModels = new ArrayList<>();
    public static List<MovieModel> recentMovieModels = new ArrayList<>();

    public static List<SeriesModel> seriesModels = new ArrayList<>();
    public static List<SeriesModel> favSeriesModels = new ArrayList<>();
    public static List<SeriesModel> recentSeriesModels = new ArrayList<>();

    public static SeriesModel selectedSeriesModel;
    public static List<MovieModel> subMovieModels = new ArrayList<>();
    public static MovieModel vod_model;

    public static int num_screen=-1;
    public static int num_server = 1;
    public static List<String> maindatas;
    public static boolean is_local = false;
    public static boolean is_mac = false;

    public static Map backup_map;
    public static String version_name,mac_address,version_str,user,pass,created_at,status,is_trail,active_cons,max_cons,time_zone;
    public static int SCREEN_WIDTH, SCREEN_HEIGHT, ITEM_V_WIDTH, ITEM_V_HEIGHT,SURFACE_WIDTH,SURFACE_HEIGHT,top_margin,right_margin,
            channel_size,EPG_WIDTH,EPG_HEIGHT,EPG_TOP,EPG_RIGHT;
    public static boolean is_first,is_vpn = false;

    public static FirstServer firstServer= FirstServer.first;
    public static boolean is_video_played = false;

    private static final String DOWNLOAD_ACTION_FILE = "actions";
    private static final String DOWNLOAD_TRACKER_ACTION_FILE = "tracked_actions";
    private static final String DOWNLOAD_CONTENT_DIRECTORY = "downloads";
    private static final int MAX_SIMULTANEOUS_DOWNLOADS = 2;
    private static final DownloadAction.Deserializer[] DOWNLOAD_DESERIALIZERS =
            new DownloadAction.Deserializer[] {
                    DashDownloadAction.DESERIALIZER,
                    HlsDownloadAction.DESERIALIZER,
                    SsDownloadAction.DESERIALIZER,
                    ProgressiveDownloadAction.DESERIALIZER
            };

    protected String userAgent;

    private File downloadDirectory;
    private Cache downloadCache;
    private DownloadManager downloadManager;
    private DownloadTracker downloadTracker;
    private ApiClient iptvclient;
    private KProgressHUD kpHUD;

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
        preference = new MyPreference(getApplicationContext(), Constants.APP_INFO);
        getScreenSize();
        getTimeZone();
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        String p = pref.getString("set_locale", "");
        if (!p.equals("")) {
            Locale locale;
            // workaround due to region code
            if(p.equals("zh-TW")) {
                locale = Locale.TRADITIONAL_CHINESE;
            } else if(p.startsWith("zh")) {
                locale = Locale.CHINA;
            } else if(p.equals("pt-BR")) {
                locale = new Locale("pt", "BR");
            } else if(p.equals("bn-IN") || p.startsWith("bn")) {
                locale = new Locale("bn", "IN");
            } else {
                /**
                 * Avoid a crash of
                 * java.lang.AssertionError: couldn't initialize LocaleData for locale
                 * if the user enters nonsensical region codes.
                 */
                if(p.contains("-"))
                    p = p.substring(0, p.indexOf('-'));
                locale = new Locale(p);
            }
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getResources().updateConfiguration(config,
                    getResources().getDisplayMetrics());
        }

        instance = this;
        iptvclient = Iptvclient.newApiClient();
        userAgent = Util.getUserAgent(this, "ExoPlayerDemo");
    }

    private void getTimeZone(){
        TimeZone tz = TimeZone.getDefault();
        time_zone = tz.getID();
        Log.e("time", String.valueOf(time_zone));
    }

    public void setKpHUD(Activity activity) {
        kpHUD = KProgressHUD.create(activity)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setCancellable(true)
//                .setLabel("Login")
                .setAnimationSpeed(1)
                .setDimAmount(0.5f);
    }

    public KProgressHUD getKpHUD(){
        return kpHUD;
    }

    private void getScreenSize() {
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        SCREEN_WIDTH = displayMetrics.widthPixels;
        SCREEN_HEIGHT = displayMetrics.heightPixels;
        if(SCREEN_WIDTH < SCREEN_HEIGHT){
            int a = SCREEN_WIDTH;
            SCREEN_WIDTH = SCREEN_HEIGHT;
            SCREEN_HEIGHT = a;
        }
        SURFACE_WIDTH = (int)(SCREEN_WIDTH/3);
        SURFACE_HEIGHT = (int)(SURFACE_WIDTH*0.65);
        top_margin = SCREEN_HEIGHT/7;
        right_margin = SCREEN_WIDTH/14;
        ITEM_V_WIDTH = (int) (SCREEN_WIDTH /8);
        ITEM_V_HEIGHT = (int) (ITEM_V_WIDTH * 1.6);

        EPG_WIDTH = (int)(SCREEN_WIDTH/4);
        EPG_HEIGHT = (int)(EPG_WIDTH*0.65);
        EPG_TOP = SCREEN_HEIGHT/8;
        EPG_RIGHT = SCREEN_WIDTH/20;
    }

    public void loadVersion() {
        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        version_name = pInfo.versionName;
    }

    public void versionCheck(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    public MyPreference getPreference() {
        return preference;
    }

    /** Returns a {@link DataSource.Factory}. */
    public DataSource.Factory buildDataSourceFactory(TransferListener<? super DataSource> listener) {
        DefaultDataSourceFactory upstreamFactory =
                new DefaultDataSourceFactory(this, listener, buildHttpDataSourceFactory(listener));
        return buildReadOnlyCacheDataSource(upstreamFactory, getDownloadCache());
    }

    /** Returns a {@link HttpDataSource.Factory}. */
    public HttpDataSource.Factory buildHttpDataSourceFactory(
            TransferListener<? super DataSource> listener) {
        return new DefaultHttpDataSourceFactory(userAgent, listener);
    }

    /** Returns whether extension renderers should be used. */
    public boolean useExtensionRenderers() {
        return "withExtensions".equals(BuildConfig.FLAVOR);
    }

    public DownloadManager getDownloadManager() {
        initDownloadManager();
        return downloadManager;
    }

    public DownloadTracker getDownloadTracker() {
        initDownloadManager();
        return downloadTracker;
    }

    private synchronized void initDownloadManager() {
        if (downloadManager == null) {
            DownloaderConstructorHelper downloaderConstructorHelper =
                    new DownloaderConstructorHelper(
                            getDownloadCache(), buildHttpDataSourceFactory(/* listener= */ null));
            downloadManager =
                    new DownloadManager(
                            downloaderConstructorHelper,
                            MAX_SIMULTANEOUS_DOWNLOADS,
                            DownloadManager.DEFAULT_MIN_RETRY_COUNT,
                            new File(getDownloadDirectory(), DOWNLOAD_ACTION_FILE),
                            DOWNLOAD_DESERIALIZERS);
            downloadTracker =
                    new DownloadTracker(
                            /* context= */ this,
                            buildDataSourceFactory(/* listener= */ null),
                            new File(getDownloadDirectory(), DOWNLOAD_TRACKER_ACTION_FILE),
                            DOWNLOAD_DESERIALIZERS);
            downloadManager.addListener(downloadTracker);
        }
    }

    private synchronized Cache getDownloadCache() {
        if (downloadCache == null) {
            File downloadContentDirectory = new File(getDownloadDirectory(), DOWNLOAD_CONTENT_DIRECTORY);
            downloadCache = new SimpleCache(downloadContentDirectory, new NoOpCacheEvictor());
        }
        return downloadCache;
    }

    private File getDownloadDirectory() {
        if (downloadDirectory == null) {
            downloadDirectory = getExternalFilesDir(null);
            if (downloadDirectory == null) {
                downloadDirectory = getFilesDir();
            }
        }
        return downloadDirectory;
    }

    private static CacheDataSourceFactory buildReadOnlyCacheDataSource(
            DefaultDataSourceFactory upstreamFactory, Cache cache) {
        return new CacheDataSourceFactory(
                cache,
                upstreamFactory,
                new FileDataSourceFactory(),
                /* cacheWriteDataSinkFactory= */ null,
                CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR,
                /* eventListener= */ null);
    }

    public ApiClient getIptvclient() {
        return iptvclient;
    }
}