import math, sys

name, filename = sys.argv

NUMBER_OF_LINES = 7

with open(filename,"r") as f:
    lines = f.readlines()
    jupiter_x = [float(lines[x].split()[0]) for x in range(0, len(lines)) if x % NUMBER_OF_LINES == 3]
    jupiter_y = [float(lines[x].split()[1]) for x in range(0, len(lines)) if x % NUMBER_OF_LINES == 3]
    saturn_x = [float(lines[x].split()[0]) for x in range(0, len(lines)) if x % NUMBER_OF_LINES == 4]
    saturn_y = [float(lines[x].split()[1]) for x in range(0, len(lines)) if x % NUMBER_OF_LINES == 4]
    voyager_x = [float(lines[x].split()[0]) for x in range(0, len(lines)) if x % NUMBER_OF_LINES == 6]
    voyager_y = [float(lines[x].split()[1]) for x in range(0, len(lines)) if x % NUMBER_OF_LINES == 6]
    min_distance_jupiter = float("inf")
    min_distance_saturn = float("inf")
    min_distance_both = float("inf")
    min_x = 0
    for x in range(0, len(jupiter_x)):
        distance_jupiter = math.sqrt((jupiter_x[x] - voyager_x[x]) ** 2 + (jupiter_y[x] - voyager_y[x]) ** 2)
        distance_saturn = math.sqrt((saturn_x[x] - voyager_x[x]) ** 2 + (saturn_y[x] - saturn_y[x]) ** 2)
        distance_both = distance_jupiter + distance_saturn
        if distance_both < min_distance_both:
            min_distance_both = distance_both
            min_distance_saturn = distance_saturn
            min_distance_jupiter = distance_jupiter
            min_x = x

    print("MIN DISTANCE BOTH: {dist}".format(dist=min_distance_both))
    print("MIN DISTANCE JUPITER: {dist}".format(dist=min_distance_jupiter))
    print("MIN DISTANCE BOTH: {dist}".format(dist=min_distance_saturn))
    print("MOMENT: {mom}".format(mom=min_x))
