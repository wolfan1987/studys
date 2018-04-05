//命名空间模式：  有助于减少程序中所需的全局变量的数量，并且同是还有助于避免命名冲突或过长的名字前缀。
//理想情况下：可以为应用程序或库创建一个全局对象，然后将所有功能添加到该全局对象中，从而在具有大量函数、对象和其他变量的情况下并不会污染全局变量.
//定义一个全局变量
/**
 * 命名空间变量的缺点：
 * 1、需要输入更多的字符，每个变量和函数前都要附加前缀，总体上增加了需要下载的代码量。
 * 2、仅有一个全局变量实例，意味着任何部分的代码都可以修改该全局实例，并且其余功能能够获得更新后的状态。
 * 3、长嵌套意味着更长（更慢）的属性解析查询时间。
 */
var MYAPP={};
//在其中添加构造函数
MYAPP.Parent = function(){};
MYAPP.Child = function(){};
//一个变量
MYAPP.some_var = 1;
//一个对象容器(包）
MYAPP.modules = {};
//嵌套对象
MYAPP.modules.module1={};
MYAPP.modules.module1.data = {a:1,b:2};
MYAPP.modules.module2 = {};

//解决命名空间的缺点：通用命名空间函数  如：
var MYAPP = MYAPP || {};  //如果有MYAPP了就不创建，否则就创建一个字面量对象
MYAPP.namespace = function(ns_string){
	var parts = ns_string.split('.');  //对传入的a.b.c.d格式字符进行分隔
	var parent = MYAPP,i;   //将最高父命名空间定义为MYAPP
	if(parts[0] === "MYAPP"){  //将传入的命名空间字符串中的MYAPP段去掉。
		parts = parts.slice(1);
	}
	
	for(i = 0; i < parts.length; i += 1){  //迭代剩下的字符，以创建命名空间
		if(typeof parent[parts[i]] === "undefined"){    //如果parent中(MYAPP)中此空间未定义
			parent[parts[i]] = {};  //为parent也就是MYAPP中添加一个属性(次级命名空间)
		}
		parent = parent[parts[i]];  //将最新的命名空间信息传给parent引用，为下一个次级命名空间的添加作准备
	}
	return parent;  //返回最终的命名空间
};

//通用命名空间函数的使用：
var module2 = MYAPP.namespace('MYAPP.modules.module2');
var result = (module2 === MYAPP.modules.module2); //true
//忽略最前面的'MYAPP'
MYAPP.namespace('modules.module51');
//长命名空间
MYAPP.namespace('once.upon.a.time.thre.was.this.long.nested.property');

//声明依赖关系（相当于java中包的静态导入）
//这种方式优点：1、明确了依赖，更容易维护和发现进行了哪些依赖；
//2、解析局部变量的速度总比要解析全部变量要快，这种方式提高了性能，全局符号解析仅会在函数中执行一次，以后将会使用局部变量，解析速度加快; 
//3、在压缩时可产生更少的代码量。
var staticImport = function(){
	//导入依赖
	var module2 = MYAPP.modules.module2;
	module2.tipmsg=function(){
		//alert("tipmsg");
	};
	var module51 = MYAPP.modules.module51;
	module51.add=function(){
		return 10+10;
	};
	//下面就可以直接用module2和module51来引用和操作命名空间中的方法或属性了
	module2.tipmsg();
	//alert(module51.add());
	
};
staticImport();

//私有属性和方法
//javascript中并没有用于私有成员的特殊语法，但可以使用闭包（closure)来实现这种功能。
//构造函数创建了一个闭包，而在闭包范围内部的任意变量都不会暴露给构造函数以外的代码。但这些私有变量
//仍然可以用于公共方法中：即：定义在构造函数中，且作为返回对象的一个部分暴露给外部的方法。
function Gadget(){
	//声明私有成员
	var name = "iPod";
	//公有函数
	this.getName = function(){   //以方法形式提供访问私有属性的功能，这个方法也称为特权方法。
		return name;
	};
}
//私有性失效(这种现象要求实现者自己细心控制才可避免)
function Gadget2(){
    //私有成员
	var specs = {
			screen_width: 320,
			screen_height:480,
			color:"white"
	};
	//公有函数,这里暴露了specs,如果外部一定要访问specs，为了不影响内部，可以clone一分副本，再返回给外面
	this.getSpecs = function(){
		return specs;
	};
}

