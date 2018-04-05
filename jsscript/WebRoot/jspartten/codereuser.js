/**
  *  类式继承模式 + 现代继承模式
  *
  */
//类式继承模式（默认模式）: 缺点： 同时继承了两个对象的属性：即：添加到this的属性及原型属性： 一般构造函数经验法则时，应该将
//可复用的成员添加到原型中,它并不支持将参数传递到子构造函数中，而子构造函数然后又将参数传递到父构造函数中。
//父构造函数
function Parent(name){
	this.name = name || 'Adam';
}
//向父类中原型添加功能
Parent.prototype.say = function(){
	return this.name;
};
//空白的子类构造函数
function Child(name){};
//实现继承的方法：  子类原型指向父类对象
function inherit(C,P){
	C.prototype = new P();  //指向一个父类构造函数创建的对象，而不是构造函数本身
}
//子类继承父类
inherit(Child,Parent);
var kid = new Child();
console.log(kid.say()); //执行父类中的say方法，输出: Adam

//类式继承模式2： 借用构造的函数
//解决了从子构造函数到父构造函数中的参数传递问题，其借用父构造函数，传递子对象以绑定到this，并且还转发任意参数，但
//只能继承在父构造函数中添加到this的属性，并不能继承那些已添加到原型中的成员,而且子对象获得了的是继承成员的副本，而不是引用。
//优点： 可以获得父对象自身成员的真实副本，不会存在子对象意外覆盖父对象的风险。
//缺点：无法从原型中继承任何东西，原型也仅是添加可重用方法及属性的位置，并不会为每个实例重新创建原型。
//模式范例如下：
function Child2(a,b,c,d){
	Parent.apply(this,arguments);  //将Parent构造函数作为this的方法来调用，此时会将父类中this部分的复制到子类
	
	//多继承
	// A.apply(this);  B.apply(this);
}
//具体例子： 父构造函数
function Article(){
	this.tags = ['js','css'];
}
var article = new Article();
function BlogPost(){};
BlogPost.prototype = article;  //默认模式实现继承，子类原型指向父类对象
var blog = new BlogPost();

function StaticPage(){
	Article.call(this);    //StaticPage也继承Article,只是继承了this，没继承prototype
}

var page = new StaticPage();
console.log(article.hasOwnProperty('tags')); //true
//blog的原型中有article，但其this中并无article相关属性，hasOwnProperty只能检测this级别的（当前对象自己的），而非原型对象中的
console.log(blog.hasOwnProperty('tags')); //false  
console.log(page.hasOwnProperty('tags'));///true

blog.tags.push('html');  //修改父类的（也就是修改了自己的）
page.tags.push('php');  //只是修改了自己的，page复制了父类的所有this属性，这些属性和父类无关

console.log(article.tags.join(', '));  //js,css,html   没有php,php只在page中

//类式继承模式：  借用和设置原型
//主要思想是结合前两种模式，即：先借用构造函数，然后设置子函数构造函数的原型使其指向一个构造函数创建的新实例。
//优点是:结果对象能够获得父对象本身的成员副本以及指向父对象中可复用功能（原型中的功能）的引用。同时子对象也能够将任意参数传递到你构造函数中。
//既可能继承父对象中的一切，又能安全修改自身属性，不会对父对象带来风险。
//缺点：  父构造函数被调用了两次，父类的属性也会被继承两次。
function Child3(a,c,b,d){
	Parent.apply(this,arguments);
}
Child.prototype = new Parent();
//如下例：
function Child4(name){
	Parent.apply(this,arguments);  //借用构造函数
}
Child4.prototype = new Parent();  //改变子类原型
var kid4 = new Child4("Patrick"); //这将调用父对象构造函数
console.log(kid4.name); //输出：Patrick
console.log(kid4.say()); //输出：Patrick
delete kid4.name; ///删除kid4自己的name属性，但其中还有父对象原型中的name
console.log(kid4.say()); //输出： Adam

//类继承模式：  共享原型： 即根据经验法则：可复用成员应该转移到原型中而不是放置在this中。出于继承的目的，任何值得继承的
//东西都应该放置在原型中实现，所以，可以仅将子对象的原型与父对象的原型设置为相同即可。其缺点是：如果继承锚下方的某处存在一个子对象或者
//孙子对象修改了原型，它将会影响到所有的父对象和祖先对象，并且子对象并不会继承父对象的thi中的属性.  形式如下：
function inheritPrototype(C,P){
	C.prototype = P.prototype;
}

//类式继承模式： 临时构造函数
function inheritTemp(C,P){
	var F = function(){};  //中间代理函数
	F.prototype = P.prototype;  //代理函数原型指向父对象原型
	C.prototype = new F();  //子对象的原型一个代理函数实例  ,这样子对象将不会继承任何父对象的this属性.
}

//临时构造函数的补充: 添加存储超类的属性uber
function inheritUber(C,P){
	var F = function(){};
	F.prototype = P.prototype;
	C.prototype = new F();
	C.uber = P.prototype;  //子对象的uber存储了父对象的原型，以备不时之需.这时C的constructor指向了Parent(P)
}

//临时构造函数的补充,重置构造函数指针(添加了存储超类后，C的constructor指向了P,而非C自己）
function inheritCPoint(C,P){
	var F = function(){};
	F.prototype = P.prototype;
	C.prototype = new F();
	C.uber = P.prototype;  
	c.prototype.constructor  = C;
}

//注意： 以上模式也称为代理函数或代理构造函数模式
//终极圣杯模式的优化：  为避免在每次需要继承时都创建（临时）构造函数。（即仅创建一次临时构造函数，并且修改它的原型，已足够）
//可以使用即时函数并且在闭包中存储代理函数.如下：
var inheritLast = (function(){
	var F = function() {};
	return function(C,P){
		F.prototype = P.prototype;
		C.prototype = new F();
		C.uber = P.prototype;  
		c.prototype.constructor  = C;
	};
}());

//伪类： Klass实现
var klass = function(Parent,props){
	var Child,F,i;
	//新建构造函数
	Child = function(){
		if(Child.uber && Child.uber.hasOwnProperty("_construct")){
			Child.uber._construct.apply(this,arguments);
		}
		if(Child.prototype.hasOwnProperty("_construct")){
			Child.prototype._construct.apply(this.arguments);
		}
	};
	//实现继承人
	Parent = Parent || Object;
	F = function(){};
	F.prototype = Parent.prototype;
	Child.prototype = new F();
	Child.uber = Parent.prototype;
	Child.prototype.constructor = Child;
	
	//添加实现方法
	for(i in props){
		if(props.hasOwnProperty(i)){
			Child.prototype[i] = props[i];
		}
	}
	//返回继承后的Child
	return Child;  
};
//klass的使用.
var Man = klass(null,{
	_construct : function(what){
		console.log("Man's constructor!");
		this.name = what;
	},
	getName:function(){
		return this.name;
	}
});
var first = new Man('Admin');
//console.log(first.getName());

var SuperMan = klass(Man,{
	_construct: function(what){
		console.log("SuperMan's constructor");
	},
	getName:function(){
		var name = SuperMan.uber.getName.call(this);
		return "I am" + name;
	}
});

var clark = new SuperMan("Clark kent");
//console.log(clark.getName());

//ECMAScript5中已实现了Object.create(parent,{});原型继承模式：
var parent = new Parent();
var child = Object.create(parent,{
	   age:{value : 2}  //ECMA5描述符
});
//console.log(child.hasOwnProperty("age"));  //结果为true
//通过复制属性实现继承（浅复制+深度复制)
//浅复制(只复制对象属性和引用（不复制对象中的对象的属性)这种方式子对象中会有和父对象一样的对象引用，修改子类就是修改父类
function extend(parent,child){
	var i;
	child = child || {};
	for(i in parent){
		if(parent.hasOwnProperty(i)){
			child[i] = parent[i];
		}
	}
	return child;
}
var dad = {name: "Adam"};
var kid = extend(dad);
//console.log(kid.name);
var dad = {
	counts: [1,2,3],
	reads:{paper:true}
};
var kid = extend(dad);
kid.counts.push(4);
//console.log(dad.counts.toString());//1,2,3,4  访问的是父类对象
//console.log((dad.reads === kid.reads)); //结果为true

