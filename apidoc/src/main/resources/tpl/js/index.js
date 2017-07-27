(function(){

	$('h5').on('click', function(){
		$(this).next('table').toggleClass('fade');
	});

	$('tr.trigger').on('click', function(){
		$(this).toggleClass('open');
		$(this).find('td:nth-child(1) i').toggleClass('hide');
		$(this).next('tr.trigger-data').toggleClass('hide');
	});

})(jQuery);