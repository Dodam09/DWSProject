    
    
     $(document).ready(function() {
        var initBody = $('body');
        var hiddenBtn = $('.Abtn');
        var hiddenPaging = $('#page');
        var hiddenSearch = $('#addBtnDiv');

        window.onbeforeprint = function () {
            hiddenBtn.hide();
            hiddenPaging.hide();
            hiddenSearch.hide(); 
            
            initBody.width('800px');
	    }
	
	
		    window.onafterprint = function () {
		    	hiddenBtn.show();
		        hiddenPaging.show();
		        hiddenSearch.show();
		        initBody.width('50%');
	    }
 	})
 