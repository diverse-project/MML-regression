import pandas as pd 
from sklearn.model_selection import train_test_split 
from sklearn.ensemble import RandomForestRegressor 
from sklearn.metrics import mean_squared_error 
from sklearn.metrics import mean_absolute_error 

df = pd.read_csv('/home/hugues/Documents/eclipse/workspace/MML-regression/org.xtext.example.mml.tests/programmeMML/Boston.csv')
X = df.drop(columns=['medv'])
y = df['medv']

clf = RandomForestRegressor()
test_size = 0.03
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=test_size)
clf.fit(X_train, y_train)
y = clf.predict(X_test)

print 'mean_squared_error: {}'.format(mean_squared_error(y, y_test))
print 'mean_absolute_error: {}'.format(mean_absolute_error(y, y_test))
