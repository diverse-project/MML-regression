import pandas as pd 
from sklearn.model_selection import train_test_split 
import numpy as nps
import xgboost as xgb
from sklearn.ensemble import RandomForestRegressor 
from sklearn.metrics import mean_squared_error 
from sklearn.metrics import mean_absolute_error 
from sklearn.metrics import median_absolute_error
df = pd.read_csv('/home/hugues/Documents/eclipse/workspace/MML-regression/org.xtext.example.mml.tests/programmeMML/Boston.csv')
X = df.drop(columns=['medv'])
y = df['medv']

clf = RandomForestRegressor()
test_size = 0.03
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=test_size)
dtrain = xgb.DMatrix(X_train, label= y_train)
dtest = xgb.DMatrix(X_test, label = y_test)

param = {
	'max_depth' : 3, 
	'eta' : 0.3}
num_round = 20
bst = xgb.train(param, dtrain, num_round)
preds = bst.predict(dtest)

print 'mean_squared_error: {}'.format(mean_squared_error(preds, y_test))
print 'mean_absolute_error: {}'.format(mean_absolute_error(preds, y_test))
print 'mean_absolute_percentage_error: {}'.format(median_absolute_error(preds, y_test))
