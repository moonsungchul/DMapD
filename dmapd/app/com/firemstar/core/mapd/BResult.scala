package com.firemstar.core.mapd

import java.util.ArrayList

case class BResult (columns : ArrayList[String], rows : ArrayList[ArrayList[String]]) {
  

  override
  def toString() : String = {
    val buf = StringBuilder.newBuilder
    //for(v <-  this.columns) {
    columns.forEach{ v =>
      buf.append(v).append("\t")  
    }
    buf.append("\n")
    rows.forEach{ v =>
      v.forEach{ j=> 
        buf.append(j).append("\t")  
      }
      buf.append("\n")
    }
    buf.toString()
  }
    
}