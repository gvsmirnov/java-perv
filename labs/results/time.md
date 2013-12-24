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
r.g.p.l.t.TimeKillerBenchmark.burnOneMs     sample   1     19989    1      999.672        0.108    us/op
r.g.p.l.t.TimeKillerBenchmark.burnOneNs     sample   1  20232856    1        0.495        0.001    us/op
r.g.p.l.t.TimeKillerBenchmark.burnTwoMs     sample   1     10000    1     1999.037        0.114    us/op
r.g.p.l.t.TimeKillerBenchmark.getNanoTime   sample   1  23745894    1        0.066        0.000    us/op
r.g.p.l.t.TimeKillerBenchmark.parkOneMs     sample   1     20004    1      998.957        1.811    us/op
r.g.p.l.t.TimeKillerBenchmark.parkOneNs     sample   1     20012    1      998.565        1.389    us/op
r.g.p.l.t.TimeKillerBenchmark.parkTwoMs     sample   1     10013    1     1998.168        2.678    us/op
r.g.p.l.t.TimeKillerBenchmark.sleepOneMs    sample   1     20011    1      998.441        1.462    us/op
r.g.p.l.t.TimeKillerBenchmark.sleepOneNs    sample   1     20009    1      998.668        1.640    us/op
r.g.p.l.t.TimeKillerBenchmark.sleepTwoMs    sample   1     10014    1     1997.672        1.998    us/op
```

```
Estimating the precision of Thread.sleep()...
Precision of Thread.sleep() is 1.38 ms
Estimating the precision of LockSupport.parkNanos()...
Precision of LockSupport.parkNanos() is 1.104 ms
Estimating the precision of System.nanoTime()...
Precision of System.nanoTime() is 554.0 ns
```
