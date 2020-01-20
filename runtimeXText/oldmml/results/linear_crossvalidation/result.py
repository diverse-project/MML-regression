#!/usr/bin/env python2
import pandas as pd
from sklearn import linear_model
from sklearn.model_selection import cross_validate
#from sklearn.metrics import accuracy_score

# Using pandas to import the dataset
df = pd.read_csv("../BostonHousing.csv", index_col=0)

# Spliting dataset between features (X) and label (y)
col_index = df.shape[1] - 1
coltodrop = [4,5,6,7,col_index]
colnametodrop = []
for i in range(len(coltodrop)):
    colnametodrop.append(df.columns[i])
X = df.drop(colnametodrop, axis=1)
y = df[colnametodrop[-1]]

lasso = linear_model.Lasso()
accuracy = cross_validate(lasso, X, y, cv=8, scoring=('r2','neg_mean_squared_error','explained_variance'), return_train_score=True)

parsedaccuracy = str(accuracy).replace("\'", "\"").replace("array(", "").replace(")", "").replace("0.        ", "0.0")
filename = "data.txt"
with open(filename, "w") as fichier:
    fichier.write(parsedaccuracy)

print("Results stored in \'" + filename + "\'")
