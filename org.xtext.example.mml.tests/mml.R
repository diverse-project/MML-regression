library(dplyr)
library(caTools)
library(Metrics)
library(rpart)
read.csv("boston.csv",head = TRUE, sep=",")->df
df %>% select(-c(medv))->X
df %>% select(c())->Y
sample.split(df$medv,SplitRatio=70.0)->split_index
train<-subset(df,split_index==T)
test<-subset(df,split_index==F)
fit1 <- rpart(medv~., data = train, method = 'class', control = rpart.control(cp = 0))
result1<-predict(fit1, test, type = 'class')test %>% select(c(medv))->testY
testY2 <- testY[,1:length(testY)]
mae(testY3, result)