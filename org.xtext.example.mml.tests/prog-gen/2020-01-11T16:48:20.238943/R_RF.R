library(caret)

data <- read.csv('/home/hugues/Documents/eclipse/workspace/MML-regression/org.xtext.example.mml.tests/programmeMML/Boston.csv') 

param_train <- trainControl(method = 'cv', number = 5)
training <- data 

fit <- train(medv ~  ., data = training, method='rf', trControl=param_train) 


pred <- predict(fit)


postResample(pred = pred, obs = training$medv)[2]
postResample(pred = pred, obs = training$medv)[3]
