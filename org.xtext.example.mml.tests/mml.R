library(dplyr)
library(caTools)
library(Metrics)
library(randomForest)
read.csv("boston.csv",head = TRUE, sep=",")->df
df %>% select(-c(medv))->X
df %>% select(c())->Y
sample.split(df$medv,SplitRatio=70.0)->split_index
train<-subset(df,split_index==T)
test<-subset(df,split_index==F)
fit <- randomForest(medv~., data = train, method = 'class')
result<-predict(fit, test, type = 'class')
test %>% select(c(medv))->testY
testY2 <- testY[,1:length(testY)]
mae(testY2, result)
mse(testY2, result)
