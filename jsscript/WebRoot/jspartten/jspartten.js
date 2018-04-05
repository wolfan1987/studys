var man = {
		hands: 2,
		legs: 2,
		heads:1
};

/**
 * 为Object.prototype添加clone方法
 */
if(typeof Object.prototype.clone === 'undefined'){
	Object.prototype.clone = function(){};
}
/**
 * 用hasOwnProperty过滤属性
 */
for( var i in man){
	if (man.hasOwnProperty(i)){ //用if过滤原型属性即：不显示prototype中的属性，只显示其自身的属性
		//console.log(i,":",man[i]);
	}
}

//另一种使用hasOwnProperty()的模式是在object.prototype中调用该函数。
for( var i in man){
	if ( Object.prototype.hasOwnProperty.call(man,i)){
	//	console.log(i,":",man[i]);
	}
}//为hasOwnProperty设置别名
var i , hasOwn = Object.prototype.hasOwnProperty;
for(i in man){
	if(hasOwn.call(man,i)){
		//console.log(i,":",man[i]);
	}
}
//另一种代码风格
for(i in man) if(hasOwn.call(man,i)){  //直接与for并排过滤
     //  console.log(i,":",man[i]);	
}
//switch模式
var inspect_me = 0,result = '';
switch(inspect_me){
case 0:
	result = "zero";
	break;
case 1:
	result = "one";
	break;
default:
	result = "unknown";
}
//避免隐式转换应用===进行比较，如:
var zero = 0;
if (zero === false){
	//console.log("===  "+zero,"neq false");
}
//反模式,这会进行隐式转换
if (zero == false){
	//console.log("=="+zero,"  eq false ");
}
/**
 * Function 与 eval的比较使用
 * 使用new Function()构造函数和eval()比较类似，在一定需要使用eval()时，可以考虑使用new Function()替代eval()，这样的
 * 好处是，new Function()中的代码将在局部函数空间中运行，代码中任何var定义的变量都不会自动成为全局变量.
 * eval()会影响作用域链，可以访问和修改它外部的作用域变量，Function不能，它只能看到全局作用域，对变量的影响比较小
 * 为了让eval()的影响变小，可以让其执行时在一个即时函数中，这样它就不会影响其它变量。
 * 
 */
function  functionEvalTest(){
	//即时函数
	(function(){
		var local = 1;
		//eval("local = 3; console.log(local)"); //3
		//console.log(local); //3
	}());
	(function(){
		var local = 1;
		//Function("console.log(typeof local);")();//未定义
	});
}
functionEvalTest();
//命名模式与注释编写
//构造函数名大定,表示可以用new来构造对象实例,相反如果function名是以小写开头，表示不能用new来调用
function  Person(){
	
};
var adam = new Person();
function  myFunction(){  //可以new,但意义上表示，这个不能new
	
}

//精确的常量或全局变量，全部大写:
var PI = 3.14,MAX_WIDTH = 800;
//私有函数以下划线开头，表示私有变量或私有函数
//使用一个下划线表示受保护属性，使用两个下划线表示私有属性
//var _name = ""; __factName="";
var person = {
		getName: function(){
			return this._getFirst()+ " "+ this.get_getLast();
		},
		_getFirst:function(){
			return " andrew";
		},
		_getLast:function(){
			return " liu ";
		}
};
/**
 * YUIDoc范例
 */
/**
 * 我的JavaScript应用程序
 * @module myapp
 */
var MYAPP = {};
/**
 * 一个数字工具
 * @namespace MYAPP (说明所属命名空间）
 * @class math_stuff （用class表示其是函数方法形式,或用constructor表示,表示其是构造函数形式，可以new)
 */
MYAPP.math_stuff = {
		/**
		 * Sums two numbers
		 * @method sum
		 * @param {Number} 是一个数
		 * @param {Number} 是第二个数
		 * @return {Number} 两个数输入的总和
		 */
		sum:function(a,b){
			return a+b;
		},
		/**
		 * Multipies two numbers
		 * @method multi
		 * @param {Number}	 是一个数
		 * @param (Number} 是第二个数
		 * @return{Number} 两个输入相乘后的结果
		 */
		multi:function(a,b){
			return a * b;
		}
};
/**
 * 示例二，演示构造函数型的注释写法
 * Constructs Person objects
 * @class Person
 * @constructor
 * @namespace MYAPP
 * @param{String}first 是名字
 * @param{String}last 是姓氏
 */
MYAPP.Person = function(first,last){
	/**
	 * 人的姓名
	 * @property first_name
	 * @type String
	 */
	this.first_name = first;
	/**
	 * Last(family) name of the person
	 * @property last_name;
	 * @type String
	 */
	this.last_name = last;
};
/**
 * 往Person的原型对象中添加getName方法
 * Returns the name of the person object
 * @method getName
 * @return {String} 人的姓名
 */
