import spacy

from spiral import ronin

nlp  = spacy.load('en_core_web_lg')
with open('test.txt') as file:
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
            if any(token.pos_ == "NOUN" for token in doc):
                pass
            else:
                print("Java spec Violation: fields should have names that are nouns, noun phrases, or abbreviations for nouns", 
                type + ": " + "'" + identifier + "'")
                print(identifier)

            if "boolean" not in type:
                for token in doc:
                    if token.tag_ in ["VBZ", "VBP", "VBG", "VB"]:
                        print("Violation: non boolean field with present tense verb |", type + ": " + "'" + identifier + "'")
                        print(identifier) 
            else: 
                if not any(item in ["is", "was", "should"] for item in spiralSplit):
                    print("Violation: boolean field names should contain 3rd person forms of the verb \"to be\" or \"should\"  |", 
                    type + ": " + "'" + identifier + "'")
                    print(identifier) 

                
         
        if identifier_type == "IDENTIFIER" and len(doc) == 1 : # 2011 Binklie et al. "field names should never only be a verb/adjective"
            if doc[0].pos_ == "VERB":
                print("Violation: field names should never be only a verb |", type + ": " + "'" + identifier + "'")
                print(identifier) 
            
            if doc[0].pos_ == "ADJ": # 2011 B,H,&L POS rule #3 
                print("Violation: field names should never be only a adjective |", type + ": " + "'" + identifier + "'")
                print(identifier) 
        
        if identifier_type == "METHOD":
            if any(token.pos_ == "VERB" for token in doc):
                    pass
            else:
                print("Java spec Violation: method names should be verbs or verb phrases", 
                type + ": " + "'" + identifier + "'")
                print(identifier)

            if "boolean" in type:
                if spiralSplit[0] != "is":
                    print("Java spec Violation: methods that test the boolean condition V of an object should be \"isV\" |", 
                    type + ": " + "'" + identifier + "'")
                    print(identifier) 
