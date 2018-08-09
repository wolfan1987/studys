package com.zjht.andrewliu.test

import java.io.{FileNotFoundException, FileReader, IOException}

import scala.util.matching.Regex;

/**
  * Created by liudaan on 2016/10/28.
  */
object RegExpTest {

  def main(args: Array[String]) {
        val partten = "Scala ".r;
        val  str = "Scala is Scalable  and cool";
        println(partten  findFirstIn( str));

        val  partten2 = new Regex("(S|s)cala");
        val str2 = "Scala is scalable and cool"
       println((partten2  findAllIn(str2)).mkString(","));

        val  partten3 = "(S|s)cala".r();
        val  str3 = "Scala is scalable and cool";
        println(partten3 replaceFirstIn(str3,"Java"));

       //val  partten4 = new Regex("abl[ae]d+");
        val pattern4 = new Regex("abl[ae]d+")
       val str4 = "ablaw is able1 and cool"
         println((pattern4 findAllIn str4).mkString(","))

      println(System.getenv("usrDir"));

    try{
      val  file = new FileReader("E:\\ideaWorkspace\\myscala\\src\\test.txt");
     val  test = RegExpTest(5);
      println("test="+test);

      test match{
        case RegExpTest(num) => println(test+" is  bigger two times than"+num);
        case _ => println("i cannot calculate");
      }

    }catch{
      case ex:FileNotFoundException =>{
        println("Missing file exception")
      }
      case ex:IOException =>{
        println("IO Exception")
      }
    }finally {
      println("Exiting finally...")
    }
  }

  //对象里的模式匹配提取器（即定义一种逆向操作）
  def   apply(x:Int) =  x*2;   //正向乘法
  def  unapply(z:Int): Option[Int] = if(z%2==0)  Some(z/2) else None;;   //逆向除法



}
