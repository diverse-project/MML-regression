#!/usr/bin/env python2
#Importing python modules
import pandas as pd
from sklearn.model_selection import train_test_split
from sklearn import linear_model
from sklearn.metrics import mean_squared_error

df = pd.read_csv("../BostonHousing.csv", index_col=0)

# Spliting dataset between features (X) and label (y)
col_index = df.shape[1] - 1
coltodrop = [4,5,6,7,col_index]
colnametodrop = []
for i in range(len(coltodrop)):
    colnametodrop.append(df.columns[i])
X = df.drop(colnametodrop, axis=1)
y = df[colnametodrop[-1]]

# Spliting dataset into training set and test set
test_size = 0.3
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=test_size)

# Set algorithm to use
clf = linear_model.Lasso()

# Use the algorithm to create a model with the training set
clf.fit(X_train, y_train)

# Compute and display the mean_squared_error
accuracy = mean_squared_error(y_test, clf.predict(X_test))

filename = "data.txt"
with open(filename, "w") as fichier:
    fichier.write(str(accuracy))

print("Results stored in \'" + filename + "\'")
