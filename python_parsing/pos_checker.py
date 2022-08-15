# Hucka, M. (2018). Spiral: splitters for identifiers in source code files.
# Journal of Open Source Software, 3(24), 653, https://doi.org/10.21105/joss.00653

from calendar import c
from curses.ascii import FF
from fcntl import F_FULLFSYNC
from os import fchdir
from regex import F
from spiral import ronin
from torch import ccol_indices_copy

with open('toSplit.txt') as file:
    identifierViolations = 0
    methodViolations = 0
    for line in file:
        # read replace the string and write to output file
        split = (line.replace('\\n', '\n').replace('\\t', '\t').split('\t'))
        spiralSplit = ronin.split(split[1])
        # check the words of the identifier (spiralSplit) for PoS and then apply rules
    print("identifier camel case violations: " + str(identifierViolations))
    print("method camel case violations: " + str(methodViolations))


    # extract identifiers from java 
    # check in java for casing violations (camel, kebab, snake etc.)
    # pass violating identifiers into python using toSplit.txt
    # e.g. IDENTIFIER id_name Type=[modifier null/final] -- the = is irrelevant
    # split them into their words within each identifier 
    # 