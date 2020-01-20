import pandas as pd

from sklearn.model_selection import cross_val_score
from sklearn import tree
from sklearn.metrics import mean_absolute_error
from sklearn.metrics import mean_squared_error
from sklearn.model_selection import train_test_split

mml_data = pd.read_csv("boston.csv")

column = mml_data.columns[-1] 
X = mml_data.drop(columns=[column]) 
y = mml_data[column] 
test_size = 0.3
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=test_size) 

clf = tree.DecisionTreeRegressor()
clf.fit(X_train, y_train)
scores = cross_val_score(estimator=clf,  X=X, y=y, cv=8,scoring=('neg_mean_absolute_error'))
print('mean_absolute_error', abs(scores.mean()))
scores = cross_val_score(estimator=clf,  X=X, y=y, cv=8,scoring=('neg_mean_squared_error'))
print('mean_squared_error', abs(scores.mean()))