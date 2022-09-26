import spacy

from spiral import ronin
import csv

nlp = spacy.load('en_core_web_lg')
with open('allIdentifiers.txt') as file:
    notANoun = 0
    nonBoolPTVerb = 0
    boolWithout3PVerb = 0
    onlyVerb = 0
    onlyAdj = 0
    tooManyOrFewWords = 0
    numericWordsInIdentifier = 0
    verbAndVerbPhrase = 0
    booleanIs = 0
    for line in file:
        # read replace the string and write to output file
        split = (line.replace('\\n', '\n').replace('\\t', '\t').split('\t'))
        spiralSplit = ronin.split(split[1])
        identifier_type = split[0]
        identifier = split[1]
        type = split[2].split("=")[0]
        posOutput = " ".join(spiralSplit)
        doc = nlp(posOutput)

        if identifier_type == "IDENTIFIER": # 2011 B,H,&L POS rule #1 
            if any(token.pos_ in ["NOUN","PROPN"] for token in doc):
                pass
            else:
                notANoun += 1
                # print("Java spec Violation: fields should have names that are nouns, noun phrases, or abbreviations for nouns",
                # type + ": " + "'" + identifier + "'")
                # print(identifier)
                # print(doc)
                # print([(token.text, token.pos_) for token in doc])

            if "boolean" not in type:
                for token in doc:
                    if token.tag_ in ["VBZ", "VBP", "VBG", "VB"]:
                        nonBoolPTVerb += 1
                        # print("Violation: non boolean field with present tense verb |", type + ": " + "'" + identifier + "'")
                        # print(identifier) 
            else: 
                if not any(item in ["is", "was", "should"] for item in spiralSplit):
                    boolWithout3PVerb += 1
                    # print("Violation: boolean field names should contain 3rd person forms of the verb \"to be\" or \"should\"  |", 
                    # type + ": " + "'" + identifier + "'")
                    # print(identifier) 

            if len(spiralSplit) not in [2,3,4]:
                print("Violation: identifier should consist of 2, 3, or 4 words", type + ": " + "'" + identifier + "'")
                print(identifier)
                tooManyOrFewWords += 1
                
            
            if not any(token.pos_ not in ["NUM"] for token in doc):
                print("Violation: Identifier name made up only of numeric words and/or values", type + ": " + "'" + identifier + "'")
                print(identifier)
                numericWordsInIdentifier += 1

                
         
        if identifier_type == "IDENTIFIER" and len(spiralSplit) == 1 : # 2011 Binklie et al. "field names should never only be a verb/adjective"
            if doc[0].pos_ == "VERB":
                onlyVerb += 1
                print("Violation: field names should never be only a verb |", type + ": " + "'" + identifier + "'")
                print(identifier) 
            
            if doc[0].pos_ == "ADJ": # 2011 B,H,&L POS rule #3
                onlyAdj += 1
                print("Violation: field names should never be only a adjective |", type + ": " + "'" + identifier + "'")
                print(identifier) 
        
        if identifier_type == "METHOD":
            if any(token.pos_ in ["VERB","AUX"]  for token in doc):
                    pass
            else:
                verbAndVerbPhrase += 1
                print("Java spec Violation: method names should be verbs or verb phrases", 
                type + ": " + "'" + identifier + "'")
                print(identifier)

            if "boolean" in type:
                if spiralSplit[0] != "is":
                    booleanIs += 1
                    print("Java spec Violation: methods that test the boolean condition V of an object should be \"isV\" |", 
                    type + ": " + "'" + identifier + "'")
                    print(identifier)

print("Java spec violations:")
print("fields should have names that are nouns, noun phrases, or abbreviations for nouns, TOTAL: " + str(notANoun))
print("method names should be verbs or verb phrases, TOTAL: " + str(verbAndVerbPhrase))
print("methods that test the boolean condition V of an object should be \"isV\", TOTAL: " + str(booleanIs))
print("Binkley et al (2011) violations:")
print("non boolean field with present tense verb, TOTAL: " + str(nonBoolPTVerb))
print("Violation: boolean field names should contain 3rd person forms of the verb \"to be\" or \"should\", TOTAL: " + str(boolWithout3PVerb))
print("Violation: field names should never be only a verb, TOTAL: " + str(onlyVerb))
print("Violation: field names should never be only a adjective , TOTAL: " + str(onlyAdj))
print("Relf violations:")
print("Violation: Identifier made up only of numeric words/constants, TOTAL: " + str(numericWordsInIdentifier))
print("Violation: Identifier not made up of 2,3, or 4 words, TOTAL: " + str(tooManyOrFewWords))

with  open("csv_data.csv", 'a', newline="") as csv_output: 
    writer = csv.writer(csv_output)
    writer.writerow(["POS"])
    writer.writerow(["Java spec violations"])
    writer.writerow(["Fields not a noun", str(notANoun)])
    writer.writerow(["Methods not a verb", str(verbAndVerbPhrase)])
    writer.writerow(["boolean method without \'is\'", str(booleanIs)])
    writer.writerow(["\n"])
    writer.writerow(["Binkley violations", ""])
    writer.writerow(["non bool present tense verb", str(nonBoolPTVerb)])
    writer.writerow(["bool field non 3rd person verb", str(boolWithout3PVerb)])
    writer.writerow(["field names only verb", str(onlyVerb)])
    writer.writerow(["field names only adjective", str(onlyAdj)])
    writer.writerow(["\n"])
    writer.writerow(["Relf violations", ""])
    writer.writerow(["Identifiers with numeric words/constants", str(numericWordsInIdentifier)])
    writer.writerow(["Identifiers not made up of 2-4 words", str(tooManyOrFewWords)])


