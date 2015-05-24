#android-log

##Introduction
android lib - log

##System requirements
Android 2.2+

##Permission requirements
```xml
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
```

##Dependencies
Apache commons-lang3   [http://commons.apache.org/proper/commons-lang/](http://commons.apache.org/proper/commons-lang/)

##Download
grab via Maven:

```xml
<dependency>
  <groupId>com.github.snowdream.android.util</groupId>
  <artifactId>log</artifactId>
  <version>1.2.0</version>
</dependency>
```

or Gradle:
```groovy
    compile 'com.github.snowdream.android.util:log:1.2.0'
```

##Docs
[http://snowdreamframework.github.io/android-log/docs/1.2.0/javadoc/](http://snowdreamframework.github.io/android-log/docs/1.2.0/javadoc/)

##Usage
1.enable/disable log  
```java
Log.setEnabled(true);  
Log.setEnabled(false);  
```

2.enable/disable log to console
```java
Log.setLog2ConsoleEnabled(true);
Log.setLog2ConsoleEnabled(false);
```

3.enable/disable log to file
```java
Log.setLog2FileEnabled(true);
Log.setLog2FileEnabled(false);
```

4.set the global Tag for the log
```java
Log.setGlobalTag("Android");
```

5.log simple
```java
Log.d("test");  
Log.v("test");  
Log.i("test");  
Log.w("test");  
Log.e("test");  
```

6.log simple -- set custom tag
```java
Log.d("TAG","test");  
Log.v("TAG","test");  
Log.i("TAG","test");  
Log.w("TAG","test");  
Log.e("TAG","test");  
```

7.log advance
```java
Log.d("test",new Throwable("test"));  
Log.v("test",new Throwable("test"));  
Log.i("test",new Throwable("test"));  
Log.w("test",new Throwable("test"));  
Log.e("test",new Throwable("test"));  
```

8.log advance  -- set custom tag
```java
Log.d("TAG","test",new Throwable("test"));  
Log.v("TAG","test",new Throwable("test"));  
Log.i("TAG","test",new Throwable("test"));  
Log.w("TAG","test",new Throwable("test"));  
Log.e("TAG","test",new Throwable("test"));  
```

9.Log to File

log into one file with FilePathGenerator
```java
Log.setFilePathGenerator(new FilePathGenerator.DefaultFilePathGenerator("/mnt/sdcard/","app",".log"));
//Log.setFilePathGenerator(new FilePathGenerator.DefaultFilePathGenerator(context,"app",".log"));
//Log.setFilePathGenerator(new FilePathGenerator.DateFilePathGenerator("/mnt/sdcard/","app",".log"));
//Log.setFilePathGenerator(new FilePathGenerator.LimitSizeFilePathGenerator("/mnt/sdcard/","app",".log",10240));

Log.d("test 1");
Log.v("test 2");
Log.i("test 3");
Log.w("test 4");
Log.e("test 5");
```

log into one file with LogFilter
```java
Log.setFilePathGenerator(new FilePathGenerator.DefaultFilePathGenerator("/mnt/sdcard/","app",".log"));

Log.addLogFilter(new LogFilter.LevelFilter(Log.LEVEL.DEBUG));
//Log.addLogFilter(new LogFilter.TagFilter(TAG));
//Log.addLogFilter(new LogFilter.ContentFilter(CUSTOM_TAG));


Log.d("test 1");
Log.v("test 2");
Log.i("test 3");
Log.w("test 4");
Log.e("test 5");
```

log into one file with LogFormatter
```java
Log.setFilePathGenerator(new FilePathGenerator.DefaultFilePathGenerator("/mnt/sdcard/","app",".log"));

Log.addLogFilter(new LogFilter.LevelFilter(Log.LEVEL.DEBUG));

Log.setLogFormatter(new LogFormatter.EclipseFormatter());
//Log.setLogFormatter(new LogFormatter.IDEAFormatter());

Log.d("test 1");
Log.v("test 2");
Log.i("test 3");
Log.w("test 4");
Log.e("test 5");
```

##License
```
Copyright (C) 2015 Snowdream Mobile <yanghui1986527@gmail.com>

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```