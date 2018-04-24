import math, sys

name, filename, dt = sys.argv
dt = float(dt)
NUMBER_OF_LINES = 7

with open(filename,"r") as f:
    lines = f.readlines()
    voyager_vx = [float(lines[x].split()[2]) for x in range(0, len(lines)) if x % NUMBER_OF_LINES == 6]
    voyager_vy = [float(lines[x].split()[3]) for x in range(0, len(lines)) if x % NUMBER_OF_LINES == 6]
    for x in range(0, len(voyager_vx)):
        speed = math.sqrt(voyager_vx[x] ** 2 + voyager_vy[x] ** 2)
        time = dt * x
        print('{time} {speed}'.format(speed=speed, time=time))
