package com.firemstar.core.mapd

import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.test._
import play.api.test.Helpers._
import play.api.Logger
import java.io.ByteArrayInputStream

class TestMapd extends PlaySpec with GuiceOneAppPerTest with Injecting {
  
  val dbname = "mapd"
  val user = "mapd"
  val host = "localhost"
  val passwd = "HyperInteractive"
  val port = "9091"
  
  "MapdEngine Test" should {
   
    /*
    "Test create table2" in {
      val mapd = new MapdEngine(host, port, dbname, user, passwd )
      mapd.open()
      //val sql = "select * from flights_2008_7M limit 10"
      //mapd.createUser("moonstar", "wooag01")
      mapd.createDatabase("TestTB1", "moonstar")
      mapd.close()
      
    } */

    /*
    "Test jdbc" in {
      Logger.info("Test JDBC ...")  
      val mapd = new MapdEngine(host, port, dbname, user, passwd)
      mapd.open()
      val sql = "select * from flights_2008_7M limit 10"
      val result = mapd.select(sql)
      val json = mapd.toJson(result)
      Logger.info("ret json :" + json)
      
      val re = mapd.toBResult(json)
      Logger.info("return : " + re.toString())
     
      val bson = mapd.toBson(mapd.select(sql))
      Logger.info("ret bson : " + bson.toByteArray())
      Logger.info("byte array : " + bson.toByteArray().length)
      
      //val list = mapd.toResultList(bson.toByteArray())
      //Logger.info(">>>>> list : " + list)
      val obj = mapd.toBResult(bson.toByteArray())
      Logger.info(">>>> result : " + obj.toString())
      mapd.close()
    }*/
   
    /*
    "Test PoolMapd Engine" in {
      val db = PoolMapdEngine.getMapdEngine(host, port, dbname, user, passwd)  
      if(db.isEmpty) {
        Logger.info("engine을 얻어올수 없습니다. ")
      } else {
        val sql = "select * from flights_2008_7M limit 10"
        val json = db.get.toJson(db.get.select(sql))
        Logger.info("json : " + json)
        
        val sql2 = "select * from flights_2008_7M limit 10"
        val json2 = db.get.toJson(db.get.select(sql2))
        Logger.info("json2 : " + json2) 
      }
      
      val db2 = PoolMapdEngine.getMapdEngine(host, port, dbname, user, passwd)  
      if(db2.isEmpty) {
        Logger.info("engine을 얻어올 수 없습니다. ")
      } else {
        val sql = "select * from flights_2008_7M limit 10"
        val json = db2.get.toJson(db2.get.select(sql))
        Logger.info("json : " + json)
        
        val sql2 = "select * from flights_2008_7M limit 10"
        val json2 = db2.get.toJson(db2.get.select(sql2))
        Logger.info("json2 : " + json2) 
      }
      
     PoolMapdEngine.unUsed(db2.get)
     
      val db3 = PoolMapdEngine.getMapdEngine(host, port, dbname, user, passwd)  
      if(db3.isEmpty) {
        Logger.info("engine을 얻어올 수 없습니다. ")
      } else {
        val sql = "select * from flights_2008_7M limit 10"
        val json = db3.get.toJson(db3.get.select(sql))
        Logger.info("json : " + json)
        
        val sql2 = "select * from flights_2008_7M limit 10"
        val json2 = db3.get.toJson(db3.get.select(sql2))
        Logger.info("json2 : " + json2) 
      }
     PoolMapdEngine.printPoolInfo()
     PoolMapdEngine.releasePool()
    } */
   
    "Test create table " in {
      Logger.info("Test JDBC ...")  
      val db = PoolMapdEngine.getMapdEngine(host, port, dbname, user, passwd)  
      if(db.isEmpty) {
        Logger.info("engine을 얻어올수 없습니다. ")
      } else {
        db.get.createUser("moonstar2", "wooag01")
        db.get.createDatabase("mtest", "moonstar")
      }
     PoolMapdEngine.printPoolInfo()
     PoolMapdEngine.releasePool()
    } 

  }
  
}