import pandas as pd 
from sklearn.model_selection import train_test_split 
from sklearn.model_selection import cross_val_predict 
from sklearn.ensemble import RandomForestRegressor 
from sklearn.metrics import mean_squared_error 
from sklearn.metrics import mean_absolute_error 

df = pd.read_csv('/home/hugues/Documents/eclipse/workspace/MML-regression/org.xtext.example.mml.tests/programmeMML/Boston.csv')
X = df.drop(columns=['medv'])
y = df['medv']

clf = RandomForestRegressor()
y_test = cross_val_predict(clf, X, y, cv=5)


print 'mean_squared_error: {}'.format(mean_squared_error(y, y_test))
print 'mean_absolute_error: {}'.format(mean_absolute_error(y, y_test))
