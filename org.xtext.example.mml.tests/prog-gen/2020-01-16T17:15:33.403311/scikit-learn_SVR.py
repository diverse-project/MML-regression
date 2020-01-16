import pandas as pd 
from sklearn.model_selection import train_test_split 
from sklearn.model_selection import cross_val_predict 
from sklearn import svm 
from sklearn.metrics import mean_absolute_error 
from sklearn.metrics import mean_squared_error 
from sklearn.metrics import median_absolute_error
df = pd.read_csv('/home/hugues/Documents/eclipse/runtime-EclipseXtext/testmml/src/Boston.csv')
X = df.drop(columns=['medv'])
y = df['medv']

clf = svm.SVR(kernel='rbf')
y_test = cross_val_predict(clf, X, y, cv=5)


print 'mean_absolute_error: {}'.format(mean_absolute_error(y, y_test))
print 'mean_squared_error: {}'.format(mean_squared_error(y, y_test))
print 'mean_absolute_percentage_error: {}'.format(median_absolute_error(y, y_test))
