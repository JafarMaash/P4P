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
            if type != "boolean":
                for token in doc:
                    # print(token.text, token.tag_)
                    if token.tag_ in ["VBZ", "VBP", "VBG", "VB"]:
                        print("Violation: non boolean field with present tense verb |", type + ": " + "'" + identifier + "'")
                        print(identifier) 
            else: 
                for token in doc:
                    if token.tag_ in ["MD"]
                # boolean field names should contain 3person forms of the verb to be or should 


        
         
        if identifier_type == "IDENTIFIER" and len(doc) == 1 : # 2011 B,H,&L POS rule #2
            if doc[0].pos_ == "VERB":
                print("Violation: field names should never be only a verb |", type + ": " + "'" + identifier + "'")
                print(identifier) 
            
            if doc[0].pos_ == "ADJ": # 2011 B,H,&L POS rule #3 
                print("Violation: field names should never be only a adjective |", type + ": " + "'" + identifier + "'")
                print(identifier) 
        

         
        # print(type)
        # print(split)
        # print(doc)