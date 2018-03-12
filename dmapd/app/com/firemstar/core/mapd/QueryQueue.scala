package com.firemstar.core.mapd

import java.util.concurrent.ConcurrentLinkedQueue

/**
 * sql query를 queue에 저장하고, 관리한다. 
 */
object QueryQueue {
  
  val queue = new ConcurrentLinkedQueue[MapdQuery]()
  
  def push(query: MapdQuery) =  {
    queue.add(query)  
  }
  
  def poll() : Option[MapdQuery] =  {
    if(queue.isEmpty() == true) return None
    Some(queue.poll())
  }
  
  
}