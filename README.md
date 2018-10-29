## 使用
1.导入
- 将 tcraftlib文件夹复制到项目根目录
```
    build.gradle、config.gradle、.gitignore 也复制到根目录
    app 的 build.gradle 参照demo复制
    复制 gradle.properties 的内容，根目录建文件夹 keystore，并添加签名文件

```
- settings.gradle
```
	include ':app', ':tcraftlib'
```

- gradle 中 添加
```
	dependencies {
		...
		implementation project(':tcraftlib')

	}
```

- 新建 MyApplication 类继承 TCraft，并在 AndroidManifest.xml 中设置为 application，并对 tcraftlib 进行初始化
```
    <application
       android:name=".app.MyApplication"
       ...
       >
        ...
     </application>
```
```
    public void onCreate() {
        super.onCreate();
		TLibManager.init(...)
    }
```
- config.gradle 修改
```
    applicationId    : "com.tincher.tcraft",
    applicationName  : "TCraft",
    providerName     : "com.tincher.tcraftlib.fileProvider",
```


- 根据项目具体情况，更改 tcraftlib 中以下内容
  - 增加或更改 AndroidManifest.xml 中的基础权限,

    ```
        <uses-permission android:name="android.permission.INTERNET" />
        <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
        <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
        <uses-permission android:name="android.permission.CHANGE_NETWORK_STATE" />
        <uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />
        <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
    ```

  - 增加或更改 PermissionConfig 中的检测的基础权限

    ```
        /**
         * 全局的权限列表，默认每个 Activity 都会检测
         * 此处的权限必须在 AndroidManifest 中声明，
         */
        public static final String[] permissions = {
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
        };
    ```

    ​

2.依赖库

目前已集成以下库，无需再次配置，可直接使用

```
    api rootProject.ext.supportDependencies.supportAppcompat
    api rootProject.ext.supportDependencies.supportV4
    api rootProject.ext.supportDependencies.supportDesign
    api rootProject.ext.supportDependencies.supportConstraintLayout

    debugApi rootProject.ext.baseDependencies.debugLeakcanary
    releaseApi rootProject.ext.baseDependencies.releaseLeakcanary

    debugApi rootProject.ext.baseDependencies.debugChuck
    releaseApi rootProject.ext.baseDependencies.releaseChuck

    api rootProject.ext.baseDependencies.retrofit
    api rootProject.ext.baseDependencies.adapterRxjava2
    api rootProject.ext.baseDependencies.retrofitGson
    api rootProject.ext.baseDependencies.rxandroid
    api rootProject.ext.baseDependencies.rxlifecycle2
    api rootProject.ext.baseDependencies.rxlifecycleComponents
    
    api rootProject.ext.extensionDependencies.utilcode
    api rootProject.ext.extensionDependencies.baseQuickAdapter
```

3.开发

demo 会在 app module 下陆续添加

## Todo

- lib 初始化：TLibManager
  - 各种路径，baseURL等
- TCraft 中依赖库的初始化