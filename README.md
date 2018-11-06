## Data skew problem 

### Problem description
Running joins of skewed data sets we can face stragglers during execution which slow down whole app. 

### Solution

Such slow tasks are mostly results of non-uniform data destribution between workers of spark app. The possible solution is to redistribute large data set between available workes in uniform manner. And in the same time, broadcast smaller data set to all workers pre-sorted by column for consolidation.

Solution of skew problem
 - uniform repartition for large data set (partition number depends from available resources)
 - broadcast for sorted smaller data set (could be shrinked to fewer attributes if not fit in worker memory)

Data set could be repartitioned with 
- DataFrame default repartitioner
- DataFrame repartitioner with special expression based on data columns
- RDD custom repartitioner

Repartition strategy depends of data nature and domain specifics. Are prefered DataFrame/DataSets API.

Additionally, we can tweak solution with executors and cores number for better cluster resources utilisation.

### Run app
step 1. build app assembly
```
$ sbt clean compile assembly
```
step 2. copy assembly jar to cluster
step 3. run spark job on cluster with command
```
spark-submit --class DataSkew --master yarn --deploy-mode client sparkchallenges-assembly-0.1.jar
```
or with cluster resouses tweak (3 executors with 3 cores each)
```
spark-submit --class DataSkew --master yarn --deploy-mode client --num-executors 3 --driver-memory 1G --executor-memory 3G --executor-cores 3 sparkchallenges-assembly-0.1.jar
```