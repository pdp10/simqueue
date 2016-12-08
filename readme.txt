
SIM QUEUE

author: Piero Dalle Pezze
license: MIT
year: 2005


Object:
	This is a queue simulator based on stochastic time events.
	The aim is to simulate a real queue, like a queue at the post office,
	where users arrive in a random order. When a client arrives, s/he is
	the last one who will be served. (FIFO = First In First Out). In particular
	there are two stochastic factors:
	      1) the time of arrive,
	      2) the time of service.
	Meanwhile, clients arrive and others leave.
	This simulation shows when a person arrives, is ready to be served, and finally leaves.    


	

About stochastic variables:
      1) Exponential stochastic variable;
      2) Triangular stochastic variable;
      To simulate a continuous stochastic variable, we use the reversed transformation method. 
      In more detail, let X be a continuous stochastic variable, so X ~ f(x), where f(x) is its 
      density function. Let F(x) be its distribution function. Let f(x) be a increase monotonous 
      function, so F(x) is invertible.
      We can demonstrate that
	    U = F(X)
      where U is the uniform stochastic variable (for a proof, see "A first course in probability" by Ross). 
      U is exactly F(X) iff X is x, so where
	    U = F(X = x).
      Therefore, we obtain a stochastic history for the uniform stochastic variable.
      As we are interested in the stochastic history for the stochastic variable X,
      we must calculate the reverse function:
	    X = F[-1](U)    (where F[-1] is the reverse function of F).
      
      For instance:
	   1)   Let X ~ Eb be an exponential stochastic variable of parameter b.
		Its density function is:
		    f(x) =  b*e exp(-b*x)   if x >= 0
			    0		    if x < 0
		Its distribution function is:
		    y = F(z) = INTEGRAL( f(z) dz )  from 0 to z
			     = 1 - e exp(-b*z)
		    (y is an uniform variable U !!)
		The reverse function of F(z) is:
		    z = F[-1](y) = -( ln(1-y) / b )
		
		As a random variable `rand` is computed from a uniform stochastic variable 
		in every programming language, we can write
		    z = -( ln(1 - rand()) / b )

	   2)	As (1) we can obtain a stochastic history for the triangular stochastic variable.
		




Example:

$ ./SimQueue.sh 

                *** SIMULATED QUEUE ***

Enter the queue size: 30
Mean number of clients per hour: 18
Most common service time [min] (the mode): 4
Longest service time [min]: 15

Stochastic generation of Arrival/Service/Leaving times for this simulated queue (FIFO):

User    Arrival Time (min)      Serving Time (min)      Leaving Time (min)
----    -----------------       ----------------        -----------------

[1]     0.0     0.0     9.301833991309138
[2]     3.2006732917769094      9.301833991309138       20.789495508765896
[3]     4.396008014055122       20.789495508765896      28.71412359297996
[4]     8.093336465580835       28.71412359297996       39.68610156873699
[5]     10.799065473229996      39.68610156873699       40.2137512617693
[6]     14.916437982156179      40.2137512617693        42.02088952920746
[7]     23.96628486861038       42.02088952920746       45.621417701475465
[8]     39.69469213197855       45.621417701475465      49.11660343758008
[9]     39.98275558788958       49.11660343758008       54.53843925197566
[10]    45.40026916938175       54.53843925197566       62.964044639814375
[11]    49.6127932666428        62.964044639814375      64.63680367740662
[12]    50.48917011194782       64.63680367740662       69.99545336937453
[13]    50.7676265263396        69.99545336937453       73.25899180621364
[14]    54.86385684102196       73.25899180621364       83.72691130487954
[15]    55.3700978565707        83.72691130487954       87.73019488103837
[16]    58.67834360438434       87.73019488103837       95.04913126536003
[17]    58.8708269314442        95.04913126536003       99.17469300281823
[18]    66.53488751712008       99.17469300281823       109.43540790639068
[19]    68.15319428330746       109.43540790639068      115.93301792952013
[20]    68.35539282551437       115.93301792952013      118.06748082306903
[21]    71.89163045347134       118.06748082306903      127.47054499125433
[22]    72.19282919529373       127.47054499125433      130.12517226201152
[23]    76.6001846811393        130.12517226201152      136.28733618616653
[24]    85.59352546833523       136.28733618616653      150.81828491361756
[25]    87.68406702919287       150.81828491361756      161.19119475332457
[26]    93.49465050008948       161.19119475332457      168.0375220388211
[27]    100.62256385478823      168.0375220388211       169.98486048662684
[28]    103.68423485473251      169.98486048662684      177.69041460261585
[29]    104.73193174175726      177.69041460261585      178.86759910394807
[30]    106.35424335608094      178.86759910394807      183.3205690513528

