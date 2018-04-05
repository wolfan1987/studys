var stooge = {};
stooge['first-name'] = 'Jerome';
stooge['nickname'] = 'Curly';
var x = stooge;
//x得到的是stooge的引用，而对x的操作就是对stooge操作
x.nickname = 'Curly2';
//此时x.nickname=stooge.nickname
var nick = stooge.nickname;
//自定义的创建对象函数
if(typeof Object.beget !== 'function'){
	//为Object添加create方法，将o对象传进去后，创建一个空函数，并将此函数的原型对象指向o,并返回新函数的实例，此时返回的对象中包含了传进来对象o的相关属性
	Object.create = function(o){  
		var F =  function(){};
		F.prototype = o;
		return new F();
	};
}

//用Object.create创建一个对象,此时其nickname来自于原型对象中
var another_stooge = Object.create(stooge);
//alert(another_stooge.nickname);

//减少全局变量的污染，将全局性的资源都纳入一个名称空间中如:
var MyApp = {};  //这是一个命名空间，然后与当前程序有关的代码都加到此空间中
MyApp.stooge = {
		"first-name":"joe",
		"last-name":"Howard"
};
MyApp.flight = {
		airline : "Oceanic",
		number: 815,
		departure:{
			IATA:"SYD",
			time:"2004-09-22 14:55",
			city:"Sydney"
		},
		arrival:{
			IATA:"LAX",
			time:"2004-09-23 10:42",
			city:"Los Angeles"
		}
};
//函数可以定义在其他函数中，一个内部函数除了可以访问自己的参数和变量外，同时它也能自由访问把它嵌套在其中的父函数的参数与变量。
//通过函数字面量创建的函数对象包含一个连到外部上下文的连接，这被称为闭包（closure)。
//javascript的4种函数调用模式,这此模式在如何初始化关键参数this上存在差异。
//第一种：方法调用模式
var myObject = {
		value:0,
		increment:function(inc){
			//测试inc是否为number类型
	      this.value+= typeof inc ==='number' ? inc : 1;
        }
};
myObject.increment();
document.writeln(myObject.value);
myObject.increment(2);
document.writeln(myObject.value);
//第二种：函数调用模式(此时this指向的是全局对象)
function  add(a,b){
	//此时会显示myObject.value的值
	//alert("add.this.value="+this.myObject.value);
	return a+b;
}
var sum = add(3,4);
//这种模式下函数指向全局对象不是个好的设计，可以用以下方法来避免其带来的影响
myObject.double=function(){
	var that = this; //此时that指向的是myObject对象
	var helper = function(){
		that.value = add(that.value,that.value);
	};
	helper();
};
//以方法的形式调用double
myObject.double();
document.writeln(myObject.value);
//第三种：构造器调用模式
//如果在一个函数前面带上new来调用，那么背地里将会创建一个连接到该函数的prototype成员的新对象，同时this会被绑定到那个新对象上。
var Quo = function(str){
	this.status = str;
};
//为所有实例添加get_status方法
Quo.prototype.get_status = function(outerstatus){
	return this.status +" | "+outerstatus;
};
var myQuo = new Quo("confused");
var temp = ["--outer"];
document.writeln(myQuo.get_status(temp));
//第四种：apply调用模式
var array = [3,5];
var sum = add.apply(null,array); //相当于直接调用add方法，并将array作为参数传给add
document.writeln(sum);
//构造一个包含status成员的对象
var statusObject = {
		status: 'A-OK'
};
//这里的意思是将get_status方法，作为statusObject对象的方法来调用，此时他们合体，就相当于在同一个函数中一样。注意：apply(object,array)；传给方法的参数一定是数组型的。
temp = ["Temp"];
var status = Quo.prototype.get_status.apply(statusObject,temp);   //status='A-OK';
document.writeln(status);

