#android-app

##Introduction
android lib - app

##System requirements
Android 2.2+

##Dependencies
1. android-downloader [https://github.com/snowdream/android-downloader](https://github.com/snowdream/android-downloader)
2. android-autoupdater [https://github.com/snowdream/android-autoupdater](https://github.com/snowdream/android-autoupdater)   
Check this for more informaion. [build.gradle](https://github.com/snowdream/android-app/blob/master/lib/build.gradle)


##Download
Download [the latest aar][1] and all the libraries in the file named build.gradle.   
[https://github.com/snowdream/android-app/blob/master/lib/build.gradle](https://github.com/snowdream/android-app/blob/master/lib/build.gradle)

or grab via Maven:

```xml
<dependency>
  <groupId>com.github.snowdream.android</groupId>
  <artifactId>app</artifactId>
  <version>0.1</version>
  <type>aar</type>
</dependency>
```

or Gradle:
```groovy
    compile 'com.github.snowdream.android:app:0.1@aar'
```

##License
```
Copyright (C) 2014 Snowdream Mobile <yanghui1986527@gmail.com>

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

[1]:https://oss.sonatype.org/service/local/artifact/maven/redirect?r=releases&g=com.github.snowdream.android&a=app&v=0.1&e=aar