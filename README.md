# Time driven simulations
Implementation of two time driven simulations: damped harmonic oscillator and Voyager 1 Mission.

## Compilation
```
mvn package
```

## Execution

### Damped harmonic oscillator
```
java -jar target/damped-harmonic-oscilator-1.0-SNAPSHOT-jar-with-dependencies.jar 
```
Parameters:

* **-alg, --algorithm &lt;arg>**: Algorithm to run (bm: Beeman, ve: Verlet, gp: Gear Predictor).
* **-dt, --deltaTime &lt;arg>**: Interval of time.
* **-g, --gamma &lt;arg>**: Gamma of the system.
* **-h, --help**: Shows the help.
* **-k, --elasticity &lt;arg>**: Elasticity of the system.
* **-m, --mass &lt;arg>**: Mass of the particle.
* **-r, --position &lt;arg>**: Initial position of the particle.
* **-tf, --finalTime &lt;arg>**: Time when the simulation ends.

### Voyager 1 Mission
```
java -jar target/damped-harmonic-oscilator-1.0-SNAPSHOT-jar-with-dependencies.jar -vo  
```

Parameters:

* **-dt, --deltaTime &lt;arg>**: Interval of time.
* **-fps, --frames &lt;arg>**: Frames per second.
* **-h, --help**: Shows the help.
* **-pf, --planetFile &lt;arg>**: File with the planet's initial positions.
* **-tf, --finalTime &lt;arg>**: Time when the simulation ends.
