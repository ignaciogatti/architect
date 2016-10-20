function showErrorMessages(response){
	if (response!=undefined && response!=null) {
		var errorMessage = $(response).find("errorMessage")[0];
		if (errorMessage!=undefined && errorMessage!=null) {
			var errors = errorMessage.getAttribute("message");
			$.gritter.add({
				title : " ",
				text : errors,
				image : 'resources/images/gritter-error.png',
				time : 4000
			});
			return true;
		}
	}
	return false;
}