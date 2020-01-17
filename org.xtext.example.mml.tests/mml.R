library(dplyr)
library(caTools)
library(rpart)
read.csv("boston.csv",head = TRUE, sep=",")->df
df %>% select(-c(medv))->X
df %>% select(c())->Y
sample.split(df$medv,SplitRatio=70.0)->split_index
train<-subset(df,split_index==T)
test<-subset(df,split_index==F)
fit <- rpart(medv~., data = train, method = 'class', control = rpart.control(cp = 0))
