package com.zjht.andrewliu.test

/**
  * Created by liudaan on 2016/10/27.
  */
object CollectionTest {

   def main(args:Array[String]){


       var  lists = List(1,2,3,4);
     var    sets = Set("aaa","bbbb","cccc","aaaaaa");
     var   tuple = (1,2,3,"AAAA","bbbbbbb");
     var  maps = Map("a"->1,"b"->2,"c"->3);
     val  options1:Option[Int] = Some(5);
     val  options2:Option[Int] = None;
    //以 :: 和Nil来定义List
     var friutList = "AAAA"::"BBBB"::"DDDDD"::Nil;
  var emptyList = Nil;
     //Head, taill, isEmpty
     println(friutList.isEmpty);
     println(friutList.head);  //第一个
     println(friutList.tail);  //除第一个其它所有

     //列表连接:  ::  :::   List.:::(list);
     var list_1 = lists::friutList;
     println(list_1.toString());
     var list_2 = friutList:::list_1;
     println(list_2.toString());
     var list_3 = lists.:::(list_2);
     println(list_3.toString());

     var list_4 = List.concat(friutList,lists);
     println(list_4.toString());

     //List 制表,通过一个匿名函数返回的值创建一个6个元素的list
     var  tab = List.tabulate(5)(n=>n*n);
     println(tab.toString());
     //得到一个4行5列的制表,后面为占位
     var tab2 = List.tabulate(4,5)(_*_);
     println(tab2.toString());

     //对List反向
     println(lists.reverse);
   //空Set一定要指定类型
     val  intSet:Set[Int] = Set();
    val  set2 = Set("apple","orangie","ccccc");
     //将两个Set相连
     var  newSet = sets.++(set2);
     println(newSet.toString());
     //从set删除一个指定内容的元素
     var delSet = newSet.-("cccc");
     println(delSet.toString());
     //创建一个新的Set
     var newSets = delSet.&(set2);
     println(newSets.toString());

     val  map2 = Map("aa"->3,"bb"->4,"cc"->5,"dd"->5)
     val  map3 = Map("aaa"->3,"bbc"->4,"ccf"->5,"dd5"->5)
     var map1 = maps++map2;
     println(map1.toString());
     var newMap = maps.++(map3);
     println(newMap.toString());

     newMap.keys.foreach{ key=>
                 print("key="+key)
       println("  value="+newMap(key))};

     //元组
     var tumple2 = Tuple2(1,"aaaa");

     var tumple3 = Tuple3("aaaaa",333, new Some());

     println(tumple2._1);
     println(tumple2._2);
     tumple3.productIterator.foreach{ x => println(x)};


     println(newMap.get("aaa"));
     println(newMap.get("dddddddddd"));
  var  strMap =   Map("aaaaa"->"aaaaa","bbbbb"->"bbbbb","cccccc"->"cccccc");
     println(show(strMap.get("aaaaa")));
     println(show(strMap.get("aaaaaa")));

   //如果options1中有值，则返回值
     println(options1.getOrElse(5));
     //如果options2没有值，则返回默认值(10)
     println(options2.getOrElse(10));

     val  itera = Iterator(1,2,3,4,"bbb","cccc");
     val  iterb = Iterator(1,2,3,4,"bbb","cccc");

     println("iterb.length="+iterb.length);
     while(itera.hasNext){
       print(itera.next());

     }
     println("itera.size="+itera.size);

   }


  //用Option进行模式匹配(有点枚举+Switch的意思
  def  show(x : Option[String]) =  x match{
    case Some(s) => s
    case None  => "?"
  }

}
class  Some{


}