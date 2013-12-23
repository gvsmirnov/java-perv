Java Perversions
================

Various java-related perversions. Do not try this in production. Unless you absolutely have to, or just feel like it, in which case totally do!

labs-time
----------------
A tool that tries to empirically estimate the precision of the various time-killers available.

Usage:
```
cd labs/
gradle clean shadow
java -cp build/libs/labs-unspecified-shadow.jar ru.gvsmirnov.perv.labs.time.PrecisionTest
```

Currently supported time killers are: ```Thread.sleep()```, ```Locksupport.parkNanos()```, ```BlackHole.consumeCPU()```, and a spinlooper that relies on ```System.nanoTime()```.

To make sure that you are not getting rubbish results due to internal VM activity, run it with
```
java -XX:+PrintCompilation -XX:+UnlockDiagnosticVMOptions -verbose:gc -XX:+PrintInlining -XX:+PrintSafepointStatistics -XX:PrintSafepointStatisticsCount=1 -cp build/libs/labs-unspecified-shadow.jar ru.gvsmirnov.perv.labs.time.PrecisionTest -v
```

labs-concurrency
------------------
Various investigations related to concurrency (e.g. http://habrahabr.ru/TODO:FINISH_THAT_ARTICLE)