//异常
//如果参数类型不符，就throw出一个异常
var addByException = function(a,b){
	if ( typeof a !== 'number' || typeof b !== 'number'){
		throw{   //此处抛出的是一个Exception对象
			name : 'typeError',
			message:'add needs numbers'
		};
	}
	return a + b;
};
//用try--catch捕获异常
var try_it = function(){
	try{
		addByException("seveen");
	}catch(e){
		document.writeln(e.name + ": "+ e.message);
	}
};
try_it();


//扩充类型的功能,为Function对象添加创建方法的方法
//在Function对象的prototype中添加method方法，传入方法名(name)，方法体(func)，
//然后在当前对象（所有的Function对象）的原型中添加为name的方法，并返回修改后的函数,即this.
Function.prototype.method = function ( name, func){  
	this.prototype[name] = func;
	return this;
};
//给Number对象添加integer方法,（Number也是Function的一种，为Function添加后，所有与Function原型有关的函数都会有method方法
Number.method('integer',function(){
	return Math[this<0 ? 'ceil' : 'floor'](this);  //当Number的值<0时时，返回Math中的ceil函数,否则返回floor函数，然后对this(数值）求值
});

document.writeln((-10/3).integer());
//为String对象添加trim方法，去掉空格
String.method('trim',function(){
	return this.replace(/^\s+|\s+|\s+$/g,'');
});
document.writeln('"' + " neat  ".trim()+'"');

//符合条件时才加方法，并返回当新对象
Function.prototype.method = function(name,func){
	if(!this.prototype[name]){
		this.prototype[name] = func;
	}
	return this;
};

//闭包
var closureObject = (function(){  //以function()形式定义一个函数，并立即调用其
	var value = 0;   
	 //通过return 返回一个函数对象即function()==return{}，此时closureObject=return{}的返回结果,
    //且return返回的函数仍旧可以访问value，这种形式称为闭包
	return {  
		increment: function(inc){
			value += typeof inc === 'number' ? inc : 1;
		},
		getValue: function(){
			return value;
		}
	};
}());
closureObject.increment(3);
//alert(closureObject.getValue());//3
//为quo对象创建get_status方法，其可以访问status参数
var quo = function(status){
	return {
		get_status: function(){
			return status;
		}
	};
};


var myQuo = quo("amazed");  
//alert(myQuo.get_status()); //显示：amazed
//用函数与闭包来进行模块化
//模块模式利用了函数作用域和闭包来创建被绑定对象与私有成员的关联。
//其一般形式是：一个定义了私有变量和函数的函数;利用闭包创建可以访问私有变量和函数的特权函数;最后返回这个特权函数，或者把它们保存到
//一个可以访问的地方,它可以摒弃全局变量的使用。促进了信息隐藏和其他优秀的设计实践。
String.method('deentityify',function(){
	var entity = {
			quot:'"',
			lt: '<',
			gt: '>'
	};
	return function(){
		return this.replace(/&([^&;]+);/g,
			function(a,b){
			   var r = entity[b];
			   return typeof r === 'string' ? r: a;
			}
		);
	};
});
document.writeln('&lt;&quot;&gt;'.deentityify()); //将字符替换 <">
//用模块模式来产生字全对象(生成序列对象)
var serial_maker = function(){
	//返回一个用来产生唯一字符串的对象，唯一字符串由两部分组成：前缀+序列号。该对象包含一个设置前缀的方法，一个设置序列号的方法，
	//和一个产生唯一字符串的gensym方法
	var prefix = '';
	var seq = 0;
	return {
		set_prefix: function(p){
			prefix = String(p);
		},
		set_seq: function(s){
			seq = s;
		},
		gensym:function(){
			var result = prefix + seq;
			seq+=1;
			return result;
		}
	};
};
var seqer = serial_maker();
seqer.set_prefix("Q");
seqer.set_seq(1000);
var unique = seqer.gensym();  //Q1000
//柯里化: 允许我们把函数与传递给它的参数相结合，产生出一个新的函数
Function.method('curry',function(){
	var slice = Array.prototype.slice,  //引用slice方法操作数组
	args = slice.apply(arguments),   //将arguments参数转变为真正的数组，以便用数组的方法操作
	that = this;
	return function(){
		return that.apply(null,args.concat(slice.apply(arguments)));  //
	};
});

