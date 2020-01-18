import pandas as pd

from sklearn import tree
from sklearn.metrics import mean_absolute_error
from sklearn.model_selection import train_test_split

mml_data = pd.read_csv("boston.csv")
y = mml_data['medv'] 
column = "medv"
X = mml_data.drop(columns=[column]) 
test_size = 0.30000000000000004
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=test_size) 

clf = tree.DecisionTreeRegressor()
clf.fit(X_train, y_train)
y_pred =  clf.predict(X_test)
accuracy = mean_absolute_error(y_test, y_pred)
print('mean_absolute_error:', accuracy)