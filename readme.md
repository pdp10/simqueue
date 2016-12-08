
# simqueue


Author: Piero Dalle Pezze

Year: 2005

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

## Summary:
This is a queue simulator based on stochastic time events.
It aims to simulate a real queue, like a queue at the post office,
where clients arrive in a random order. When a client arrives, s/he is
the last one who will be served. (FIFO = First In First Out). In particular
there are two stochastic factors:

- the time of arrive,
- the time of service.

Meanwhile, clients arrive and others leave.
This simulation shows when a person arrives, is ready to be served, and finally leaves.    


## About stochastic variables:
To simulate a continuous stochastic variable, we use the reversed transformation method. 
In more detail, let X be a continuous stochastic variable, so X ~ f(x), where f(x) is its 
density function. Let F(x) be its distribution function. Let f(x) be a increase monotonous 
function, so F(x) is invertible. We can demonstrate that
```
U = F(X)
```
where U is the uniform stochastic variable (for a proof, see "A first course in probability" by Ross). 
U is exactly F(X) iff X is x, so where
```
U = F(X = x).
```
Therefore, we obtain a stochastic history for the uniform stochastic variable.
As we are interested in the stochastic history for the stochastic variable X,
we must calculate the reverse function:
```
X = F[-1](U)    (where F[-1] is the reverse function of F).
```

Simqueue samples clients arrival times from an extponential distribution, whereas 
service times are sampled from a triangular distribution. 

### Exponential variable
Let X ~ Eb be an exponential stochastic variable of parameter b.
Its density function is:
```
f(x) =  b*e exp(-b*x)   if x >= 0
    	0		if x < 0
```	    
Its distribution function is:
```
y = F(z) = INTEGRAL( f(z) dz )  from 0 to z
	 = 1 - e exp(-b*z)
```
(y is an uniform variable U !!)
The reverse function of F(z) is:
```
z = F[-1](y) = -( ln(1-y) / b )
```
As a random variable `rand` is computed from a uniform stochastic variable 
in every programming language, we can write
```
z = -( ln(1 - rand()) / b )
```

### Triangular variable
As above, we can obtain a stochastic history for the triangular stochastic variable.	




## Example:
### Compilation
Below are the commands for compiling this project. Maven is required.
```
$ mvn package
$ cd target
$ java -jar simqueue-devel-jar-with-dependencies.jar 
```

### A simulation
```
simqueue: a queue simulator based on stochastic time events.

Number of clients to simulate: 30
Mean number of clients per hour: 18
Most common service time [min] (the mode): 4
Longest service time [min]: 15

Stochastic generation of Arrival/Service/Leaving times for this simulated queue (FIFO):

Client  Arrival Time (min)      Serving Time (min)      Leaving Time (min)
------  ------------------      ------------------      ------------------

[1]     0.0     				0.0     				14.094579105519252
[2]     5.708226732849479       14.094579105519252      20.051025201680716
[3]     15.623349503549953      20.051025201680716      20.921822084160496
[4]     21.960956579775768      21.960956579775768      33.02358276729773
[5]     22.98081380928354       33.02358276729773       40.683632133149686
[6]     25.44686713329548       40.683632133149686      45.1933325642773
[7]     27.889408777669246      45.1933325642773        48.56811726897787
[8]     36.80080428561017       48.56811726897787       52.055270399401486
[9]     37.047697699391726      52.055270399401486      58.108546127310284
[10]    37.78847766871045       58.108546127310284      67.81082561594309
[11]    45.06969902561143       67.81082561594309       71.8154836600492
[12]    47.89627511561797       71.8154836600492        74.41582161043138
[13]    48.19383300222079       74.41582161043138       79.62812606893108
[14]    48.39667373104677       79.62812606893108       83.6325622182463
[15]    50.935789447325334      83.6325622182463        85.39600733918046
[16]    52.34657182386873       85.39600733918046       96.85268602778352
[17]    56.40578601609782       96.85268602778352       109.178502855287
[18]    57.904258026579846      109.178502855287        111.43937759798699
[19]    59.54557171837028       111.43937759798699      120.95028478546092
[20]    66.22514562292336       120.95028478546092      125.2755480433057
[21]    67.17365914392425       125.2755480433057       128.20578114736753
[22]    67.5391442396196        128.20578114736753      136.38392718445948
[23]    68.22130836603034       136.38392718445948      142.47583217538482
[24]    72.96863270019018       142.47583217538482      151.11195130234296
[25]    77.27930261235403       151.11195130234296      153.87523693275224
[26]    78.14500078485722       153.87523693275224      155.26068177696396
[27]    82.9901299048406        155.26068177696396      165.2803070659285
[28]    93.1578730092031        165.2803070659285       172.07088177207356
[29]    95.60849939247149       172.07088177207356      180.00622391339965
[30]    96.54470672270493       180.00622391339965      188.3903964910933

[THEORETICAL VALUES]
 1- Mean arrive time:           3.3333333333333335 min
 2- Variance arrive time:       11.11111111111111 min^2
 3- Std dev arrive time:        3.3333333333333335 min
 4- Maximum service time:       15.0 min
 5- Mean service time:          6.333333333333333 min
 6- Variance service time:      10.055555555555555 min^2
 7- Std dev service time:       3.1710495984067415 min

[SIMULATED VALUES]
 1- Mean arrive time:           3.218156890756831 min 
 2- Variance arrive time:       10.356533773525674 min^2 
 3- Std dev arrive time:        3.218156890756831 min 
 4- Maximum service time:       14.094579105519252 min 
 5- Mean service time:          6.245042066515935 min 
 6- Variance service time:      8.052067732673962 min^2 
 7- Std dev service time:       2.83761655842962 min 

[RELATIVE ERRORS]
 1- Mean arrive time error:             0.1151764425765025 min 
 2- Variance arrive time error:         0.7545773375854363 min^2 
 3- Std dev arrive time error:          0.1151764425765025 min 
 4- Maximum service time error:         0.9054208944807485 min 
 5- Mean service time error:            0.0882912668173983 min 
 6- Variance service time error:        2.0034878228815938 min^2 
 7- Std dev service time error:         0.3334330399771215 min 

Running time of the simulation: 0 min 0 s 158 ms
```

Notes:

1. Time is expressed in minutes in this model.
2. When a client leaves the service (Leaving Time) another is served.
3. Client arrival time is sampled from an exponential distribution.
4. Client actual service time is sampled from a triangular distribution.


## References:

- Malesani Paolo, `Ricerca Operativa` 
- Sheldon M. Ross, `A first course in probability`
