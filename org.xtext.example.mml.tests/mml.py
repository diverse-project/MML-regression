import pandas as pd
from sklearn import tree
from sklearn.model_selection import train_test_split
from sklearn.metrics import mean_squared_error
mml_data = pd.read_csv('https://raw.githubusercontent.com/acherm/teaching-MDE1920/master/boston/boston.csv', sep=',')
X = df[df.columns.difference([1,2,3])]
y = df[columns = [df.columns[0]]]
clf = tree.DecisionTreeRegressor()
test_size = 30
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=30)
accuracy0=mean_squared_error(y_test, clf.predict(X_test))
print(accuracy0)

print (mml_data)
