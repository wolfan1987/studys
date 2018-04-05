//单例模式之---静态属性中的实例
function Universe(){
	//如果已有一个实例
	if(typeof Universe.instance === "object"){
		return  Universe.instance; 
	}
	
	this.start_time = 0;
	this.bang = "Big";
	//缓存
	Universe.instance = this;
	//隐式返回
	return this;
}
var uni1 = new Universe();
var uni2 = new Universe();
console.log(uni1 === uni2);

////单例模式之---闭包中的实例
function Universe(){
	//缓存实例
	var instance = this;
	
	//正常初始化
	this.start_time = 0;
	this.bang = "Big";
	
	//用闭包函数，重写构造函数
	Universe = function(){  
		return instance;  //这会引起constructor不相等,外面的和里面返回的是两个不同的构造函数
	};
}

var uni1 = new Universe();
var uni2 = new Universe();
console.log(uni1 === uni2);  //true
console.log(uni1.constructor === Universe);  //false

//解决构造函数不相等的方法
function Universe(){
	//缓存实例
	var instance;
	//重写构造函数
	Universe = function Universe(){
		return instance;
	};
	
	//保留原型属性
	Universe.prototype = this;
	//实例
	instance = new Universe();
	//重置构造函数指针
	instance.constructor = Universe;
	//定义功能
	instance.start_time = 0;
	instance.bang = "Big";
	
	return instance;
}
Universe.prototype.nothing = true;
var uni1 = new Universe();
Universe.prototype.everything = true;
var uni2 = new Universe();
//console.log((uni1 = uni2)); //true
//console.log(uni1.nothing && uni1.everything && uni2.nothing && uni2.everything);
//console.log(uni1.constructor === Universe);  

var Universe;
(function(){
	var instance;
	
	Universe = function Universe(){
		if(instance){
			return instance;
		}
		instance = this;
		
		this.start_time = 0;
		this.bang = "Big";
	};
}());

//工厂模式

//父构造函数
function CarMaker(){};

CarMaker.prototype.drive = function(){
	return "Vroom, I have " + this.doors + " doors";
};

//静态工厂方法
CarMaker.factory = function(type){
	var constr = type, newcar;
	
	//如果构造函数不存在，则发生错误
	if(typeof CarMaker[constr] !== "function"){
		throw {
			name : "Error",
			message: constr + " doesn't exist!"
		};
	}
	
	//构造已知构造函数，使用原型继承父类，但仅继承一次
	if(typeof CarMaker[constr].prototype.drive !== "function"){
		CarMaker[constr].prototype = new CarMaker();
	}
	
	//创建一个新的实例
	newcar = new CarMaker[constr]();
	return newcar;
};

CarMaker.Compact= function(){
	this.doors = 4;
};
CarMaker.Convertible = function(){
	this.doors = 2;
};
CarMaker.SUV = function(){
	this.doors = 24;
};

//使用
var corolla = CarMaker.factory("Compact");
var solstice = CarMaker.factory("Convertible");
var cherokke = CarMaker.factory("SUV");
console.log(corolla.drive());
console.log(solstice.drive());
console.log(cherokke.drive());

var data = [1,2,3,4,5,6,7];
//迭代器模式
var myiterator = (function(data){
	var index = 0,length = data.length;
	return {
		next: function(){
			var element;
			if(!this.hasNext()){
				return null;
			}
			
			element = data[index];
			index = index+2;
			return element;
		},
		hasNext: function(){
			return index < length;
		},
		rewind: function(){
			index = 0;
		},
		current: function(){
			return data[index];
		}
	};
}(data));

//while(myiterator.hasNext()){
	//console.log(myiterator.next());
//}
//myiterator.rewind();
//console.log(myiterator.current());

//装饰者模式
//可以在运行时动态添加附加功能到对象，有一个比较方便的特征在于其预期行为的可定制和可配置特性，可以从
//有一些基本功能的普通对象开始，然后从可用装饰资源池中选择需要用于增强普通对象的那些功能，并且按照顺序进行装饰，
//尤其是当装饰顺序很重要的时间.使得每个装饰者成为一个对象，并且该对象包含了应该被重载的方法，每个装饰者实际上继承了目前已经被前一个装饰
//者进行增强后的对象，每个装饰对象uber(继承的对象)上调用了方法并获取其值，此外它还继续执行了一些操作。
//相当于执行结果累加或不同业务顺序的组合
//基本功能
function Sale(price){
	this.price = price || 100;
	this.decorators_list = [];
}

