package com.zjht.andrewliu.test

import java.util.Date;

/**
  * Created by liudaan on 2016/10/26.
  */
object FunctionTest {

  def main(args: Array[String]) {
    //此时传进去的函数，不像java中先执行再作为参数传进去，而先将函数传进去，并告知调用函数其返回类型
    //当在函数内部实际使用引用结果时，再调用函数
   // delayed(time());
    //paramNameCall(b=10,a=20);
  //  for(i <- 1 to 10){
  //    println("factorial  "+i+" value = "+factorial(i));
  //  }
    //paramChangeCall(1,"aaaa","bbbb","cccc","eeeee");
    //5,6,7
  //  defaultParamFunc();
    //覆盖默认a=5
   // defaultParamFunc(a=8);
    //用apply调用layout函数，并以10作为参数传给layout
    //println(apply(layout,10));
    //匿名函数(一行文本搞定的函数
    val inc = (x:Int) => x+1;
    println(inc(3));
    val  incb = inc(7)-1;
     println(incb);
    val  mul = (x:Int,y:Int) => x+y;
    println(mul(3,4));
    val userDir = () => { System.getProperty("user.dir")};
    println(userDir());

    def  strCat1(str1:String)(str2:String)=str1+str2;
    println(strCat1("abc")("defg"));
    def  strCat2(str1:String)=(str2:String)=>str1+str2;
    println(strCat2("abfff")("gggggg"));
    println(strCat("11111")("22222222"));

   // println(FACTORIAL(2));
   val  date = new Date();
    //表示logWithDateBound指向了log的引用，并且指定date为不变值，message可变值
   val logWithDateBound= log(date,_:String);
    logWithDateBound("message------1");
    Thread.sleep(1000);
    logWithDateBound("message------2");
    Thread.sleep(2000);
    logWithDateBound("message------3");

    //闭包函数（不引用自身范围之外的函数),
    var  factory = 3;
    //这里muiltValue函数引用了外部的参数factory，如果不引用，则muitValue为闭包函数
    val  muiltValue = (i:Int)=> i*factory;
    println(muiltValue(2));

  }


  //部分应用函数
  def log(date : Date, message :String): Unit ={
    println(date + "----" + message)
  }

  //嵌套函数:(函数里面定义一个函数，此内函数只能在外部函数中调用
  def FACTORIAL(i: Int): Int = {
    def fact(i: Int, accumulator: Int): Int = {
      if (i <= 1)
        accumulator
      else
        fact(i - 1, i * accumulator)
    }
    fact(i, 1)
  }


  //柯里转换函数接受多个参数成一条链的函数，每次取一个参数。柯里函数是具有多个参数列表定义
  def strCat(str1: String)(str2:String): String ={
     return str1+str2;
  }

  //高阶函数，类似于Javascript的apply,即将A函数和B值，组合成另一个签名的函数并调用，即用apply函数的第一个参数是函数，第二个参数是第一个函数的参数
  def  apply(f : Int => String,v :Int) = f(v);
  def layout[A] (x:A) = "["+x.toString+"]";

  //默认参数函数,调用时可以不传参数
  def defaultParamFunc( a:Int = 5,b:Int = 6,c:Int = 7): Unit ={
    println("a="+a);
    println("b="+b);
    println("c="+c);
  }


  //可变参数函数，最后一个参数的长度为一个数组
  def  paramChangeCall(argInt: Int ,args : String*): Unit ={
      println("argInt="+argInt);
    for(str <-  args){
      println("argstr="+str);
    }
  }

  //递归函数，求阶乘
  def  factorial(n : BigInt): BigInt ={

    if(n<=1){
        return  1;
    }else{
      return n*factorial(n-1);
    }

  }

  //按参数名来调用函数
  def  paramNameCall(a : Int,b : Int): Unit ={
      println(" a.value="+a);
      println(" b.value="+b);
  }

  //定义一个打印当前时间的函数,
  def  time(): Long ={
      println("get Current Time ......");
      return System.nanoTime();  //不加 return 会默认会将此值返回(Long)
  }
  //定义一个调用打印当前时间的函数，此时t只是引用了time()函数，而不会执行些函数
  def delayed(t : => Long): Unit ={
    println("In delayed Method");
    println("Param:"+t);  //在这时才执行函数
    t;  //这里再执行一次函数
  }

}
