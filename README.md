Java Perversions
================

Various java-related perversions. Do not try this in production. Unless you absolutely have to, or just feel like it, in which case totally do!

labs-time
---------
A tool that tries to empirically estimate the precision of various ways to bide your time in java..

Usage:
```
cd labs/
gradle clean shadow
java -XX:-UseBiasedLocking -cp build/libs/perverted-labs-0.1.jar ru.gvsmirnov.perv.labs.time.PrecisionTest
```


To make sure that you are not getting rubbish results due to internal VM activity, run it with
```
java -XX:+PrintCompilation -XX:+UnlockDiagnosticVMOptions -verbose:gc -XX:+PrintInlining -XX:+PrintSafepointStatistics -XX:PrintSafepointStatisticsCount=1 -cp build/libs/perverted-labs-0.1.jar ru.gvsmirnov.perv.labs.time.PrecisionTest -v
```

The precision may be affected by both resolution and by invocation cost. Use this command to estimate the invocation cost:
```
java -cp build/libs/perverted-labs-0.1.jar org.openjdk.jmh.Main "ru.gvsmirnov.perv.labs.time.*" -f -tu us -bm sample
```

labs-concurrency
----------------
Various investigations related to concurrency (e.g. http://gvsmirnov.ru/blog/tech/2014/02/10/jmm-under-the-hood.html)

labs-jit
--------
Messing with JIT, see here: