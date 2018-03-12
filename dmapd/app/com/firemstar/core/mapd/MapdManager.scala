package com.firemstar.core.mapd

import com.firemstar.core.mapd.QueryQueue


object MapdManager {
  
  
  /**
   * query을 query queue에 저장한다. 
   */
  def qsubQuery(so_host: String, so_port : String, query : String) = {
    val qq = new MapdQuery(so_host, so_port, query)
     QueryQueue.push(qq) 
  }
  
  
}