
SIMULATED QUEUE

author: Piero Dalle Pezze
license: MIT
year: 2005


Object:
	This is a queue simulator based on stochastic time events.
	The aim is to simulate a real queue, like a queue of a post office,
	where users arrive in a random order. When a client arrives, he is
	the last one who will be served. (FIFO = First In First Out). In particular
	there are two stochastic factors:
	      1) the time of arrive of a client,
	      2) the time of a client service.
	During the time some clients arrive and some others go away.
	A simulation of this reality is to show when a person arrives, starts to be
	served and then leaves the server.    


	

About stochastic varibles:
      1) Exponential casual variable;
      2) Triangular casual variable;
      To simulate a continuous stochastic variable, we use the reversed transformation method. 
      In more detail, let X be a continuous stochastic variable, so X ~ f(x), where f(x) is its 
      density function. Let F(x) be its distribution function. Let f(x) be a increase monotonous 
      function, so F(x) is invertible.
      We can demonstrate that
	    U = F(X)
      where U is the uniform stochastic variable (for a proof, see "A first course in probability" by Ross). 
      U is exactly F(X) iff X is x, so where
	    U = F(X = x).
      Therefore, we obtain a casual history for the uniform stochastic variable.
      As we are interested in the casual history for the stochastic variable X,
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
		
		As a random variable `rand` is computated from a uniform stochastic variable 
		in every programming language, we can write
		    z = -( ln(1 - rand()) / b )

	   2)	As (1) we can obtain a casual history for the triangular stochastic variable.
		




Example:

		*** SIMULATED QUEUE ***

How is the capacity of the queue that you want to simulate? 30
Which is the averange number of users in an hour? 18
How long is the most common (the mode) service time (minutes)? 4
How long is the maximum service time (minutes)? 15

Casual history of the cadence times of the simulated queue (FIFO):

User	Arrived (minutes)	Served (minutes)	Go Away (minutes)
----	-----------------	----------------	-----------------
[1]	0.0			0.0			4.188943886954821
[2]	1.4139821986334242	4.188943886954821	7.440256323965718
[3]	5.079378625595708	7.440256323965718	12.039611302013554
[4]	7.458775707363041	12.039611302013554	16.016638020246067
[5]	7.8037596061715915	16.016638020246067	16.77788998531669
[6]	8.954605482483865	16.77788998531669	20.783905157161467
[7]	12.989380434793063	20.783905157161467	32.410989869914204
[8]	19.77521330578633	32.410989869914204	41.45211966641769
[9]	34.630254230911945	41.45211966641769	43.43924403521794
[10]	42.2697666179794	43.43924403521794	50.25660290649584
[11]	46.41066360455103	50.25660290649584	52.57980365172928
[12]	46.95991701849671	52.57980365172928	58.44051214884202
[13]	54.35930058974406	58.44051214884202	62.39369782375851
[14]	59.79814869974687	62.39369782375851	67.73245655812234
[15]	65.64797445225574	67.73245655812234	71.07671265120618
[16]	65.68156598489911	71.07671265120618	79.68056970067782
[17]	69.34973807397604	79.68056970067782	81.59887234594758
[18]	73.59117728339888	81.59887234594758	82.87662899370153
[19]	74.43035820758651	82.87662899370153	92.52718840217017
[20]	77.70783900510315	92.52718840217017	101.07158024730566
[21]	81.02857164165513	101.07158024730566	107.37268275874811
[22]	81.57887464310903	107.37268275874811	116.81892699460272
[23]	83.65034011003719	116.81892699460272	125.65810359610084
[24]	86.5876020858378	125.65810359610084	134.30911654445728
[25]	88.09144598387114	134.30911654445728	142.0605339450247
[26]	88.83616443831251	142.0605339450247	148.43611569638645
[27]	94.12481668363758	148.43611569638645	161.02852776419056
[28]	95.58106118149131	161.02852776419056	164.58082515889942
[29]	101.72466204438182	164.58082515889942	174.3981188474463
[30]	105.99007116130866	174.3981188474463	181.8643217957967


[REAL VALUES]
 1- Averange arrive: 3.3333333333333335 minutes
 2- Variance arrive: 11.11111111111111 minutes
 3- Standard deviation arrive: 3.3333333333333335
 4- Maximum service time: 15.0 minutes
 5- Averange service time: 6.333333333333333
 6- Variance service time: 10.055555555555555
 7- Standard deviation service time: 3.1710495984067415

[SIMULATED VALUES]
 1- Averange arrive: 3.533002372043622 minutes (Error = 0.1996690387102884)
 2- Variance arrive: 12.482105760865858 (Error = 1.3709946497547474)
 3- Standard deviation arrive: 3.533002372043622 (Error = 0.1996690387102884)
 4- Maximum service time: 12.592412067804105 minutes (Error = 2.407587932195895)
 5- Averange service time: 6.06214405985989 minutes (Error = 0.2711892734734427)
 6- Variance service time: 6.021268532668716 minutes (Error = 4.034287022886839)
 7- Standard deviation service time: 2.4538273233193726 minutes (Error = 0.7172222750873689)




Notes:
  1) In this model, the time is expressed in minutes.
  2) When a user leaves the service (Go Away) another is served.
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
     	In fact this is the b parameter for the triangular casual variable T(a,m,b), where (a,b) is the interval in 
     	which there is the function of density for the variable and m is the mode ("the most common (the mode) 
     	service time"). The interval (a, b) is (0, b) because at least one client must be served (so a is 0). The 
     	mode m is the greatest value assumed by the density function of the triangular stochastic variable.
	As in the previous note, the average service time (b) follows the same rule as for (a).
	As example, let us consider user [24]:	
		x[24] = 69.80473426491487 ,                 
		y[24] = 150.0365004617665 ,                
		z[24] = 160.93758814506594 .
	The service time is the value ( 160.93758814506594 - 150.0365004617665 ), so: 
			st[24] = z[24] - y[24].
	You can also observe that 
		the mode for the vector st is for the mode you have put at start for n --> infinite,
		the expected value for the vector st is for the averange service time for n --> infinite,
		no st[k] for k = 1 to n is over than the maximum service time.



References:
	For references, chapter 6 of `Ricerca Operativa` by Paolo Malesani offers an overview of simulated queues. 
	'A first course in probability' by Sheldon M. Ross.

								Piero Dalle Pezze
								10th October 2005