//对象字面量以及私有性
//用匿名即时函数创建闭包来实现私有性如：
var myobj;  //公有对象
(function(){
	//私有成员
	var name = "my,oh my";
	//实现公胡部分,没有用var修饰，将会覆盖外部有myobj
	myobj={
			//特权方法
			getName:function(){
				return name;
			}
	};
}());
//alert(myobj.getName());
//另一种实现方式: 模块模式的基本框架
var myobj = (function(){
	//私有成员
	var name = "my,oh my";
	//实现公胡部分,没有用var修饰，将会覆盖外部有myobj
	return{
			//特权方法
			getName:function(){
				return name;
			}
	};
}());
//alert(myobj.getName());
//原型与私有属性：  即将私有属性添加到prototype中，这样不必每次创建对象时，都要复制一份私有属性

Gadget.prototype = (function(){
	//私有成员
	var browser = "Mobile webkit";
	//公胡原型成员
	return {
		getBrowser:function(){
			return browser;
		}
	};
}());
var toy = new Gadget();
console.log(toy.getName());//对象特权方法
console.log(toy.getBrowser());//原型特权方法

//将私有方法提示为公有方法： 即在外面声明一个公有对象，在匿名即时函数中，将相关私有方法复制给外面的公有对象
var myarry;
(function(){
	var astr = "[object Array]", toString = Object.prototype.toString;
	function isArray(a){ //判断是否为数组
		return toString.call(a) === astr;
	}
	function indexOf(haystack,needle){
		var i = 0,max = haystack.length;
		for(; i < max; i+=1){
			if(haystack[i] === needle){
				return i;
			}
		}
		return -1;
	}
	myarray = {  //将内部函数暴露到外面的对象中
			isArray : isArray,
			indexOf : indexOf,
			inArray: indexOf   //用于PHP
	};
}());
console.log(myarray.isArray([1,2])); //true
console.log(myarray.isArray({o:1})); //false
console.log(myarray.indexOf(['a','b','z'],'z')); //2
console.log(myarray.inArray(['a','b','z'],'z')); //2

//模块模式：
//一种创建自包含非耦合代码片段的有得工具，可以将它视为墨盒功能，并且可以根据您所编写软件的需求添加、替换或删除这些模块。
//它是以下几种模式的组合：
//1、命名空间；2、即时函数; 3、私有和特权成员; 4、声明依赖;  如下：
//创建命名空间
MYAPP.namespace('MYAPP.utilities.array');
MYAPP.namespace('MYAPP.utilities.object');
MYAPP.namespace('MYAPP.utilities.lang');
//以即时函数形式，定义模块
MYAPP.utilities.array = (function(){
	//依赖
	var uobj = MYAPP.utilities.object;
	var ulang = MYAPP.utilities.lang; 
	//私有属性
	var array_string = "[object Array]";
	var ops = Object.prototype.toString;
	//私有方法
	function  display(){
		alert("display!");
	}
	//return 公有API
	return{
		inArray:function(needle,haystack){
			var i = 0,max = haystack.length;
			for(; i < max; i+=1){
				if(haystack[i] === needle){
					return i;
				}
			}
			return -1;
		},
		
		isArray:function(a){
			return ops.call(a) === array_string;
		}
	};
}());

var  arraypkg = MYAPP.utilities.array;
//alert(arraypkg.isArray([1,2]));
//alert(arraypkg.inArray(['a','b','z'],'z'));

//提示模块模式：即在即时函数中将所有函数私有化，然后return回一个包含可以访问私有函数的对象，给外面访问
MYAPP.utilities.array = (function(){  //揭示或暴露外部可用API
	 //私有属性
	var array_string = "[object Array]";
	var ops = Object.prototype.toString;
	//私有方法
	inArray = function(needle,haystack){
		var i = 0,max = haystack.length;
		for(; i < max; i+=1){
			if(haystack[i] === needle){
				return i;
			}
		}
		return -1;
	},
	isArray = function(a){
		return ops.call(a) === array_string;
	};
	//揭示公有API,
	return{
		isArray: isArray,
		indexOf: inArray
	};
}());

