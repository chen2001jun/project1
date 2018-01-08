// JavaScript Document

$(document).ready(function(){
    // 下拉菜单
	$('.dropdown').click(function(){
		var $super = $(this).find('.dropdown-toggle');
		if(!$super.next().is(':visible')){
			$super.next().slideDown('fast');
			$(document).one('click',function(){
				$super.next().slideUp('fast');
			})
		}
		else
			$super.next().slideUp('fast');	
		return false;
	});
	
	$('.dropdown-menu').click(function(event){
		event.stopPropagation();
	});
	
	//checkbox 与 radio UI
	if($().iCheck){
		$('input').iCheck({
			checkboxClass: 'icheckbox_minimal-blue',
			radioClass: 'iradio_minimal-blue',
			increaseArea: '20%' // optional
		});
	
		/* icheck 全选/反选*/
		if(jQuery('.checkall').length > 0) {
			$('.checkall').on('ifChecked', function(event){
				var parentTable = jQuery(this).parents('table');										   
				var ch = parentTable.find('tbody input[type=checkbox]');
				//check all rows in table
				ch.each(function(){ 
					$('input').iCheck('check');
				});
			});
			
			$('.checkall').on('ifUnchecked', function(event){
				var parentTable = jQuery(this).parents('table');										   
				var ch = parentTable.find('tbody input[type=checkbox]');
				//check all rows in table
				ch.each(function(){ 
					$('input').iCheck('uncheck');
				});
			});
		}
	}
	
	if(!$().iCheck){
		// check all checkboxes in table
		if(jQuery('.checkall').length > 0) {
			jQuery(document).on('click','.checkall',function(){
				var parentTable = jQuery(this).parents('table');										   
				var ch = parentTable.find('tbody input[type=checkbox]');
				
				if(jQuery(this).is(':checked')) {
				
					//check all rows in table
					ch.each(function(){ 
						jQuery(this).prop('checked',true);
						jQuery(this).parent().addClass('checked');	//used for the custom checkbox style
						jQuery(this).parents('tr').addClass('selected'); // to highlight row as selected
					});
								
				
				} else {
					
					//uncheck all rows in table
					ch.each(function(){ 
						jQuery(this).prop('checked',false); 
						jQuery(this).parent().removeClass('checked');	//used for the custom checkbox style
						jQuery(this).parents('tr').removeClass('selected');
					});	
					
				}
			});
		}
	}
	
	$('.captcha').click(function(){
		$(this).attr('src', ctx+'/assets/captcha.jpg?t=' + new Date().getTime());
	})
});