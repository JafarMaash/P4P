# Hucka, M. (2018). Spiral: splitters for identifiers in source code files.
# Journal of Open Source Software, 3(24), 653, https://doi.org/10.21105/joss.00653

from spiral import ronin

with open('toSplit.txt') as file:
    identifierViolations = 0
    methodViolations = 0
    for line in file:
        # read replace the string and write to output file
        split = (line.replace('\\n', '\n').replace('\\t', '\t').split('\t'))
        spiralSplit = ronin.split(split[1])
        if len(spiralSplit) > 1:
            if split[0] == "IDENTIFIER":
                identifierViolations += 1
                print( split[1] + ": ", spiralSplit)
            else:
                methodViolations += 1
    print("identifier camel case violations: " + str(identifierViolations))
    print("method camel case violations: " + str(methodViolations))