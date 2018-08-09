/**
  * 支持包嵌套
  */
package com.zjht.andrewliu.test


/**
  * Created by liudaan on 2016/10/26.
  */
object Test {


  def main(args: Array[String]) {

   val exec : society.professional.Executive = new   society.professional.Executive;
    val exec2 : society.professional.Exec2 = new   society.professional.Exec2;
    val exec3 : society.Exec3 = new   society.Exec3;
    exec.help(exec);
    exec.help2();
    exec2.help2(exec);
    exec3.help3(exec);


  }

}

package  society{
  package  professional{

    //可以对Class中的私有变量限定其对久访问的包的范围
    class Executive {
      //只能在 professional 包中访问
      private[professional] var workDetails = null;
      //只能在 society包中才能访问
      private[society] var friends = null;
      //只能在当前对象中才能访问
      private[this] var secrets = null;

      def help(another : Executive) {
        println("Executive.help.workDetails="+another.workDetails);
        ///println(another.secrets) //ERROR
        println("Executive.help.friends="+another.friends);
      }

      def help2( ): Unit ={
        println("Executive.help2.secrets="+secrets);
      }
    }


    class Exec2{
      def help2(another : Executive ): Unit ={
        println("Exec2.help2.workdetails="+another.workDetails);
        println("Exec2.help2.friends="+another.friends);
      }
    }
  }

  class Exec3{
    def help3(another : professional.Executive ): Unit ={
      //println(another.workDetails);     workDetails只能在professional包内访问
      println("exec3.helper3.friends="+another.friends);
    }
  }

}



