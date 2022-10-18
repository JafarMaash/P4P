# Hucka, M. (2018). Spiral: splitters for identifiers in source code files.
# Journal of Open Source Software, 3(24), 653, https://doi.org/10.21105/joss.00653

from spiral import ronin

# This file checks for case violations. 
# toSplit.txt contains every word that does not superficially conform to a casing convention (i.e. fully lowercase identifiers or those containing numbers).
# Each identifier is split here into its constituent words. If it is split into more than one word, then that means it has multiple words
# yet is NOT written using a casing convention, and hence is a violation. 

with open('toSplit.txt') as file:
    fieldViolations = 0
    methodViolations = 0
    for line in file:
        # read replace the string and write to output file
        split = (line.replace('\\n', '\n').replace('\\t', '\t').split('\t'))
        spiralSplit = ronin.split(split[1]) # split the identifier into its words, and put those in a string array
        if len(spiralSplit) > 1: # if there is more than one word in the identifier
            if split[0] == "FIELD":
                fieldViolations += 1
                print( split[1] + ": ", spiralSplit)
            else:
                methodViolations += 1
    print("Field case violations: " + str(fieldViolations))
    print("Method case violations: " + str(methodViolations))