MYAPP.Person.prototype.getName=function(){
	return this.first_name + ' ' + this.last_name;
};

var newPerson = new MYAPP.Person("andrew","liu");
//console.log(newPerson.getName());

//检查对象实际类型
var o = new Object();
//console.log(o.constructor === Object); //true
var o = new Object(1);
//console.log(o.constructor === Number); //true
var o = new Object("I am a string");
//console.log(o.constructor === String); //true
//console.log(typeof o.substring); //true  ,所有的String都有substring方法
var o = new Object(true);
//console.log(o.constructor === Boolean); //true

//构造函数的返回值（可以在里面返回其它对象),若试图返回 并非对象的值，虽不会出错，但是函数却会简单的忽略该值，构造会返回this所引用的对象
var Objectmaker = function(){
	this.name = "This is it";
	var that = {};
	that.name = "And that's that";
	return that;
};
var o = new Objectmaker();
console.log(o.name); //And  that's that
//对构造函数强制使用new模式
//如果调用构造函数时，不使用new,会导致构造函数中的this指向了全局对象.全在ECMAscript 5中得到了解决，严格模式下不会指向全局对象

//当用函数加闭包形式构建对象时，调用函数将会失去原型，如：
function Waffle_1(){
	var that = {};
	that.tastes = "yummy";
	return that;
}
function  Waffle(){  //这种方式总会返回一个对象，但是WaffleReturn的原型将会丢失，添加到原型中的成员，对对象来说是不可用的.
	return {
		tastes: "yummy"
	};
}

//自调用构造函数： 为了解决以上原型丢失问题，并使得原型属性可在实例对象中使用，那么可以在构造函数中检查this是否为构造函数的一个实例，如果为否
//构造函数可以再次调用自身，并且在这次调用中正确地使用new操作符：重写Waffle函数.
function Waffle(){
	if(!(this instanceof Waffle)){    //以防原型丢失，检查this.这样可以永远返回有原型的对象
		return new Waffle();
	}
	//另一种检查方式：
	if(!(this instanceof arguments.callee)){  //callee属性指向的是被调用的函数（这里是指向Waffle)
		return new arguments.callee();  //同样这里new 相当于执行: new Waffle();
	}
	this.tastes = "yummy";
}
Waffle.prototype.wantAnother = true;  //此时可以为原型添加成员

var first = new Waffle();
var second = Waffle();//此时不用new调用，将会在构造函数中重新new一个对象返回,其它与用new无差别，构造函数检查就是为了不让返回无原型的对象
console.log(first.tastes+first.wantAnother);// yummy+true
console.log(second.tastes+first.wantAnother);//yummy+true

//ECMAScript5定义了一个新方法，Array.isArray(),用于检测参数是事为数组，是则返回true，以下是检查数组与Array.isArray相同功能的方法实现：
function  IsArray(array){
	//当淌有isArray方法时，就实现一个，并返回比较结果
	if(typeof Array.isArray ==='undefined'){
		Array.isArray = function(arg){
			//call(arg)返回的也是[object Array]
			return Object.prototype.toString.call(arg) === "[object Array]";
		};
	}
}
function genericErrorHandler(e){
	//alert("出错，我来处理!");
}

//JavaScript的内置错误对象：  Error(),SyntaxError(),TypeErrot()...
try{
	//调用可能发生错误的语句
	//var i = 10/0;
	throw{
		name : "MyErrorType", //自定义错误类型
		message:"oops",
		extra:"This was rather embarrassing",
		remedy: genericErrorHandler //错误处理函数
	};
}catch(e){
	//alert(e.message);
	e.remedy();
}
//javascript中没有花括号{}，块级作用域，只有函数作用域，所以在花括号中用var声明的变量，也是函数作用域。
//当将命名函数表达式分配给一个具有不同名称的变量时，在技术上是可行的，但有些浏览器(如IE),这种用法没有被正确的实现如:
var foo = function bar(){};  //在IE浏览器中不起作用。
//函数的提升：  对于所有的变量，无论在函数体的休息进行声明，都会在后台被提升到函数顶部。
//同样的函数声明也会被提升。但当是函数表达式，只有其函数变量会提升，函数体不会提升

