package com.zjht.andrewliu.test

import java.io.{File, PrintWriter}

import scala.io.Source

/**
  * Created by liudaan on 2016/10/28.
  */
object FileTest {


  def main(args:Array[String]): Unit ={
    //  val writer = new PrintWriter("E:\\ideaWorkspace\\myscala\\src\\test.txt");
  //  writer.write("hhhhhhhhhhhhhhhhhhhhhhhhddddd");
  //  writer.close();

   // val line = Console.readLine();

  ///  println("Thanks, you just typed: " + line)

    readFileContent();

  }


  def  readFileContent(): Unit ={
    println("-----beging  read file------");
    Source.fromFile("E:\\ideaWorkspace\\myscala\\src\\test.txt").foreach(
      print
    )
  }

}
