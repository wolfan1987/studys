package com.zjht.andrewliu.test

/**
  * Created by liudaan on 2016/10/27.
  */
object ClassTest {

def main(args:Array[String]): Unit ={
    val firstClass : FirstClass = new FirstClass(10,10);
  firstClass.move(20,20);

  val  sonClass:SonClass = new SonClass(10,20,30);
  printLocation();
  sonClass.move(5,5);
  sonClass.move(5,5,5);
  println("trait.isEqual="+firstClass.isEqual(2));
  println("trait.isNotEqual="+sonClass.isNotEqual(firstClass));
  println(matchTest(8));
  println(matchTestMix("false"));

  def  printLocation(): Unit ={
    println("printLocation.SonClass Location x="+sonClass.x);
    println("printLocation.SonClass Location y="+sonClass.y);
    println("printLocation.SonClass Location z="+sonClass.z);
  }


  val  pp1 = new Person("a ",20);
  val  pp2 = new Person("b",21);
  val  pp3 = new Person("c ",22);
  val  pp4 = new Person("cc",22);
  val pList:List[Person]  =List(pp1,pp2,pp3,pp4);
  for(p <-  pList){
    matchClass(p);
  }



}

  //类匹配
  def matchClass(p:Person): Unit =
    p match {
      case  Person("a",20) => p.printPersonInfo();
      case Person("b",21) => p.printPersonInfo();
      case Person("c ",22) => p.printPersonInfo();
      case Person(pName,pAge) => p.printPersonInfo();
    }


  //指定类型匹配
  def matchTest(x:Int):String =
    x match{
      case 1 => "AAA";
      case 2 => "BBB";
      case 3 => "CCC";
      case _ => "Any";
    }


 //任何类型匹配
  def matchTestMix(x : Any):Any =
    x match{
      case 1 => "true";
      case "false" => 0;
      case y: Int => "Scala Int";
      case _ => "many";
    }

}



/**
  * 类似于Java的抽象类
  */
trait Equal{
  def isEqual(x:Any) : Boolean;
  def isNotEqual(x:Any):Boolean = ! isEqual(x);
}

/**
  * Scala 的类定义
  * @param xp
  * @param yp
  */
class FirstClass(xp: Int, yp:Int ) extends Equal{
  var  x: Int =  xp;
  var  y: Int =  yp;

  def  move(dx: Int,dy:Int): Unit ={
    x = x+dx;
    y = y+dy;

    println("ParentClass X Location="+x);
    println("ParentClass Y Location="+y);

  }

  override def isEqual(x: Any): Boolean = x.isInstanceOf[FirstClass] && x.asInstanceOf[FirstClass].x ==x;
}


class SonClass(  val xp: Int, val yp: Int,val zc:Int) extends FirstClass(xp,yp){
  var z :Int = zc;
  def move(dx:Int,dy:Int,dz:Int): Unit ={
    x = x+dx;
    y = y+dy;
    z = z+dz;
    println("SonClass Location x="+x);
    println("SonClass Location y="+y);
    println("SonClass Location z="+z);


  }

}
//类匹配
case class  Person(pName:String,pAge:Int){
     var  name:String = pName;
     var   age:Int  = pAge;
     def  printPersonInfo(): Unit ={
          println("name="+this.name+", age="+this.age);
     }

}