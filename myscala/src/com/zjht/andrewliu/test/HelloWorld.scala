package com.zjht.andrewliu.test

import scala.collection.immutable.HashSet
import scala.util.control.Breaks
;
;

/**
  * Created by liudaan on 2016/10/26.
  */
object HelloWorld {

  def main(args: Array[String]) {
    val  abc = "aaaaaaaaaa";
    val  abc123 =
      """11111|111|111333 455454asdfasd
        asdfasdfasdff""";
    //可变变量
    var temp:String = "123434545454";
    var temp2 = "aaaaaaaaaaaaa";
   //不可变变量
    val valtemp:String = "1dddddddd";

    test(args);

    println("Hello  World!!"+abc123+abc+temp+valtemp+temp2);
  }

  def  test(args:Array[String]): Unit ={
      //多重任务+无组(自定义类型）
      val (myIntValue : Int, myStrValue : String)=Pair(40,"Foo");
    //推断类型的无组
    var (intValue,strValue ) = Pair(50,"abcd");

    println(intValue,strValue);
    println(myStrValue,myIntValue);

      var    outClass: Outter = new Outter;

   println(outClass.myPrintInfo("111111"));
    println(outClass.toString);
    outClass = new Outter;

    println(outClass.myPrintInfo("222222"));
    println(outClass.toString);

    outClass = new Outter2;
    println(outClass.myPrintInfo("dddddd"));
    println(outClass.toString);

    var j = 0;  //包括10
    for( j <- 1 to 10 ){
      println("j="+j);
    }
  var i = 0;   //不包括10
    for( i <- 1 until  10){
      println("i="+i);
    }

    //同时在循环中指定多个范围，相当于嵌套循环，三个范围 就是3个java中的for 循环
    var a = 0;
    var b = 0;
    var c = 0;
    for(a <-  1 to 3; b <- 1 to 3; c <- 1 to 3){  //多范围嵌套循环
     //  println("a= "+a+"b="+b+"c="+c);
    }

    //for循环遍历集合
    val  list:List[Int]  = List(1,2,3,4,5,6);
    val  strSet:Set[String] = HashSet("aaa","bbb","ccc","ddd");

    //得到循环中满足条件的值
    val  returnInts = for{i <- list if i !=2; if i>3}yield i;
    //迭代满足的值
    var temp = 0;
    for(temp <-  returnInts){
       println("temp="+temp);
    }

    for(i <- list  if i !=2;if i>3){  // 加上if过滤器
      //  println("int.i="+i);
    }

    //在循环中用break
    temp = 0;
    val  loop = new Breaks;
    loop.breakable{
        for(temp <- list){
          println("no break.temp="+temp);
            if(temp==4){
              loop.break;
            }
        }
    };
    println("break loop!");
    for(str <- strSet){
        println("str="+str);
    }

    //打断嵌套循环
    var ia = 0;
    var  ib = 0;
    val  iaList = List(1,2,3,4,5,6);
    val  ibList = List(2,3,4,5,6,7);
    val outer = new Breaks;
    val  innter = new Breaks;
    outer.breakable{
       for(ia <- iaList){
         println("Value of ia="+ia);
         innter.breakable{
            for(ib <- ibList){
               println("Value of ib = "+ib);
              if(ib == 6){  //当ib==6时，中断本次内循环
                innter.break;
              }
            }
         }
       }
    };

  }

}

/**
  * 符号常量'x是简写的表达scala.Symbol(“X”)。Symbol是一个类，它的定义如下
  *
  * @param name
  */
final  case  class Symbol private(name: String){
  override  def toString:String = ""+name;
}

class Outter{
  class Innter{
    private  def  innerMethod(args: String): Unit ={
       println("innerMethod-----"+args);
    }

     def  innerMethod2(intArgs:Int ): String ={
        return "innerMethod2="+intArgs;
    }

    class Inner2{
       innerMethod("abcde");
    }

  }
     protected  def  myPrintInfo2(strArg:String): String ={
      return  strArg+(new Innter).innerMethod2(44);;
    }
      def  myPrintInfo(strArg:String): String ={
        return  strArg+(new Innter).innerMethod2(44);;
      }
}

class Outter2 extends   Outter{
    def  myPrintInfo(): Unit ={
      println("son printinfo-------");
      myPrintInfo2("333333");
    }
}


