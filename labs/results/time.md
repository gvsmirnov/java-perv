Linux Laptop
------------------------
3816 MB RAM; Intel(R) Core(TM) i3 CPU M 380  @ 2.53GHz

```
Benchmark                                     Mode Thr     Count  Sec         Mean   Mean error    Units
r.g.p.l.t.TimeKillerBenchmark.burnOneMs     sample   1     20003    1      999.652        0.217    us/op
r.g.p.l.t.TimeKillerBenchmark.burnOneNs     sample   1  24245077    1        0.110        0.001    us/op
r.g.p.l.t.TimeKillerBenchmark.burnTwoMs     sample   1     10005    1     1998.941        0.046    us/op
r.g.p.l.t.TimeKillerBenchmark.getNanoTime   sample   1  21232069    1        0.074        0.000    us/op
r.g.p.l.t.TimeKillerBenchmark.parkOneMs     sample   1     18210    1     1097.345        3.912    us/op
r.g.p.l.t.TimeKillerBenchmark.parkOneNs     sample   1    322256    1       61.871        0.135    us/op
r.g.p.l.t.TimeKillerBenchmark.parkTwoMs     sample   1      9491    1     2105.804        5.576    us/op
r.g.p.l.t.TimeKillerBenchmark.sleepOneMs    sample   1     18135    1     1101.718        1.175    us/op
r.g.p.l.t.TimeKillerBenchmark.sleepOneNs    sample   1     18178    1     1099.514        4.258    us/op
r.g.p.l.t.TimeKillerBenchmark.sleepTwoMs    sample   1      9497    1     2104.680        6.870    us/op
```

```
Estimating the precision of Thread.sleep()...
Precision of Thread.sleep() is 1.126 ms
Estimating the precision of LockSupport.parkNanos()...
Precision of LockSupport.parkNanos() is 210.480 us
Estimating the precision of System.nanoTime()...
Precision of System.nanoTime() is 316.0 ns
Estimating the precision of Thread.yield()...
Precision of Thread.yield() is 436.0 ns
Estimating the precision of Object.wait(millis, nanos)...
Precision of Object.wait(millis, nanos) is 1.128 ms
Estimating the precision of LockSupport.park()...
Precision of LockSupport.park() is 15.625 ms
Estimating the precision of Object.wait()...
Precision of Object.wait() is 250.0 ms
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
