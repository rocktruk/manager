(function($) {  
  //这里的notSameAndContinuity是验证的名字  
  //default是默认信息  
   $.fn.bootstrapValidator.i18n.nanCompare = $.extend($.fn.bootstrapValidator.i18n.notSameAndContinuity || {}, {  
       'default': 'nan not illgeal'  
   });  
   //validate是验证的方法  
   $.fn.bootstrapValidator.validators.numCompare = {  
       validate: function(validator, $field, options) {  
           var value = $field.val(); 
           if(options.min && value < options.min){
           		return false;
           }
           if(options.max && value > options.max){
           		return false;
           }
           return true;  
       }  
   };  
}(window.jQuery));