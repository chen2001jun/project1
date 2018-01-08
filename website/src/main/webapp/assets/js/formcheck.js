function formcheck()
{
	var old_tips = {};
	//检查格式是否正确
	this.checkFormat = function(obj_name,check_type,error_msg)
	{
		if(typeof(old_tips['var_'+obj_name])){old_tips['var_'+obj_name]=$('#'+obj_name+'_tips').html();}
		var val = $.trim($('#'+obj_name).val());
		if(this.check(check_type,val))
		{
			$('#'+obj_name+'_tips').html('').removeClass('error').addClass("right");
			return true;
		}
		else
		{
			$('#'+obj_name).focus().select();
			$('#'+obj_name+'_tips').html(error_msg).removeClass("right").addClass("error");
			return false;
		}
		
	};
	//检查格式是否正确
	this.checkFormatBind = function(obj_name,check_type,error_msg)
	{
		var val = $.trim($('#mobileBind').val());
		if(this.check(check_type,val))
		{
			$('#'+obj_name+'_tips').html('');
			return true;
		}
		else
		{
			$('#'+obj_name).addClass('form-group error');
			$('#'+obj_name+'_tips').html('<i class="fa fa-times"></i>'+error_msg);
			return false;
		}

	};

	//检查格式是否正确
	this.checkFormatLogin = function(obj_name,check_type,error_msg)
	{
		var val = $.trim($('#mobileLogin').val());
		if(this.check(check_type,val))
		{
			$('#'+obj_name+'_tips').html('');
			return true;
		}
		else
		{
			$('#'+obj_name).addClass('form-group error');
			$('#'+obj_name+'_tips').html('<i class="fa fa-times"></i>'+error_msg);
			return false;
		}

	};

	this.checkFormatResetPwd = function(obj_name,check_type,error_msg)
	{
		var val = $.trim($('#password1').val());
		if(this.check(check_type,val))
		{
			$('#'+obj_name+'_tips1').html('').hide();
			return true;
		}
		else
		{

			$('#'+obj_name+'_tips1').show().html(error_msg);
			return false;
		}

	};

	this.check = function(type,val)
	{
		switch(type)
		{
			case "email":{if(!/^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/.test(val)){return false;}else {return true};break;}
			case "phone":{if(!/^1[3-8]\d{9}$/.test(val)){return false;}else {return true};break;}
			case "idcard":{if(!/(^\d{15}$)|(^\d{17}([0-9]|X)$)/.test(val)){return false;}else {return true};break;}
			case "password":{if(!/^\w{6,20}$/.test(val)){return false;}else {return true};break;}
			case "other" :{if(val==''){return false;}else{return true;} break;}
			case "error": return false;break;
		}
		alert('checktype error');
		return false;
	};
	
	this.checkEmail = function(email)
	{
		if(!/^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/.test(email))
			return false;
		else
			return true;
	};
	
	this.checkPhone = function(phone)
	{
		if(!/^1[3-8]\d{9}$/.test(phone)){return false;}
		else return true;
	};
	
	this.checkIdcard = function()
	{
		var re = /(^\d{15}$)|(^\d{17}([0-9]|X)$)/;
		if (!re.test(idcard.value)) 
		{
		   return false;
		}
	};
	
	this.changeTimes = function(obj,fun)
	{
		var times = parseInt(obj.html());
		if(times>0)
		{
			obj.html(--times);
		}
		else
		{
			eval(fun+"()");
			clearInterval(send_interval);
		}
	};
	this.overCallback = function()
	{
		$('#msg_end,#sendmsg_time').hide();
		$('#sendmsg_button').show();
	};
	this.submitForm = function()
	{
		if($('#phone_code').val()=='')
		{
			alert('没有填写手机验证码');
			$('#phone_code').focus();
			return false;
		}
		if($('#password').val()!=$('#repassword').val())
		{
			alert('两次密码不一致');
			return false;
		}
		if($('#idcard').val()=='')
		{
			alert('身份证号码为空');
			return false;
		}
		if($('#idcard_type_id').val()==1)
		{
			//判断身份证是否合法
			var re = /(^\d{15}$)|(^\d{17}([0-9]|X)$)/;
		  	if (!re.test(idcard.value)) 
			{
			   alert("身份证号格式不正确！");
			   $('#idcard').focus();
			   return false;
		  	}
		}
		return true;
	}
}
var OBJ_formcheck = new formcheck();