Sale.prototype.getPrice = function(){
	return this.price;
};


//Sale的装饰对象执行器
Sale.prototype.decorate = function(decorator){
	var F = function(){}, overrides = this.constructor.decorators[decorator], i , newobj;
	F.prototype = this;
	newobj = new F();
	newobj.uber = F.prototype;
	for(i in overrides){
		if(overrides.hasOwnProperty(i)){
			newobj[i] = overrides[i];
		}
	}
	
	return newobj;
};

//装饰者对象集合
Sale.decorators = {};

Sale.decorators.fedtax = {
	getPrice : function(){
		//得到上次执行的结果再参与计算
		var price = this.uber.getPrice();
		price += price * 5 /100;
		return price;
	}	
};
Sale.decorators.quebec = {
	getPrice: function(){
		var price = this.uber.getPrice();
		price += price * 7.5 /100;
		return price;
	}
};

Sale.decorators.money = {
		getPrice: function(){
			return "$" + this.uber.getPrice().toFixed(2);
		}
};
Sale.decorators.cdn = {
	getPrice: function(){
		return "CDN$ "+ this.uber.getPrice().toFixed(2);
	}	
};

//装饰模式的使用
var sale = new Sale(100);  //初始价格
sale = sale.decorate("fedtax");  //在初始价格上+税费
sale = sale.decorate("quebec");  //在计算完fedtax后，再加上一个费用
sale = sale.decorate("money");  //格式化
console.log(sale.getPrice());

//使用列表实现装饰者模式

function Sale(price){
	this.price = price || 100;
	this.decorators_list = [];
}

Sale.decorators = {};
Sale.decorators.fedtx = {
		getPrice: function(price){
			return price + price * 5 /100;
		}
};
Sale.decorators.quebe = {
	getPrice:function(price){
		return price + price * 7.5 /100;
	}	
};
Sale.decorators.money = {
	getPrice:function(price){
		return "$" + price.toFixed(2);
	}	
};

Sale.prototype.decorate = function(decorator){
	this.decorators_list.push(decorator); //将所有装饰对象加入数组
};

Sale.prototype.getPrice = function(){
	var price = this.price, i , max = this.decorators_list.length, name;
	for( i = 0; i < max; i += 1){
		name = this.decorators_list[i];  //得到装饰者对象
		price = Sale.decorators[name].getPrice(price);  //执行每一个装饰对象，并得到其价格
	}
	return price;
};

//策略模式：
var validator = {
		//所有可用的检查
		types:{},
		//错误消息
		messages:[],
		//当前验证配置，名称 ： 验证类型
		config:[],
		//接口方法，data为键值对
		validate: function(data){
			var  i , msg ,type,checker,result_ok;
			//重置所有消息
			this.message = [];
			for(i in data){  //要检查的数据
				if(data.hasOwnProperty(i)){  //如果有这个属性
					type = this.config[i]; //验证类型
					checker = this.types[type];  //验证函数
					
					if(!type){
						continue;  //不需要验证
					}
					if(!checker){
						throw {
							name: "ValidationError",
							message: " No handler to validate type "+ type
						};
					}
					
					//用checker验证
					result_ok = checker.validate(data[i]);
					if(!result_ok){
						msg = "Invalid value form * "+ i + "*, "+checker.instructions;
						this.messages.push(msg);
					}
				}
			}
			return this.hasErors();
		},
		
		hasErors: function(){
			return this.messages.length !== 0;
		}
};

//非空检查
validator.types.isNoEmpty = {
		validate: function(value){
			return value !== "";
		},
		instructioins: "the value cannot be empty"
};
//检查是否为数字
validator.types.isNumber={
		validate:function(value){
			return !isNaN(value);
		},
		instructions: "thie value can only be a valid number , e.g. 1, 3.14 or 2010"
};

//检查是否只包含字母和数字
validator.types.isAlphaNum={
		validate: function(value){
			return !/[^a-z0-9]/i.test(value);
		},
		instructions: "the value can only contain characters and numbers, on special symbols"
};

var data = {
		fist_name: "SuperMan",
		last_name:"Man",
		age : "unknown",
		username: "o_0"
};

validator.config = {
	first_name:"isNonEmpty",
	age: "isNumber",
	username: "isAlphaNum"
};

validator.validate(data);
if(validator.hasErors()){
	console.log(validator.messages.join("\n"));
}
