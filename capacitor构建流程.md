
---

# 前端项目打包为Android项目流程

## 一、环境准备与项目初始化

1. 使用VS Code或相应编辑器打开前端项目。
2. 进入Terminal操作面板。
3. 如未安装Capacitor，执行以下命令安装：
   ```bash
   npm install @capacitor/core @capacitor/cli @capacitor/device @capacitor/android
   ```

4. 初始化Capacitor配置，执行：
   ```bash
   npx cap init [你的应用名] [包名]
   ```
   初始化完成后会生成`capacitor.config.ts`文件。

---

## 二、构建项目并添加Android平台

### 1. 构建Web项目
```bash
npm run build   # 或 yarn build，根据项目构建命令调整
```

### 2. 添加Android平台支持
```bash
npx cap add android
```

### 3. 同步项目到Android平台
```bash
npx cap sync android
```

执行完毕后，会在根目录生成`android`文件夹，用Android Studio打开该文件夹。

---

## 三、Android项目配置调整注意事项

> 自动生成的Android项目版本可能过高，需根据实际情况调整以下配置：

1. **Java版本**  
   默认可能使用Java 21，建议改为17。  
   在`gradle`文件中全局搜索`JavaVersion`，将其改为`17`。

2. **Gradle版本**
    - `gradle-wrapper.properties`文件中的gradle版本下载可能较慢，可手动下载对应版本替换。
    - 全局`build.gradle`文件中`classpath`版本可调整为8.0.2或兼容版本。

3. **Android SDK版本**  
   默认可能为35，建议改为34以避免编译错误。  
   修改`variables.gradle`中的：
   ```gradle
   compileSdkVersion = 34
   targetSdkVersion = 34
   ```
   同时将`androidxCoreVersion`从1.15.0降低至1.10.0。

4. **兼容性处理**  
   在`CapacitorWebView`中，有关`android35`的判断需注释掉（因当前使用34）。

---

## 四、常见问题与解决方案

### 1. 版本兼容问题
- 如安装`@capacitor/device`时提示需要Capacitor 8.0以上，可执行：
  ```bash
  npm install @capacitor/core@8 @capacitor/cli@8 @capacitor/android@8 @capacitor/device@8
  ```
- 检查插件版本：
  ```bash
  npm list @capacitor/core @capacitor/cli @capacitor/android @capacitor/device
  ```

### 2. `npx cap init`报错
如出现版本提示错误，可尝试：
```bash
capacitor init [应用名] [包名]
```

### 3. Node版本过低报错
如出现：
```
[fatal] The Capacitor CLI requires NodeJS >=22.0.0
```
原因：项目中自动生成的`node.exe`版本过低。  
解决：用本地安装的高版本Node替换项目中的`node.exe`。

### 4. 依赖版本冲突
#### a. Gradle版本
修改`gradle-wrapper.properties`，例如：
```
distributionUrl=https\://services.gradle.org/distributions/gradle-8.11.1-all.zip
```

#### b. Android依赖版本(Android Studio版本过低时需调整)
在`variables.gradle`中配置（示例）：
```gradle
ext {
    minSdkVersion = 24
    compileSdkVersion = 34
    targetSdkVersion = 34
    androidxActivityVersion = '1.7.1'
    androidxAppCompatVersion = '1.7.1'
    androidxCoordinatorLayoutVersion = '1.3.0'
    androidxCoreVersion = '1.10.0'
    androidxFragmentVersion = '1.7.1'
    coreSplashScreenVersion = '1.0.0'
    androidxWebkitVersion = '1.10.0'
    junitVersion = '4.13.2'
    androidxJunitVersion = '1.2.0'
    androidxEspressoCoreVersion = '3.7.0'
    cordovaAndroidVersion = '14.0.1'
}
```

#### c. Kotlin版本冲突
在根目录`build.gradle`中统一版本：
```gradle
buildscript {
    ext.kotlin_version = '1.8.22'
    dependencies {
        classpath 'com.android.tools.build:gradle:8.1.4'
        classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version"
    }
}

allprojects {
    configurations.all {
        resolutionStrategy {
            force "org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version"
            force "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlin_version"
            force "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlin_version"
            force "org.jetbrains.kotlin:kotlin-reflect:$kotlin_version"
        }
    }
}
```
---