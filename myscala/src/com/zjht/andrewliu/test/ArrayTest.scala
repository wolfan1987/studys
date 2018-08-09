package com.zjht.andrewliu.test

/**
  * Created by liudaan on 2016/10/27.
  */
object ArrayTest {

    def main(args:Array[String]){

        var  array1:Array[String] = new Array[String](3);
        array1(0)="aaaaa";  array1(1) = "bbbbb";   array1(2) = "cccccc";

        var  array2 =  Array("111","2222","33333");
      var  totalValue:Array[String] = new Array[String](array2.length);
       for(a <-  array1){
            printf(" array1.value: %s",a);
            println();
       }

      for(b <- 0  to  array2.length-1){
          printf(":array2.value: %s",array2(b));
        println();
        totalValue(b) = array2(b);
      }
    var bb:String = "";
      for(c <- 0 to totalValue.length-1){
        bb=bb.concat(totalValue(c));
      }
      println();
      printf("totalValue=%s",bb);

      //多维数组(二维,3,4,5)
      var twoDim = Array.ofDim[Int](3,3);
      for(i <- 0 to 2){
        for(j <- 0 to 2){
           twoDim(i)(j) = i+j;
        }
      }
      println();
      for(i<- 0 to 2){
          for(j <- 0 to 2){
            print(twoDim(i)(j));
          }
        println("-------------");
      }
      var threeDim = Array.ofDim[Int](3,3,3);
      threeDim(0)(0)(0) = 1;
      threeDim(0)(0)(1) = 2;
      threeDim(0)(0)(2) = 3;
      for(i <- 0 to 2){
          for(j <- 0 to 2){
            for(k <- 0 to 2){
              print(threeDim(i)(j)(k));
            }
            println();
          }
        println();
      }

      var  emptyArray = new Array[String](array1.length+array2.length);
      //多个数组连接在一起，形成一个新的数组
      emptyArray = Array.concat(array1,array2);
      for(x  <-  emptyArray){
        println(x);
      }

      //创建范围数组
      var numList = Array.range(10,20,2);
      var numList2 = Array.range(10,20);
      for( d <- numList){
         print("   d="+d);
      }
      println();
      for( d <- numList2){
        print("  d="+d);
      }
      println();
    val  newArrays = Array.apply(Int,1,2,3,4,5);
      for(d <- newArrays){
         print("  d="+d);
      }

    }
}