var add1 = add.curry(1);
//alert(add1(6));  //7
//javascript的继承实现
//伪类（操作prototype属性来为对象的构造器扩展功能,然后此用此对象来实例化）
//javascript对象中的prototype的构造器属性默认为： this.prototype = {constructor:this}，指向的是当前对象,
//new 操作符大概是这么执行的
/**Function.method('new',function(){
	var that = Object.create(this.prototype);
	var other = this.apply(that,arguments);
	return (typeof other === 'object' && toher) || that;
});
**/
//操作对象prototype属性来扩展对象功能
var Mammal = function(name){
	this.name = name;
};
Mammal.prototype.get_name = function(){
	return this.name;
};
Mammal.prototype.says = function(){
	return this.saying || '';
};
var myMammal = new Mammal('Herb the Mammal');
var name = myMammal.get_name();
//用另一个伪类来继承Mammal，这是通过定义它的constructor函数并替换它的prototype为一个Mammal的实例来实现的
var Cat = function(name){
	this.name = name;
	this.saying = 'meow';
};
Cat.prototype = new Mammal();
//扩展Cat.prototype，增加方法
Cat.prototype.purr=function(n){
	var i,s = '';
	for(i = 0; i < n; i+=1){
		if(s){
			s += '-';
		}
		s += 'r';
	}
	return s;
};
Cat.prototype.get_name = function(){
	return this.says() + '  '+ this.name + ' '+ this.says();
};

var myCat = new Cat("Henrietta");
var says = myCat.says(); //meow
var purr = myCat.purr(5); //r-r-r-r-r
var name = myCat.get_name();//'meow Henrietta meow
//alert(says + purr + name);
//为Function添加,inherits方法，以模拟继承
//将当前对象的原型指向要继承的Parent对象，然后将新对象返回
Function.method('inherits',function(Parent){
	this.prototype = new Parent();
	return this;
});
//inherits方法与级联实现
var Dog = function(name){
	this.name = name;
	this.saying = 'meow';
}
.inherits(Mammal)
.method('purr',function(n){
	var i,s = '';
	for ( i = 0; i < n; i+=1){
		if (s){
			s += '-';
		}
		s += 'r';
	}
	return s;
})
.method('get_name',function(){
	return this.says()+'  '+ this.name + '  '+this.says();
});

var myDog = new Dog("Henrietta");
 says = myDog.says(); //meow
 purr = myDog.purr(5); //r-r-r-r-r
 name = myDog.get_name();//'meow Henrietta meow
//alert(says + purr + name);
 function  marker(config){
	 
 }

//对象说明符： 平常传递参数给函数或方法时，最好以JSON格式封装传递调用 ，可按如下格式:
var myFunResult = marker({
	first : "first",
	middle:"middle",
	last:"last",
	state: "state",
	city: "city"
});
//或
var config = {
		first : "first",
		middle:"middle",
		last:"last",
		state: "state",
		city: "city"
};
myFunResult  =  marker(config);
//原型模式的进化： 摒弃类，专注于对象，即：用新对象继承旧对象的属性，按需要重写新对象中来自旧对象中属性或方法
var myMammal = {
		name : 'Herb the Mammal',
		get_name : function(){
			return this.name;
		},
		says : function(){
			return this.saying || '';
		}
};
//构造的新对象 ，按需要定制.这种形式也称为“差异化继承”
var myCat = Object.create(myMammal);
myCat.name = "Henrietta";
myCat.saying = "meow";
myCat.purr = function(n){
	var i, s = '';
	for ( i = 0; i < n; i+=1){
		if (s){
			s += '-';
		}
		s += 'r';
	}
	return s;
};
myCat.get_name = function(){
	return this.says() + ' ' + this.name + ' 	'+ this.says();
};