//深度复制(如果某个属性为对象，则要递归的复制的出该对象的属性。另外，还需要检查该对象是否为一个真实对象或者一个数组。）这将会创建真实副本

function extendDeep(parent,child){
	var i, toStr = Object.prototype.toString,astr = "[object Array]";
	child = child || [];
	for(i in parent){
		if(parent.hasOwnProperty(i)){
			if(typeof parent[i] === "object"){
				child[i] = (toStr.call(parent[i]) === astr) ? [] : {};
				extendDeep(parent[i],child[i]);
			}else{
				child[i] = parent[i];
			}
		}
	}
	return child;
}

var dad = {
		counts: [1,2,3],
        reads:{paper:true}
};
var kid = extendDeep(dad);
kid.counts.push(4);
//console.log(kid.counts.toString()); //结果为"1,2,3,4"  子对象有自己的副本
//console.log(dad.counts.toString());//结果为: 1,2,3

//多对象混入(mix-in)复制模式 (即：将多个对象的属性复制到子对象中返回）
function mixIn(){
	var arg,prop,child = {};
	for(arg = 0; arg < arguments.length; arg+=1){
		for(prop in arguments[arg]){
			if(arguments[arg].hasOwnProperty(prop)){
				child[prop] = arguments[arg][prop];
			}
		}
	}
	return child;
}

var cake = mixIn(
 {eggs: 2,large:true},
 {butter:1,salted:true},
 {flour: "3 cups"},
 {suger: "sure!"}
);
//console.dir(cake);

//方法借用模式：将A对象的方法，临时借调过来像自己的对象一样使用
//即apply()和call()方法,如下：
//notmyobj.doStuff.call(myobj,param1,p2,p3);
//notmyobj.doStuff.apply(myobj,[param1,p2,p3]);

//借用的补充：   绑定
//借用时，方法的内部，this所指向的对象是基于调用表达式而确定的。但有时最好能够“锁定" this的值，或者将其绑定到特定对象并预先确定该对象.
var one = {
		name : "object",
		say: function(greet){
			return greet + ", "+this.name;
		}
};
var two = {
		name : "another object"
};
//将one.say方法作为two对象来调用
console.log(one.say.apply(two,['hello']));  //会显示 "hello ,another object  这时say方法内部的this会指向two对象

//绑定时会引发的将this指向全局变量问题。如：
var say = one.say;
say('hoho');//结果为： hoho,undefined   这时this指向的是window
//作为回调函数
var yetanother = {
		name : "Yet another object",
		method : function(callback){
			return callback("hola");
		}
};

//console.log(yetanother.method(one.say));  //传进去的话，this一样指向全局变量
//用bind方法来避免this指向全局变量,这里返回的函数可以通过闭包来访问，即使在bind()返回后，内部函数仍然可以访问o和m,并且总是指向原始对象和方法
function bind(o,m){
	return function(){
		return m.apply(o,[].slice.call(arguments));
	};
}
//如：
var twosay = bind(two,one.say);
console.log(twosay('yo'));   //结果为: you,another object 
//ECMAScript5中实现的Function.prototype.bind()方法：
//如： var newFunc = obj.someFunc.bind(myobj,1,2,3);

//Function.prototype.bind()实现,如果不是在ECMAScript5中使用时，可以执行以下代码）
function  initBind(){
	if(typeof Function.prototype.bind === "undefined"){
		Function.prototype.bind = function(thisArg){
			var fn = this,slice = Array.prototype.slice,args = slice.call(arguments,1);
			return function(){
				 return fn.apply(thisArg,args.concat(slice.call(arguments)));
			};
		};
	}
}

//直接使用ECMA5Script的bind
var twosay2 = one.say.bind(two);
console.log(twosay2("Bonjour"));

var twosay3 = one.say.bind(two,"Enchante");
console.log(twosay3());

