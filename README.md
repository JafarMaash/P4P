# P4P
Welcome to the repository for Part 4 Project #67: *Evaluating Identifier Quality* 
<br>Students: Jack Chu & Jafar Maash
<br>Supervisor: Ewan Tempero

Identifiers are a key source of information for developers when understanding concepts within source code. 
<br>Consequently, writing meaningful identifiers is important and can lead to code of a higher quality, readability/comprehensibility, and maintainability.
<br>The literature lays out a number of guidelines for how to meaningfully name identifiers. 
<br>This project takes identifier naming guidelines from multiple sources in the literature and assesses identifier names from a range of source code repositories against them to determine how closely each guideline is followed.

The result of this is a source code tool which extracts identifier names from a repository, splits them into their constituent words, and then assesses them against 9 different semantic guidelines as well as around 5 typographic guidelines (i.e. casing, underscores, etc). 

## Project setup

This project can be split into two major components: 
- The Java part (`P4P/src/main/java`), which uses JavaParser to extract identifiers and evaluate them typographically.
- The Python part (`P4P/python_parsing`), which uses an identifier splitter and Natural Language Parser (NLP) to evaluate them semantically. 

## General flow for evaluating a repository (how to run)
- Download a repository's source code and extract the source files to a folder 
- Edit the path variable inside `JavaParserIdentifiers.java` for the desired repository and run it to extract the identifiers from the repository
  - `toSplit.txt`, `allIdentifiers.txt`, and `csv_data.csv` are produced
  - Statistics for number of fields, number of methods, and length/underscore usage violations are outputted in the console (and `csv_data.csv`)
- Run `splitter.py`
  - case violations are outputted in the console ONLY
- Run `pos_checker.py`
  - Semantic violations are outputted in the console (and `csv_data.csv`)
- Record all this data, delete `toSplit.txt`, `allIdentifiers.txt`, and `csv_data.csv`, and repeat on a new repository. 

In addition, 3 files are created with every run:
- `allIdentifiers.txt`
  - Every identifier in a repo, whether it's a field or method, its type/return type, and (if it's a field) whether or not it has a final modifier
  - Used for semantic guidelines
- `toSplit.txt`
  - Every identifier which doesn't follow a casing convention regex pattern
  - Run through the splitter to identify true casing violations
- `csv_data.csv`
  - Temporary csv file which outputs most of the violation statistics which can be then easily copy pasted into a spreadsheet to save time 


**NOTE: Sometimes, identifiers recorded in the txt files also carry over comments with them - I don't know why. These will break the Python scripts, 
so if you want, before you run them, you can ctrl+f "/" in `allIdentifiers.txt` and `toSplit.txt` and remove any comments and ensure the file is formatted properly.**

To see an example of how to edit the root path in order to analyse your desired repository source code, look at how its done in the code for the `kalah_designs`.

## The Java part
Files: 
- `JavaParserIdentifiers.java`
- `TypographyGuidelines.java`
- `Analyser.java` 
- `Utility.java`

### `JavaParserIdentifiers.java`
This class uses JavaParser to extract identifiers from a repository. 
It loops through every path/file in a repository, and adds the identifiers for all fields and methods into two hashmaps respectively. 
- `methods` stores (key,value) pairs representing (method name, return type) respectively. 
- `fields` stores (key, value) pairs where the value is an Entry object. Overall a `fields` entry looks like (field name, <type, modifier>).
The hashmap is set up like this in order to keep track of the modifiers fields have, as if they are `final` then different guidelines apply to them (see Every Guideline Implemented). 

After recording every identifier in a repository, a `TypographyGuidelines` object is created and is passed these hashmaps. It then (you guessed it) assesses the identifiers against typography guidelines and returns the results. 
The results are then printed from within `JavaParserIdentifiers`. 

### `TypographyGuidelines.java`
This class has different methods representing different guidelines, which are primarily individually called from within `JavaParserIdentifiers.java`.
It checks method and field length, final field casing, underscore usage, and casing conventions used. 
This class writes every identifier and its type (and final modifier if applicable for fields) to `allIdentifiers.txt` for later semantic analysis.
It also evaluates every identifier to see if it matches regex patterns for camelCase, PascalCase, snake_case, and kebab-case. Any identifier which does not follow one of these
is recorded in `toSplit.txt`. The identifiers which don't pass the test are generally those which are fully lowercase, or use numbers. Obviously, not all fully lowercase identifiers violate casing conventions, since those which are just one word have to be fully lowercase (e.g. `delete`). 
In order to distinguish between single-word (i.e. typograhpically valid) lowercase identifiers like `delete`, and those multi-word identifiers which aren't cased properly like e.g. `anotherfolder`, they are split by `splitter.py`. 
Those identifiers which split into multiple words are then recorded as case violations, as if they were properly cased they never would've made it to `toSplit.txt` anyway (since they would've followed one of the conventions' regex patterns and hence been skipped)

 ### `Analyser.java` and `Utility.java`
 We didn't touch these classes, so will not comment on them. They were copied over from Ewan's example use of JavaParser. They **are** necessary though so probably don't touch them, idk.  

## The Python part
This is where the identifiers are split, and semantic guidelines are applied.

Relevant files:
- `splitter.py`
- `pos_checker.py`
- `pos_tagger.py`

### `splitter.py`
This file splits identifiers which don’t superficially follow a casing convention into their constituent words, in order to evaluate whether or not they committed casing violations. It uses the **spiral** library, which can be found and installed [here](https://github.com/casics/spiral). There was an issue about collections library and python version or something like that. If you're reading this I didn't update the readme with details. To put it briefly, it requires going into spiral's source code and adding `abc.collections` in some file instead of `collections`.  

### `pos_checker.py`
Splits every identifier into its constituent words, evaluating them against semantic identifier naming guidelines. This uses spiral to split the words and spaCy as a Natural Language Parser in order to identify the part of speech of every word. More information on spaCy can be found [here](https://spacy.io/usage).

### `pos_tagger.py`
This file is not relevant to the repo evaluation process. It can be used for internal testing purposes, to observe how spaCy processes certain individual words. 


## Every Guideline Implemented

### P.A. Relf, “Achieving Software Quality through Source Code Readability”, *Qualcon 2004*: <br>
![image](https://user-images.githubusercontent.com/62087759/196416505-7d04326f-9d32-4bd4-885b-1c644268e860.png)


### D. Binkley, M. Hearn, and D. Lawrie, “Improving identifier informativeness using part of speech information.”, 2011 <br>

![image](https://user-images.githubusercontent.com/62087759/196417973-ec04f601-5b13-4c84-81bc-4bcbbbc3a41c.png)


### J. Gosling, B. Joy, G. Steele, G. Bracha, and A. Buckley. “The Java Language Specification: Java SE 8 Edition.” <br>
![image](https://user-images.githubusercontent.com/62087759/196416727-a5004efc-6991-45c7-8676-0b1aa40c465a.png)
![image](https://user-images.githubusercontent.com/62087759/196416762-6f0c838e-66d6-466c-9f27-c4de2fadb490.png)

### Accepted Cases:
- camelCase
- PascalCase
- snake_case
- kebab-case

### The Data Spreadsheet
In the data spreadsheet, semantic rules are numbered as follows: 
![image](https://user-images.githubusercontent.com/62087759/196423862-19d07707-2b24-4096-9e30-429d2a558c5c.png)
![image](https://user-images.githubusercontent.com/62087759/196423886-a3e4ebd8-ac35-4f35-8d46-19673031166f.png)

In addition, we used to track what case conventions every repository followed, which you'll see in the spreadsheet.
Virtually every repo was around 99% camelCase so we stopped.