//模块模式(函数化模式)：定义的4个步骤
//1、创建一个新对象。 2、有选择地定义私有实例变量和方法；3、给这个新对象扩充方法。4、返回那个新对象
//以下为模块模式代码模板
var funConstructor = function(spec,my){
	var that;  //定义其它只能在此方法来使用的私有变量
	my = my || {}; //验证从外面带来的对象
	//把共享诉变量和函数添加到my中
	//my.member = value;
	//that =  new Object();
	//添加给that的特权方法
	return that;
};
//例子如下：
//spec是传到里面的，my是从里面暴露到外面的,这是用作父类的对象
var mammal = function (spec,my){
	var that = {},innerVar = "test";
	my = my || {};
	my.fromInner = "abcdefg";
	my.fromInnerFunction = function(){
		alert(this.fromInner);
	};
	that = {};
	that.get_name = function(){
		return spec.name;
	};
	that.says = function(){
		return spec.saying || '';
	};
	return that;
};
var my = {
		method1:function(id,name){
			alert(id+name);
		}
};
//
var myMammal = mammal({name:'andrewliu',saying:'Hello world'},my);
/**alert(myMammal.get_name());
alert(myMammal.says());
alert(my.fromInnerFunction());
alert(my.method1(1, "AA"));
**/
//这是扩展对象，cat只要关注于自己的实现即可
var cat = function(spec){
	spec.saying = spec.saying || 'meow';
	var that = mammal(spec,my);
	that.purr = function(n){
		var i, s = '';
		for ( i = 0; i < n; i+=1){
			if (s){
				s += '-';
			}
			s += 'r';
		}
		return s;
	};
	that.get_name = function(){
		return this.says() + ' ' + spec.name + ' 	'+ this.says();
	};
	return that;
};

//访问父类的方法:
Object.method('superior',function(name){  //name = 父类方法名
	var that = this, method = that[name]; //将当前对象引用传给that,得到父类方法
	return function(){
		return method.apply(that,arguments);  //将父类方法作为that对象的方法调用，以arguments为方法参数
	};
});

//调用父类方法测试：
var coolcat = function(spec){
	var that = cat(spec);
	super_get_name = that.superior('get_name');
	that.get_name = function(n){
		return 'like 	'+ super_get_name() + 'baby';
	};
	return that;
};

var myCoolCat = coolcat({name:"Bix"});
var name = myCoolCat.get_name();
//alert(name);

//部件实例：从部件中把对象组装出来  模块模式（函数化模式）的应用，也可以传一个my进去
var eventuality = function(that){
	var registry={};
	that.fire = function(event){
		//在一个对象上触发一个事件，该事件可以是一个包含事件名称的字符串或者是一个拥有包含事件名称的type属性对象。
		//通过on方法注册的事件处理程序中匹配事件名称的名称的函数将被调用 
		var array,func,handler,i,type = typeof event === 'string' ? event : event.type;
		//如果这个事件在一组事件处理程序中，那么就遍历它们并按顺序依次执行
		if(registry.hasOwnProperty(type)){
			array = registry[type];
			for ( i = 0; i < array.length; i+=1){
				handler = array[i];
				//每个处理程序包含一个方法和一组可选的参数
				//如果该方法是一个字符串形式的名字，那么寻找到该函数
				func = handler.method;
				if ( typeof func === 'string'){
						func = this[func];
				}
				//调用一个处理程序，如果该条目包含参数，那么传递它们过去，否则，传递该事件对象。
				func.apply(this,handler.parameters || [event]);
			}
		}
		return this;
	};
	
	that.on = function(type,method,parameters){
		//注册一个事件，构造一条处理程序条目。将它插入到处理程序数组中，如果这种类型的事件还不存在，就构造一个
		 var handler = {
				 method : method,
				 parameters: parameters
		 };
		 if (registry.hasOwnProperty(type)){
			 registry[type].push(handler);
		 }else{
			 registry[type] = [handler];
		 }
		 return this;
	};
	return that;
};
