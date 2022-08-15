import spacy 

nlp  = spacy.load('en_core_web_lg')
doc = nlp("Where are you?")
for token in doc:
    print(token.text, token.pos_)