//创建构造函数模块
MYAPP.namespace("MYAPP.utilities.Array");
MYAPP.utilities.Array = (function(){
	//依赖
	var uobj = MYAPP.utilities.object,ulang = MYAPP.utilities.lang;
	//私有属性和方法
	//可选的一次性初始化过程
	var Constr;
	//公有API----构造函数
	Constr = function(o){
		this.elements = this.toArray(o);
	};
	//公有API原型：
	Constr.prototype = {
		constructor:MYAPP.utilities.Array,
		version:"2.0",
		toArray:function(obj){
			for(var i = 0, a = [], len = obj.length; i< len; i+=1){
				a[i] = obj[i];
			}
			return a;
		}
	};
	Constr.prototype.display = function(){
		//alert("display");
		return "fromdisplay";
	};
	//返回要分配给新命名空间的构造函数
	return Constr;
}());
var arr = new MYAPP.utilities.Array({a:'1',b:'2'});
//alert(arr.display());
//将全局变量导入到模块中
MYAPP.utilities.module = (function(app,globa){
	//引用全局对象
	//以及理在被转换成局部变量的全局应用程序命名空间对象
}(MYAPP,this));


//沙箱模式：
//主要解决了命名空间的如下几个缺点：
//1、对单个全局变量的依赖变成糖了对应用程序的全局变量依赖，在命名空间模式中，是没有办法使用同一个应用程序或库的两个版本运行在同一个页面中，
//因为这两者都需要同一个全局符号名。
//2、对这种以点分割的名字来说，需要输入更长的字符，并且在运行时需要解析更长的时间。
//沙箱模式提供了一个可用于模块运行的环境，肯不会对其他模块和个人沙箱造成任何影响.
//定义一个全局构造函数：,
function Sandbox(){
	//将参数转换成一个数组
	var args = Array.prototype.slice.call(arguments);
	//最后一个参数是回调函数
	callback = args.pop();
	//模块可以作为一个数组传递，或作为单独的参数传递
	var modules = (args[0]&&typeof args[0] === "string") ? args : args[0], i;
	
	//确保该函数
	//作为构造函数被调用
	if(!(this instanceof Sandbox)){
		return new Sandbox(modules,callback);
	}
	//向this添加属性
	this.a = 1;
	this.b = 2;
	//现在向该核心"this"对象添加模块
	//不指定模块名称或指定"*"，都表示“使用所有模块”
	if(!modules || modules === "*"){
		modules = [];
		for(i in Sandbox.modules){
			if(Sandbox.modules.hasOwnProperty(i)){
				modules.push(i);
			}
		}
	}
	//初始化所需的模块
	for(i = 0; i < modules.length; i+=1){
		Sandbox.modules[modules[i]](this);
	}
	callback(this);
}
Sandbox.prototype = {
		name : "My Application",
		version: "1.0",
		getName:function(){
			return this.name;
		}
};

//为Sandbox增加模块
Sandbox.modules = {};
Sandbox.modules.dom = function(box){
	box.getElement = function(){
		console.log("dom.getelement");
	};
	box.getStyle = function(){
		console.log("dom.getStyle");
	};
	box.foo = "bar";
};
Sandbox.modules.event = function(box){
	//如果需要，就访问Sandbox原型，如下语句：
	//box.constructor.prototype.m = "mmm";
	box.attachEvent = function(){
		console.log("event.attachEvent");
	};
	box.detachEvent = function(){
		console.log("event.detachEvent");
	};
};
Sandbox.modules.ajax = function(box){
	box.makeRequest = function(){
		console.log("ajax.makeRequest");
	};
	box.getResponse = function(){
		console.log("ajax.getResponse");
		return "sandbox success!";
	};
};

//沙箱模式的使用(以字符参数形式引入模块）
//Sandbox('ajax','dom',function(box){
//*号引入所有模块化
//Sandbox('*',function(box){
//以数组形式引入指定模块,
//在这里将会把ajax+event模块中的方法全部添加到box中
new Sandbox(['ajax','event'],function(box){
	  // for(var i in box){
		//   console.log(i);
	  // }
       //console.log(box.getResponse());
});

//公有和私有静态成员
var Gadget = function(){};
Gadget.isShiny = function(){
	return "you bet";
};
//向该原型添加一个普通方法
Gadget.prototype.setPrice = function(price){
	this.price = price;
};
//调用静态方法
//console.log(Gadget.isShiny()); //输入 you bet
var iphone = new Gadget();
iphone.setPrice(500);
//以静态方式访问实例方法与用实例调用静态方法都无法正常运行
//console.log(typeof Gadget.setPrice); //输出undefined
//console.log(typeof iphone.isShiny);  //输出undefined

