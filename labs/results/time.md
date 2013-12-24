Linux Laptop
------------------------
3816 MB RAM; Intel(R) Core(TM) i3 CPU M 380  @ 2.53GHz

```
Benchmark                                        Mode Thr     Count  Sec         Mean   Mean error    Units
r.g.p.l.t.TimeKillerBenchmark.burnOneMs        sample   1     20008    1      999.577        0.064    us/op
r.g.p.l.t.TimeKillerBenchmark.burnTwoMs        sample   1     10007    1     1998.997        0.063    us/op
r.g.p.l.t.TimeKillerBenchmark.getNanoTime      sample   1  23832661    1        0.067        0.000    us/op
r.g.p.l.t.TimeKillerBenchmark.parkOneMs        sample   1     17934    1     1114.076        1.698    us/op
r.g.p.l.t.TimeKillerBenchmark.parkTwoMs        sample   1      9472    1     2110.587        2.262    us/op
r.g.p.l.t.TimeKillerBenchmark.sleepOneMs       sample   1     18288    1     1092.754        1.803    us/op
r.g.p.l.t.TimeKillerBenchmark.sleepTwoMs       sample   1      9408    1     2124.348        4.071    us/op
```

```
Estimating the precision of Thread.sleep()...
Precision of Thread.sleep() is 1.118 ms
Estimating the precision of LockSupport.parkNanos()...
Precision of LockSupport.parkNanos() is 213.938 us
Estimating the precision of System.nanoTime()...
Precision of System.nanoTime() is 316.0 ns
```

Windows Box
------------------------
4079 MB RAM; Intel(R) Xeon(R) CPU E5606 @ 2.13GHz

```
Benchmark                                     Mode Thr     Count  Sec         Mean   Mean error    Units
r.g.p.l.t.TimeKillerBenchmark.burnOneMs     sample   1     19984    1      999.693        0.114    us/op
r.g.p.l.t.TimeKillerBenchmark.burnTwoMs     sample   1     10000    1     1999.262        0.201    us/op
r.g.p.l.t.TimeKillerBenchmark.getNanoTime   sample   1  21824817    1        0.070        0.000    us/op
r.g.p.l.t.TimeKillerBenchmark.parkOneMs     sample   1     19896    1     1004.414        5.953    us/op
r.g.p.l.t.TimeKillerBenchmark.parkTwoMs     sample   1     10013    1     1997.918        1.987    us/op
r.g.p.l.t.TimeKillerBenchmark.sleepOneMs    sample   1     19913    1     1003.507        4.621    us/op
r.g.p.l.t.TimeKillerBenchmark.sleepTwoMs    sample   1     10011    1     1997.825        2.117    us/op
```

```
Estimating the precision of Thread.sleep()...
Precision of Thread.sleep() is 1.38 ms
Estimating the precision of LockSupport.parkNanos()...
Precision of LockSupport.parkNanos() is 1.104 ms
Estimating the precision of System.nanoTime()...
Precision of System.nanoTime() is 554.0 ns
```
