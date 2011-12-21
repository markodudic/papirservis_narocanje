var path = window.location.pathname.split('/');
	
var Hydra = {
		'pageContext':"/"+path[1],
		'customPageId':path[3],
		'pageEntryCode':path[4]
		};

function initLanguages() {

	//language bar
	var obj = {'sl-SL':
					{image: Hydra.pageContext+'/img/icons/slSL_icon.png', imageClass: 'languageBarIcons',
            		onClick: function(){
						changeLanguage('sl-SL');
    				},
    				label:MooTools.lang.get('msg','header.slSLTitle')
					},
				'sr-SR':
					{image: Hydra.pageContext+'/img/icons/srSR_icon.png', imageClass: 'languageBarIcons',
	            	onClick: function(){
						changeLanguage('sr-SR');
					},
					label:MooTools.lang.get('msg','header.srSRTitle')
					},
				'us-US':
					{image: Hydra.pageContext+'/img/icons/usUS_icon.png', imageClass: 'languageBarIcons',
	            	onClick: function(){
						changeLanguage('us-US');
					},
					label:MooTools.lang.get('msg','header.usUSTitle')
					},
				'de-DE':
					{image: Hydra.pageContext+'/img/icons/deDE_icon.png', imageClass: 'languageBarIcons',
			    	onClick: function(){
						changeLanguage('de-DE');
					},
					label:MooTools.lang.get('msg','header.deDETitle')
					}
				};
	var languageArray = ['sl-SL','sr-SR','us-US','de-DE'];
	
	var items = [obj[MooTools.lang.getCurrentLanguage()]];
	
	for ( var int = 0; int < languageArray.length; int++) {
		if(languageArray[int] != MooTools.lang.getCurrentLanguage())
			items.push(obj[languageArray[int]]);
	}
	
    new Jx.Button.Combo({
	        label: MooTools.lang.get('msg','header.language'),
	        items: items
	}).addTo('languageBar');
	
}

function changeLanguage(language){
	this.cookie.set('lang', language);
	MooTools.lang.setLanguage(language);
	setLang(language);
	initComponents(language);
	
}

function init() {
	if ($('status').value == 0) {
		$('headerac').set('html',MooTools.lang.get('msg','header.hello') + ", " + $('name').value + " " + $('surname').value);
		$('headerlo').set('html',MooTools.lang.get('msg','header.logout'));
	} else {
		$('headerac').set('html',MooTools.lang.get('msg','header.account'));
		$('headerlo').set('html',MooTools.lang.get('msg','header.login'));
	}
	$('headerti').set('title',MooTools.lang.get('msg','header.hydra'));

	
	getLanguage();
	initLanguages();
} 

function logout () {
	if ($('status').value == 0) {
		//odjavim uporabnika
		new Request({
			method: 'post', 
			url: 'logout',
			'onSuccess':function(result){
		}}).send();
	}
}

function getLanguage() {
	MooTools.lang.setLanguage('sl-SL');

	this.cookie = new Hash.Cookie('hydraCookie', {
		path: '/',
		duration: 365
	});
	
	//check cookie value for language
	if (!$defined(this.cookie.get('lang'))) {
		this.cookie.set('lang', 'sl-SL');
	}
	
	MooTools.lang.setLanguage(this.cookie.get('lang'));
	return this.cookie.get('lang');
} 

window.addEvent('domready', function(){
	$('img_logo').set('src',Hydra.pageContext+'/img/ps.jpg');
	init();
});