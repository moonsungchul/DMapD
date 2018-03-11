package com.firemstar.core.mapd

import play.api.Logger
import scala.collection.mutable.HashMap
import scala.collection.mutable.ListBuffer
import java.util.concurrent.ConcurrentHashMap

object PoolMapdEngine {
  
  val pool = new ConcurrentHashMap[String, ListBuffer[MapdEngine]]
 
  /**
   * use가 false 되어 있는 MapdEngine을 찾아 리턴한다. 만약 없으면 새롭게 생성하고 리턴한다.  
   */
  def getMapdEngine(host : String, port:String, dbname:String, user:String, passwd:String) : Option[MapdEngine] = {
    val key = String.format("%s:%s:%s:%s:%s", host, port, dbname, user, passwd)
    if(pool.containsKey(key) == false) {
       val list = ListBuffer.newBuilder[MapdEngine]  
       val engine = new MapdEngine(host, port, dbname, user, passwd)
       if(engine.open() == true) {
         engine.use = true
         list += engine
         pool.put(key, list.result())
         return Some(engine)
       } else {
         Logger.info("mapd을 연결하는데 실패했습니다.")
         return None
       }
     } else {
       Logger.info("????????????????/")
       val ll = pool.get(key)
       ll.result().foreach{ v =>
         Logger.info(">>>>>>> v : " + v.toString())
          if(v.use == false) return Some(v) 
       }
       val engine = new MapdEngine(host, port, dbname, user, passwd)
       if(engine.open() == true) {
         engine.use = true
         ll += engine
         return Some(engine)
       } else {
         Logger.info("mapd을 연결하는데 실패했습니다.")
         return None
       }
       
     }
  }
  
  def unUsed(engine : MapdEngine) = {
   engine.use = false 
  }
  
  
  def releasePool() = {
    this.pool.forEach{ (k, v) =>
      v.foreach(f => 
         f.close() 
       )
    }
  }
  
  def printPoolInfo() = {
    Logger.info("pool : " + pool.toString())
    Logger.info(this.pool.size().toString())
    this.pool.forEach{ (k, v) => 
      val pp =  "pool info => %s: %d".format(k.toString(), v.length)
      Logger.info(pp)
    }
  }
  
  
  
  
}