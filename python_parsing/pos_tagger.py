# This file is not relevant to the testing/evaluation process of repos. 
# It's here as a quick way to check how spaCy processes certain individual words or identifiers you may be curious about. 
import spacy 

nlp  = spacy.load('en_core_web_lg')
doc = nlp("oneHundred")
for token in doc:
    print(token.text, token.pos_)