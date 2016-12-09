
# simqueue


Author: Piero Dalle Pezze

Year: 2005

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

## Summary:

This is a queue simulator based on stochastic time events.
The aim is to simulate a real queue, like a queue at the post office,
where users arrive in a random order. When a client arrives, s/he is
the last one who will be served. Therefore, this is a FIFO (First In First Out) queue.

For each client there are two stochastic events: arrival and service times. 
In this simulation, the arrival time is sampled from an exponential distribution, 
whereas the service time from a triangular distribution.

This simulation shows when a person arrives, is ready to be served, and finally leaves.    


## Sampling random variables:
To simulate a continuous random variable, the _inverse transformation method_ 
(also called inverse transform sampling) was used. To use this method, the desired 
distribution function must be integrable and invertible. 

The exponential and triangular distribution functions chosen for simulating client arrival 
and service times, are both integrable and invertible.

Let `X ~ f(x)` be a continuous random variable, where `f(x)` is its probability density function (PDF). 
`f(x)` must be integrable in `[a,b]` and its integral `F(X)`, the cumulative distribution function (CDF) for `X`, 
must be invertible.

Therefore, the random variable `U = F(X)` has a uniform distribution on `[0,1]` (for a proof, see Ref.[2]). 
The method states that the desired random variable `F^{-1}(U)` has the same distribution of X.

In practice, one needs to compute:
```
F(y) = \int_a^b f(x) dx
X = F^{-1}(U) , U uniform distribution on [0,1]
```
`x_i` computed from `F^{-1}(u_i)` will have the desired distribution function.


__Note:__
If the functions are not integrable or invertible (e.g. normal distribution), other numerical 
methods are available (e.g.):

- Box-Muller transform
- Ziggurat algorithm


### Exponential variable
Let X ~ Exp(k) be an exponential random variable. Its PDF is:
```
f(x) =  k*e^{-k*x}   if x >= 0
    	0		     otherwise
```	    
Its CDF is:
```
U = F(X) = \int_0^{+inf} f(x) dx
	     = 1 - e^(-k*x)
```
The reverse function of F(X) is:
```
X = F[-1](U) = -( ln(1 - u) / k )
```
As a random variable `u=rand()` is computed from a uniform stochastic variable 
in every programming language, we can write
```
x_i = -( ln(u_i) / k ) = -( ln(1 - u_i) / k )
```


### Triangular variable
As above, we can obtain a stochastic history for the triangular stochastic variable.	




## Example:
### Compilation
Below is the commands for compiling this project. Maven is required.
```
$ mvn package
```

### A simulation
Configuration (file: parameters.txt):
```
clients_num=50
clients_per_hour=25
most_common_service_time=3.5
maximum_service_time=10.0
```

