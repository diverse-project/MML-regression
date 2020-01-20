library(dplyr)
library(caTools)
library(Metrics)
<<<<<<< HEAD
library(rpart)
read.csv("boston.csv",head = TRUE, sep=",")->df
=======
library(randomForest)
read.csv("boston.csv",head = TRUE, sep=";")->df
>>>>>>> 4e39d0dd6f8e55f64effb64794fec94df2bdcf58
df %>% select(-c(medv))->X
df %>% select(c(medv))->Y
sample.split(df$medv,SplitRatio=70.0)->split_index
train<-subset(df,split_index==T)
test<-subset(df,split_index==F)
fit <- rpart(medv~., data = train, method = 'class', control = rpart.control(cp = 0))
result1<-predict(fit, test, type = 'class')
result <- as.numeric(levels(result1))[result1]
test %>% select(c(medv))->testY
testY2 <- testY[,1:length(testY)]
mae(testY2, result)
mse(testY2, result)