//以加上外观门面形式，访问静态方法： 这时，如果在静态方法内部使用this要特别注意，当执行Gadget.isShiny()时，那么isShiny()内部的this将会指向
//Gadget构造函数。如果执行iphone.isShiny()，那么this将会指向iphone
Gadget.prototype.isShiny = Gadget.isShiny;  //在原型中添加isShiny，其指向原始静态方法
//console.log(iphone.isShiny());
//如何以静态或非静态方式调用同一个方法。（以instanceof函数来区分当前对象this，是否为构造函数的实例）
//构造函数
var Gadget = function(price){
	this.price = price;
};
//静态方法
Gadget.isShiny = function(){
	//这种方法总是可以运行
	var msg = "you bet";
	if(this instanceof Gadget){
		msg+=", it costs $"+this.price+"!";
	}
	return msg;
};
//向该原型添加一个普通方法
Gadget.prototype.isShiny = function(){
	return Gadget.isShiny.call(this);
};
//静态方法调用 
//console.log(Gadget.isShiny());  // you bet
 //实例方法调用 
 var a = new Gadget('499.99');
 //console.log(a.isShiny()); //you bet ,it costs $499.99!
//私有静态成员： 以同一个构造函数创建的所有对象共享该成员，构造函数外部不可访问该成员
 var Gadget = (function(){  //重新返回一个构造函数给Gadget
	 //静态变量/属性,相当于java中的staic变量，后面的return，使得在Gadget中永远只有一份counter
	 var counter = 0;
	 //返回 
	 //该构造函数新的实现
	 return function(){
		console.log(counter += 1); 
	 };
 }());
 var g1 = new Gadget(); //1   控制台打印为:1
 var g2 = new Gadget();//2                2
 
 //私有静态变量+特权方法访问私有静态变量
 //构造函数
 var Gadget = (function(){
	 //静态变量/属性
	 var counter = 0, NewGadget;
	 //新构造函数，将是实现返回给Gadget的函数
	 NewGadget = function(){
		counter += 1; 
	 };
	 //添加特权方法
	 NewGadget.prototype.getLastId = function(){
		return counter; 
	 };
	 //覆盖构造函数
	 return NewGadget;
 }());
 var iphone = new Gadget();
 //console.log(iphone.getLastId()); //1
 var ipod = new Gadget();
 //console.log(ipod.getLastId()); //2
 var ipad = new Gadget();
 //console.log(ipad.getLastId()); //3
 
 //常量模式,只可以设置初始值，能检查是否定义，读取常量值
 var constant = (function(){
	var constants = {}, ownProp = Object.prototype.hasOwnProperty,allowed = {string: 1,number: 1,boolean:1},
	prefix = (Math.random()+"_".slice(2));
	return {
		set: function(name,value){
			if(this.isDefined(name)){  //检查是事定义过
				return false;
			}
			if(!ownProp.call(allowed, typeof value)){  //检查变量类型
				return false;
			}
			constants[prefix + name] = value;  //设值
			return true;
		},
		isDefined: function(name){
			return ownProp.call(constants,prefix+name);    //检查其中是否有此属性
		},
		get:function(name){
			if(this.isDefined(name)){  //如果有定义此常量
				return constants[prefix + name];    //返回常量值
			}
			return null;
		}
	};
	
 }());
 
 //检查是否已经定义
 //console.log(constant.isDefined("maxwidth"));  //false
 //定义
 //console.log(constant.set("maxwidth",480)); //true
 
 //再次检查
 //console.log(constant.isDefined("maxwidth")); //true
 
 //试图重新定义
 //console.log(constant.set("maxwidth",320)); //false
 
 //该值是否仍保持不变
 //console.log(constant.get("maxwidth")); //480
 
 //链模式（在方法中返回this来实现）
 //method方法来实现链模式,method方法实现：  它是在最基本的Function的prototype中添加的，所以创建任何一个对象中，都会有这个方法
 // 1、新方法的名称; 2、方法的实现
 if(typeof Function.prototype.method !== "function"){
	 Function.prototype.method = function(name,implementation){
		 this.prototype[name] = implementation;
		 return this;
	 };
 }
 //使用method
 var Person = function(name){
	 this.name = name;
 }.method("getName", function(){
	return this.name; 
 }).method("setName", function(name){
	 this.name = name;
	 return this;
 });
 var a = new Person("Adam");
 //console.log(a.getName()); //输出: Adam
 //console.log( a.setName("Eve").getName());  //输出Eve
