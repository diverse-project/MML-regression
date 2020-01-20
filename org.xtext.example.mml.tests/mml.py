#Importing python modules
import pandas as pd
from sklearn.model_selection import train_test_split, cross_validate
from sklearn.tree import DecisionTreeRegressor, DecisionTreeClassifier, ExtraTreeClassifier, ExtraTreeRegressor
from sklearn.metrics import mean_absolute_error, mean_squared_error
from sklearn.metrics import mean_absolute_error
mml_data = pd.read_csv('foo2.csv', sep=';')
X = mml_data.drop(columns=["medv"])
y = mml_data["medv"]
test_size = 0.30000000000000004
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=test_size)
clf = DecisionTreeRegressor()
clf.fit(X_train, y_train)
accuracy = mean_absolute_error(y_test, clf.predict(X_test))
print("result : "+str(accuracy))
