package com.firemstar.core.mapd

import java.sql.Connection
import java.sql.Statement
import java.sql.DriverManager
import java.sql.SQLException
import play.api.Logger
import java.sql.ResultSet
import java.sql.ResultSetMetaData
import java.util.HashMap
import java.util.ArrayList
import java.io.StringWriter
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.ByteArrayOutputStream
import de.undercouch.bson4jackson.BsonFactory
import java.io.ByteArrayInputStream
import com.fasterxml.jackson.core.`type`.TypeReference
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.databind.DeserializationFeature

class MapdEngine(host : String, port:String, 
    dbname:String, user:String, password:String) {
  
  val JDBC_DRIVER = "com.mapd.jdbc.MapDDriver"
  //val DB_URL = "jdbc:mapd:localhost:9091:mapd";
  var conn : Connection = null
  var stmt : Statement = null
  var use : Boolean = false
  
  

  def open() : Boolean = {
    val dburl = String.format("jdbc:mapd:%s:%s:%s", host, port, dbname)
    try {
        Class.forName(JDBC_DRIVER);
        conn = DriverManager.getConnection(dburl, user, password);
        stmt = conn.createStatement();
        return true
      } catch {
        case e : SQLException => Logger.error(e.getMessage)
        case e1 : Exception => Logger.error(e1.getMessage)
      }
      return false
  }
  
  def close() = {
    if(conn != null) conn.close()
    if(stmt != null) stmt.close()
  }
  
  def select(sql : String) : BResult = {
    val rs = stmt.executeQuery(sql)
    val rows = new ArrayList[ArrayList[String]]
    val columns = new ArrayList[String]
    var sw = 0
    while(rs.next()) {
      val rmd = rs.getMetaData()
      if(sw == 0) {
        for(i <- 1 to rmd.getColumnCount) {
           columns.add(rmd.getColumnName(i))
        } // for...
        sw = 1
      }
      val row = new ArrayList[String]
      for(i <- 1 to rmd.getColumnCount) {
         row.add(rs.getString(rmd.getColumnName(i)))
      }
      rows.add(row)
    }
    val ret = new BResult(columns, rows)
    rs.close()
    Logger.info(">>>> ret :" + ret.toString())
    return ret
  }
  
  def toJson(re : BResult) : String = {
    val mapper = new ObjectMapper() with ScalaObjectMapper
    mapper.registerModule(DefaultScalaModule)
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    mapper.writeValueAsString(re)
  }
  
  def toBResult(json : String) : BResult = {
    val mapper = new ObjectMapper() with ScalaObjectMapper
    mapper.registerModule(DefaultScalaModule)
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    val typeRef = new TypeReference[BResult](){}
    mapper.readValue[BResult](json, typeRef)
    
  }
  
  def toBson(re : BResult) : ByteArrayOutputStream = {
    val baos = new ByteArrayOutputStream();
    val mapper = new ObjectMapper(new BsonFactory()) with ScalaObjectMapper
    mapper.registerModule(DefaultScalaModule)
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    mapper.writeValue(baos, re);
    return baos
  }
  
  def toBResult(ar : ByteArrayOutputStream) : BResult = {
    val mapper = new ObjectMapper(new BsonFactory()) with ScalaObjectMapper
    mapper.registerModule(DefaultScalaModule)
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    val typeRef = new TypeReference[BResult](){}
    val re = mapper.readValue[BResult](ar.toByteArray, typeRef );
    return re
  }
  
  def toBResult(ar : Array[Byte]) : BResult = {
    val mapper = new ObjectMapper(new BsonFactory()) with ScalaObjectMapper
    mapper.registerModule(DefaultScalaModule)
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    val typeRef = new TypeReference[BResult](){}
    val re = mapper.readValue[BResult](ar, typeRef );
    return re
  }
  
  
  def update(sql : String) : Boolean  = {
    return stmt.execute(sql)
  }
  
  def delete(sql : String) : Boolean = {
    return stmt.execute(sql) 
  }
  
  
     
}