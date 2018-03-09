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

    "Test jdbc" in {
      Logger.info("Test JDBC ...")  
      val mapd = new MapdEngine(host, port, dbname, user, passwd)
      mapd.open()
      val sql = "select * from flights_2008_7M limit 100"
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
    }

  }
  
}