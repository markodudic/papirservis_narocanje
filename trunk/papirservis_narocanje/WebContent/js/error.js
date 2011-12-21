function initComponents(lang) {
	MooTools.lang.setLanguage(lang);
	$('head').set('html',MooTools.lang.get('msg','error.head'));

	new Request({method: 'get', url: Hydra.pageContext+'/error-msg',
		'onSuccess':function(result){
		$('error_msg').set('html',MooTools.lang.get('msg',result.trim()));
	}}).send();	
}

window.addEvent('domready', function(){
	initComponents(getLanguage());
});