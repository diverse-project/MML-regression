import pandas as pd
from sklearn import tree
mml_data = pd.read_csv('https://raw.githubusercontent.com/acherm/teaching-MDE1920/master/boston/boston.csv', sep=',')

print (mml_data)

test_size = 0.3

clf = tree.DecisionTreeRegressor()
