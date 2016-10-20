$.fn.dropDownList = function (className, resultsSpanId, selectedValue) {
   
	$("." + className).addClass("flagvisibility");
   	
	$("."+ className + " dt a").click(function() {
    
		$("." + className + " dd ul").toggle('slow');
			});
	
   	$("." + className + " dd ul li a").click(function() {
   
		var text = $(this).html();
   
		$("." + className + " dt a span").html(text);
   
		$("." + className + " dd ul").hide('slow');
		
		$("#" + resultsSpanId).html(getSelectedValue(selectedValue));
		
		$.get("changeDbStatus?id=" + $("#"+resultsSpanId).attr("rel")+ "&status=" + $("#" + resultsSpanId).html() ,function(){
                    $.gritter.add(
                    {
                        title: "Cambio de estado en el Design Bot",
                        text: "La operacion se realizo exitosamente",
                        image: 'resources/images/gritter-ok.png',
                        time: 2000
                     });
		});
		
		$("#" + resultsSpanId).css('visibility' , 'hidden'); 
		});
   
		function getSelectedValue(id) {
    		return $("#" + id).find("dt a span.value").html();
		}
		
		function getSelected()
		{
			return $("#" + resultsSpanId).html();
		}
		
		$(document).bind('click', function(e) {
			var $clicked = $(e.target);
			if (! $clicked.parents().hasClass(className))
			{
    			$("." + className + " dd ul").hide('slow');
    			
			}	
			});
	}; 