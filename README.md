## Data skew problem 

### Problem description
Running joins of skewed data sets we can face stragglers during execution which slow down whole app. Detail description in [the blog post](https://dataengi.com/2019/02/06/spark-data-skew-problem/)

### Solution

Such slow tasks are mostly results of non-uniform data distribution between workers of spark app. The possible solution is to redistribute large data set between available workes in uniform manner. And in the same time, broadcast smaller data set to all workers pre-sorted by column for consolidation.

Solution of skew problem
 - uniform repartition for large data set (partition number depends from available resources)
 - broadcast for sorted smaller data set (could be shrinked to fewer attributes)

Data set could be repartitioned with 
- DataFrame default repartitioner
- DataFrame repartitioner with special expression based on data columns
- RDD custom repartitioner

Repartition strategy depends of data nature and domain specifics. Are preferred DataFrame/DataSets API.

Additionally, we can tweak solution with executors and cores number for better cluster resources utilisation.

### Run app
step 1. build app assembly
```
$ sbt clean compile assembly
```
step 2. copy assembly jar to cluster
step 3. run spark job on cluster with command
```
spark-submit --class DataSkew --master yarn --deploy-mode client sparkchallenges-assembly-0.1.jar <users-dataset-path> <deps-dataset-path>
```
or with cluster resources tweak (3 executors with 3 cores each)
```
spark-submit --class DataSkew --master yarn --deploy-mode client --num-executors 3 --driver-memory 1G --executor-memory 3G --executor-cores 3 sparkchallenges-assembly-0.1.jar <users-dataset-path> <deps-dataset-path>
```

## References

- Ayan Ray. «Big data skew». Data & Analytics, 05:25:39 UTC. https://www.slideshare.net/ayanray4/big-data-skew-64549151.
«Balancing Spark – Bin Packing to Solve Data Skew». Silverpond (blog), 05, October 2016. https://silverpond.com.au/2016/10/06/balancing-spark/.
- Gao, Yufei, Yanjie Zhou, Bing Zhou, Lei Shi, і Jiacai Zhang. «Handling Data Skew in MapReduce Cluster by Using Partition Tuning». Journal of Healthcare Engineering 2017 (2017). https://doi.org/10.1155/2017/1425102.
- «Handling Data Skew Adaptively In Spark Using Dynamic Repartitioning». Databricks (blog). Accessed 07, November 2018. https://databricks.com/session/handling-data-skew-adaptively-in-spark-using-dynamic-repartitioning.
- Joins for skewed datasets in Spark. Contribute to tresata/spark-skewjoin development by creating an account on GitHub. Scala. 2015. Reprint, Tresata, 2018. https://github.com/tresata/spark-skewjoin.
- «Snapshot». Accessed 07, November 2018. https://silverpond.com.au/2016/10/06/balancing-spark/.

Have fun! Team [@DataEngi](https://github.com/dataengi)

