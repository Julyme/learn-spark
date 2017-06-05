package com.learnspark

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

object LocalSpark {
  
	def main(args: Array[String]): Unit = {
		
		val sc = initSpark
		wordCount(sc)
       //findTextInfo(sc)
    }
	
	def initSpark: SparkContext = {
		val conf = new SparkConf()
        conf.setAppName("WordCount") //
            .setMaster("local")

        val sc = new SparkContext(conf)
		sc
	}
	
	def wordCount(sc: SparkContext){
		 
        val filePath = Thread.currentThread().getContextClassLoader.getResource("word.txt").toString()
        //获取文件内容
        val lines = sc.textFile(filePath, 1)
        //分割单词
        val words = lines.flatMap(lines => lines.split(" "))
        val pairs = words.map(word => (word,1))
        val result = pairs.reduceByKey((word, acc) => word + acc)
        //sort by count DESC
        val sorted = result.sortBy(e => {e._2}, false, 1)
        //val mapped = sorted.map(e => (e._2,e._1))
        sorted.foreach(e => println("【" + e._1 + "】出现了" + e._2 + "次"))
        sc.stop()
	}
	
	def findTextInfo(sc: SparkContext){
		 val filePath = Thread.currentThread().getContextClassLoader.getResource("word.txt").toString()
        //获取文件内容
        val lines = sc.textFile(filePath, 1)
        //筛选警告信息
        val warnInfo = lines.filter(line => line.contains("WARN"))
        println("警告信息数目： "+warnInfo.count())
        //取前10条数据
        warnInfo.take(10).foreach(println(_))
	}
	
}