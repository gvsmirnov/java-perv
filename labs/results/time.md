Linux Laptop
===================
3816 MB RAM; Intel(R) Core(TM) i3 CPU M 380  @ 2.53GHz

```
Benchmark                                        Mode Thr     Count  Sec         Mean   Mean error    Units
r.g.p.l.t.TimeKillerBenchmark.burnOneMs        sample   1     20008    1      999.577        0.064    us/op
r.g.p.l.t.TimeKillerBenchmark.burnTwoMs        sample   1     10007    1     1998.997        0.063    us/op
r.g.p.l.t.TimeKillerBenchmark.getNanoTime      sample   1  23832661    1        0.067        0.000    us/op
r.g.p.l.t.TimeKillerBenchmark.parkOneMs        sample   1     17934    1     1114.076        1.698    us/op
r.g.p.l.t.TimeKillerBenchmark.parkTwoMs        sample   1      9472    1     2110.587        2.262    us/op
r.g.p.l.t.TimeKillerBenchmark.returnNanoTime   sample   1  23830755    1        0.067        0.000    us/op
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
