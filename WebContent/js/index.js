
function initComponents(lang) {
	MooTools.lang.setLanguage(lang);

	$('toolbar').set('id','_old_toolbar');
	tlb = new Element('div').set('id','toolbar');
	tlb.inject($('_old_toolbar'),'after');
	$('_old_toolbar').destroy();

	var tb = new Jx.Toolbar();
	tb.addTo('toolbar');
	
	var searchBtn = new Jx.Button({
		label: MooTools.lang.get('msg','login.potrdi'),
		image: Hydra.pageContext+'/img/confirm.png',
		onClick:function(){login();}
	});
	searchBtn.addTo('toolbar');

	
	// texti
	$('h5').set('html',MooTools.lang.get('msg','login.login'));
	$('l_username').set('html',MooTools.lang.get('msg','login.username'));
	$('l_password').set('html',MooTools.lang.get('msg','login.password'));

}

//todo zakodirati podatke
function login () {
	var podatkiJSON = {};
	podatkiJSON['username'] = $('username').value;
	podatkiJSON['password'] = $('password').value;
	
	var error= false;
	$('l_error').set('html', '');
	if ($('username').value.length == 0) {
		$('l_error').set('html',MooTools.lang.get('msg','login.empty_username') + "<br>");
		error=true;
	}
	if ($('password').value.length == 0) {
		$('l_error').set('html', $('l_error').get('html') + MooTools.lang.get('msg','login.empty_password'));
		error=true;
	}
	if (error) {
		return;
	}
	
	var j = {};
	j.logInData = Base64.encode(JSON.encode(podatkiJSON));		
	
	new Request({
		method: 'post', 
		url: 'login',
		data: j,
		'onSuccess':function(result){
			var login = JSON.decode(result);
			//preverim kaj sem dobil s serverja
			if (login[0].status == 1) {
				$('l_error').set('html',MooTools.lang.get('msg','login.wrong_user_or_pass') + "<br>");
			} else if (login[0].status == 2) {
				$('l_error').set('html',MooTools.lang.get('msg','login.wrong_pass') + "<br>");
			} else {
				window.location=Hydra.pageContext+"/orders";
				//$('loginForm').action = Hydra.pageContext+"/dispatcher";
				//$('loginForm').submit();				
			}
			
	}}).send();
}	


window.addEvent('domready', function() {
	initComponents(getLanguage());
	
});