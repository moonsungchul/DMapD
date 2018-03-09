package com.firemstar.core.mapd

import play.api.Logger
import scala.collection.mutable.HashMap
import scala.collection.mutable.ListBuffer

object PoolMapdEngine {
  
  val pool = HashMap.newBuilder[String, ListBuffer[MapdEngine]]
 
  /**
   * use가 false 되어 있는 MapdEngine을 찾아 리턴한다. 만약 없으면 새롭게 생성하고 리턴한다.  
   */
  def getMapdEngine(host : String, port:String, dbname:String, user:String, passwd:String) : Option[MapdEngine] = {
    val key = String.format("%s:%s:%s:%s:%s", host, port, dbname, user, passwd)
    if(pool.result().contains(key) == false) {
       val list = ListBuffer.newBuilder[MapdEngine]  
       val engine = new MapdEngine(host, port, dbname, user, passwd)
       if(engine.open() == true) {
         engine.use = true
         list += engine
         pool.result().put(key, list.result())
         return Some(engine)
       } else {
         Logger.info("mapd을 연결하는데 실패했습니다.")
         return None
       }
     } else {
       val ll = pool.result().get(key)
       val dlist = ll.get
       dlist.foreach{ v =>
          if(v.use == false) return Some(v) 
       }
       val engine = new MapdEngine(host, port, dbname, user, passwd)
       if(engine.open() == true) {
         engine.use = true
         dlist += engine
         return Some(engine)
       } else {
         Logger.info("mapd을 연결하는데 실패했습니다.")
         return None
       }
       
     }
  }
  
  
  
  
}