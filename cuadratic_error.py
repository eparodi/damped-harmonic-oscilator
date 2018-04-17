import sys
import subprocess
import csv
import math

def analytic(times):
    m = 70.0
    gamma = 100.0
    k = 10000.0
    return [math.exp(-(gamma*t)/(2*m)) * math.cos(math.sqrt(k/m - gamma ** 2/ (4* (m ** 2))) * t) for t in times]

def errorc(analytic_values, given):
    error_cuadratic = 0
    for i in range(0, len(given)):
        error = given[i] - analytic_values[i]
        error = error ** 2
        error_cuadratic += error
    return error_cuadratic / len(given)

name, alg, filename = sys.argv

with open(filename, 'w') as f:
    csv_writer = csv.writer(f, delimiter=';',
                            quotechar='|', quoting=csv.QUOTE_MINIMAL)
    dts = range(0, 1000)
    dts = [(x+1) / 10000.0 for x in dts]
    csv_writer.writerow(['dt', 'err'])
    for i in dts:
        command = 'java -jar ./target/damped-harmonic-oscilator-1.0-SNAPSHOT-jar-with-dependencies.jar \
        -alg {alg} -dt {dt}'.format(
                alg=alg,
                dt=i,
                )
        p = subprocess.Popen(command, shell=True, stdout=subprocess.PIPE, stderr=subprocess.STDOUT)
        lines =  p.stdout.readlines()
        positions = [float(x.split()[1]) for x in lines]
        times = [float(x.split()[0]) for x in lines]
        retval = p.wait()
        analytic_values = analytic(times)
        err = errorc(analytic_values, positions)
        csv_writer.writerow([i, err])