Simulation (output file: simqueue.csv)
```
$ java -jar target/simqueue-devel-jar-with-dependencies.jar 

simqueue: a FIFO queue simulator based on stochastic time events.

Client  Arrival Time (min)      Serving Time (min)      Leaving Time (min)
------  ------------------      ------------------      ------------------
[1]     0.0     				0.0     				6.5194354689736205
[2]     8.376951649010204       8.376951649010204       16.36632850759785
[3]     8.421513773953206       16.36632850759785       22.17770995239662
[4]     12.730545589483256      22.17770995239662       24.51561986289453
[5]     13.838939163002987      24.51561986289453       27.992603104538063
[6]     18.582802415438735      27.992603104538063      34.354410106957864
[7]     21.25313780072267       34.354410106957864      34.73485067042712
[8]     24.106943212828412      34.73485067042712       40.84747192302618
[9]     24.973418861373744      40.84747192302618       42.300674267145645
[10]    32.211858145469186      42.300674267145645      50.40952816081892
[11]    33.18097457812899       50.40952816081892       55.891741742668216
[12]    33.70534148159769       55.891741742668216      58.46176630848275
[13]    34.81851946729032       58.46176630848275       61.34317331399302
[14]    35.38957278969216       61.34317331399302       64.99072445331123
[15]    35.896170097620754      64.99072445331123       68.60928315475404
[16]    36.36419397882376       68.60928315475404       77.17529367454023
[17]    37.01230269892034       77.17529367454023       81.6392097107703
[18]    39.0674751587611        81.6392097107703        86.07941738952458
[19]    42.047944477060604      86.07941738952458       88.32528735685698
[20]    43.33735449924996       88.32528735685698       91.60580950028894
[21]    46.14257303824972       91.60580950028894       95.41097631894264
[22]    49.06277273631638       95.41097631894264       101.20938495532246
[23]    50.6202695410552        101.20938495532246      106.45908943755279
[24]    53.98859972021029       106.45908943755279      113.52598841444637
[25]    54.477074493154575      113.52598841444637      116.63278909958821
[26]    54.68132279272102       116.63278909958821      118.90476189187089
[27]    56.053104893284576      118.90476189187089      122.7890886383858
[28]    60.26583977227257       122.7890886383858       130.56770402572806
[29]    61.943180952336014      130.56770402572806      137.52739009604846
[30]    63.09747068422244       137.52739009604846      142.96776904004335
[31]    64.60748126475131       142.96776904004335      147.82549887776156
[32]    66.0636423844423        147.82549887776156      153.601031009093
[33]    68.59292391472951       153.601031009093        157.64053525399228
[34]    79.37309135581239       157.64053525399228      163.5675589221197
[35]    81.60436706417387       163.5675589221197       165.5944018159401
[36]    82.19045930690771       165.5944018159401       167.85188913952493
[37]    84.71241687698245       167.85188913952493      171.88796553469203
[38]    88.1287436537038        171.88796553469203      174.8540374363189
[39]    88.49883904414979       174.8540374363189       182.8612856504571
[40]    94.84619522948759       182.8612856504571       189.66459519021046
[41]    96.25047362076123       189.66459519021046      195.8273858303607
[42]    98.39516779111422       195.8273858303607       197.44478495655775
[43]    99.40441519891436       197.44478495655775      202.59687756777495
[44]    102.82850790937927      202.59687756777495      205.08787274404986
[45]    106.15813986743369      205.08787274404986      207.09543111879765
[46]    106.51042885560513      207.09543111879765      212.6884018229283
[47]    107.99269376286999      212.6884018229283       220.04023899450726
[48]    109.61708576712631      220.04023899450726      221.73089545889295
[49]    110.5968860398389       221.73089545889295      225.45423249332435
[50]    110.70447588325533      225.45423249332435      229.6996185897262


[THEORETICAL VALUES]
 1- Mean arrive time:           2.4 min
 2- Variance arrive time:       5.759999999999999 min^2
 3- Std dev arrive time:        2.4 min
 4- Maximum service time:       10.0 min
 5- Mean service time:          4.5 min
 6- Variance service time:      4.291666666666667 min^2
 7- Std dev service time:       2.071633815776009 min

[SIMULATED VALUES]
 1- Mean arrive time:           2.2140895176651068 min 
 2- Variance arrive time:       4.902192392234505 min^2 
 3- Std dev arrive time:        2.2140895176651068 min 
 4- Maximum service time:       8.56601051978619 min 
 5- Mean service time:          4.556842048193793 min 
 6- Variance service time:      2.795310672991756 min^2 
 7- Std dev service time:       1.6719182614565091 min 

[ABSOUTE ERRORS]
 1- Mean arrive time error:             0.18591048233489316 min 
 2- Variance arrive time error:         0.8578076077654941 min^2 
 3- Std dev arrive time error:          0.18591048233489316 min 
 4- Maximum service time error:         1.4339894802138105 min 
 5- Mean service time error:            0.05684204819379257 min 
 6- Variance service time error:        1.4963559936749111 min^2 
 7- Std dev service time error:         0.3997155543194997 min 

Running time of the simulation: 0 min 0 s 5 ms
```

Notes:

1. Time is expressed in minutes in this model.
2. When a client leaves the service (Leaving Time) another is served.
3. Client arrival time is sampled from an exponential distribution.
4. Client actual service time is sampled from a triangular distribution.


## References:

1. Malesani Paolo, `Ricerca Operativa`
2. Sheldon M. Ross, `A first course in probability`