//回调模式(在一个方法中调用传入到该方法中一会儿要用的另一个方法）如：在遍历节点时，回调外部传入的方法,或同时传入回调函数的对象
function writeCode(callback,callback_obj){
	var isCallback = true;
	//遍历节点的for循环
	//检测callback是否为可调用的函数
	if(typeof callback !== "function"){
		isCallback = false;
	}
	if(isCallback){
	  callback();  //回调
	  //或以对象形式回调
	  //callback.call(callback_obj,null);
	  //callback = callback_obj[callback];  //索引法
	}
	
}
function introduceBugs(){
	//被传入作为回调函数的方法
}
writeCode(introduceBugs,null);

//自定义函数
//内部函数改变外部函数的行为（惰性函数）,但当该函数使用了不同的名称，如：分配给不同的变量或以对象的方法来使用，那么重定义将永远的不会发生，并且 将
//会执行原型函数体。
var scareMe = function(){
	alert("Boo!");
	scareMe = function(){  //这将改为外部scareMe的行为
		alert("Double boo!");
	};
};
//scareMe();//Boo!  scareMe(); //Double boo!
scareMe.property = "properly"; //添加一个新属性
var prank = scareMe;  //赋值给另一个不同名称的变量
var spooky = {boo:scareMe}; //作为一个方法使用
//以上操作后，scareMe的property其实为undefined，即： 外部改变不会变内部函数，只有内部改变函数定义才会更新函数外部。

//即时函数：一般用于初始化时，将所有变量包装在局部变量中，执行一次即可。如：
(function(){  
	//alert("init......");
})();

//传参+调用+返回值
var result = (function (who,when,global){
	//alert(who+when+global.scareMe.property);
	  return 2+2;
}("Joe Black",new Date(),this));
//alert(result); //4
var result = function(){ //定义时即调用
	return 3+3;
}();
var result = (function(){  //定义完才调用 
	return 4+4;
})();
//即时函数中返回另一个函数
var getResult = (function(){
	var res = 5+5;
	return function(){
		return res;
	};
}());
//alert(getResult());//10
//在对象属性中使用即时函数
var o = {
		message:(function (){
			var who = "me", what = "call";
			return what + " "+who;
		}()),
		getMsg: function(){
			return this.message;
		}
};
//o.getMsg(); //call me
//o.messsage; //call me

//即时对象初始化: 在即时函数中定义模块，然后在即时函数执行完，立即执行其中的模块，如：初始化模块
({
	maxWidth:600,
	maxheight:400,
	gimmeMax:function(){
		return this.maxWidth + "x "+ this.maxheight;
	},
	init:function(){
		console.log(this.gimmeMax());
	}
}).init();  //另一种形式：({....}.init());

//初始化时分支：即：加载时分支或探测
//用此种方式：可以在脚本初始化时一次性探测出浏览器的特征，以后就可以在整个页面生命周期内重定义函数的运行方式。
//接口
var utils = {
		addListener:null,
		removeListener:null
};
//实现
if(typeof window.addEventListener === 'function'){  //大从浏览器
	utils.addListener = function(el,type,fn){
		el.addEventListener(type,fn,false);
	};
	utils.removeListener = function(el,type,fun){
		el.removeEventListener(type,fn,false);
	};
}else if(typeof document.attachEvent === "function"){  //IE浏览器
	utils.addListener = function(el,type,fn){
		el.attachEvent('on'+type,fn);
	};
	utils.removeListener = function(el,type,fun){
		el.detachEvent('on'+type,fn);
	};
}else{  //老古董浏览器
	utils.addListener = function(el,type,fn){   
		el['on'+type] = fn;
	};
	utils.removeListener = function(el,type,fun){
		el['on'+type] = null;
	};
}


//缓存备忘模式：即将需要运行时间长或结果可以复用的数据保存起来，当有请求相同结果的请求来时，优先从缓存中拿
var myFunc = function(){
	//var cacheKey = JSON.stringify(Array.prototype.slice.call(arguments));//序列化参数，作为key
	var result;
	if(!myFunc.cache[cacheKey]){
		result = {};
		//大数据计算,大开销计算
		myFunc.cache[cachekey] = result;
	}
	return myFunc.cache[cachekey];
};
myFunc.cache = {};
//curry(柯里化模式）当发现正在调用同一个函数，并且传递的参数绝大多数都是相同的，那么该函数可能是用于Curry化的一个很好的候选参数。
//柯里化函数：
function schonfinkelize(fn){
	var slice = Array.prototype.slice,stored_args = slice.call(arguments,1);
	return function(){
		var new_args = slice.call(arguments), args = stored_args.concat(new_args);
		return fn.apply(null,args);
	};
}
//普通函数
function curryAdd(x,y){
	return x+y;
}
//调用 
var newAdd = schonfinkelize(curryAdd,5);
var r = newAdd(4);  //9
//级联调用 
var r = schonfinkelize(curryAdd,8)(8); //16