[REAL VALUES]
 1- Mean arrive time:           3.3333333333333335 min
 2- Variance arrive time:       11.11111111111111 min^2
 3- Std dev arrive time:        3.3333333333333335 min
 4- Maximum service time:       15.0 min
 5- Mean service time:          6.333333333333333 min
 6- Variance service time:      10.055555555555555 min^2
 7- Std dev service time:       3.1710495984067415 min

[SIMULATED VALUES]
 1- Mean arrive time:           3.545141445202698 min 
 2- Variance arrive time:       12.568027866493876 min^2 
 3- Std dev arrive time:        3.5451414452026984 min 
 4- Maximum service time:       14.530948727451033 min 
 5- Mean service time:          6.110685635045093 min 
 6- Variance service time:      9.17674823150569 min^2 
 7- Std dev service time:       3.029314812215081 min 

[RELATIVE ERRORS]
 1- Mean arrive time error:             0.21180811186936443 min (5.9746027 %)
 2- Variance arrive time error:         1.4569167553827658 min^2 (11.592246 %)
 3- Std dev arrive time error:          0.21180811186936488 min (5.9746027 %)
 4- Maximum service time error:         0.46905127254896684 min (3.2279468 %)
 5- Mean service time error:            0.22264769828823994 min (3.6435797 %)
 6- Variance service time error:        0.8788073240498662 min^2 (9.576457 %)
 7- Std dev service time error:         0.1417347861916607 min (4.678774 %)

Running time of the simulation: 0 min 0 s 159 ms




Notes:
  1) In this model, the time is expressed in minutes.
  2) When a user leaves the service (Leaving Time) another is served.
  3) An exponential stochastic variable for input time:
     	When the average number of users per hour is requested, the program converts this number (user/hour) in the 
    	average number of users per minute (user/minute). Also, it computes the average time of arrival of a new 
	user (a). This allows to examine that (a) is the expected value for the first column:
		where T  is the actual time,
			n  is the capacity of the queue,
			t(k) is the time function that returns the time distance from the user_k to the user_(k-1), 
			so tk = T - t(k-1) ,
		we obtain
			(a)  =  [SUM ( t(k) ) for k = 1 to n ] / n      for n --> infinite (the low of the great numbers)
  4) A triangular stochastic variable for input time:
     	"The maximum service time" is a measure of the maximum time recorded for the service.
     	In fact this is the b parameter for the triangular stochastic variable T(a,m,b), where (a,b) is the interval in 
     	which there is the function of density for the variable and m is the mode ("the most common (the mode) 
     	service time"). The interval (a, b) is (0, b) because at least one client must be served (so a is 0). The 
     	mode m is the greatest value assumed by the density function of the triangular stochastic variable.
	As in the previous note, the average service time (b) follows the same rule as for (a).
	You can also observe that 
		the mode for the vector st is for the mode you have put at start for n --> infinite,
		the expected value for the vector st is for the average service time for n --> infinite,
		no st[k] for k = 1 to n is over than the maximum service time.



References:
	For references, chapter 6 of `Ricerca Operativa` by Paolo Malesani offers an overview of simulated queues. 
	'A first course in probability' by Sheldon M. Ross.

Piero Dalle Pezze
10th October 2005
