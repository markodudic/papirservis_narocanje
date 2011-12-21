var subjectOption = '';
var materialsOption = '';
var usersOption = '';
var print_html = '';


function initComponents(lang) {
	MooTools.lang.setLanguage(lang);

	//getSubjects();
	
	new Request({method: 'get', url: 'subjects',
		'onSuccess':function(result){
			subjectOption = JSON.decode(result);
			addSubjects(subjectOption);

			new Request({method: 'get', url: 'materials',
				'onSuccess':function(result){
					materialsOption = JSON.decode(result);
					addMaterials(materialsOption);

					new Request({method: 'get', url: 'users',
						'onSuccess':function(result){
							usersOption = JSON.decode(result);
							addUsers(usersOption);
					}}).send();
			}}).send();
	}}).send();

	
	
	
	$('toolbar').set('id','_old_toolbar');
	tlb = new Element('div').set('id','toolbar');
	tlb.inject($('_old_toolbar'),'after');
	$('_old_toolbar').destroy();

	var tb = new Jx.Toolbar();
	tb.addTo('toolbar');
	
	var confirmBtn = new Jx.Button({
		label: MooTools.lang.get('msg','dispatcher.potrdi'),
		image: Hydra.pageContext+'/img/confirm.png',
		onClick:function(){confirm();}
	});
	confirmBtn.addTo('toolbar');
	var clearBtn = new Jx.Button({
		label: MooTools.lang.get('msg','dispatcher.ponastavi'),
		image: Hydra.pageContext+'/img/magnifier.png',
		onClick:function(){reset();}
	});
	clearBtn.addTo('toolbar');
	var pdfBtn = new Jx.Button({
		label: MooTools.lang.get('msg','dispatcher.pdf'),
		image: Hydra.pageContext+'/img/icon_pdf.gif',
		onClick:function(){tiskaj();}
	});
	pdfBtn.addTo('toolbar');
	
	// texti
	$('h5').set('html',MooTools.lang.get('msg','dispatcher.seznam_obrazcev'));
	$('l_stranka').set('html',MooTools.lang.get('msg','dispatcher.stranka'));
	$('l_naslov').set('html',MooTools.lang.get('msg','dispatcher.naslov'));
	$('l_kraj').set('html',MooTools.lang.get('msg','dispatcher.kraj'));
	$('l_kontakt').set('html',MooTools.lang.get('msg','dispatcher.kontakt'));
	$('l_telefon').set('html',MooTools.lang.get('msg','dispatcher.telefon'));
	$('l_kupec').set('html',MooTools.lang.get('msg','dispatcher.kupec'));

	$('l_datum').set('html',MooTools.lang.get('msg','dispatcher.datum'));
	$('l_odpadek').set('html',MooTools.lang.get('msg','dispatcher.odpadek'));
	$('l_kolicina').set('html',MooTools.lang.get('msg','dispatcher.kolicina'));
	$('l_osnovno').set('html',MooTools.lang.get('msg','dispatcher.osnovno'));
	$('l_opomba').set('html',MooTools.lang.get('msg','dispatcher.opomba'));
	$('l_narocil').set('html',MooTools.lang.get('msg','dispatcher.narocil'));

		
	var lfs = ['dobavitelj','naslov','narocilo','evl','datum_narocila',
	           'datum_izvedbe','vrsta_odpadka','klasifikacijska_stevilka','kolicina',
	           'placnik','kontakt','opomba','vozilo'];
	for (var i = 0; i < lfs.length; i++) {
		$(lfs[i]).set('html',MooTools.lang.get('msg','dispatcher.'+lfs[i]));
	}
			
			
	$$('.chooser-date').each( function(el){
		var picker = new SlimPicker(el,{
			format: MooTools.lang.get('msg', 'datepicker.format'),
			dayNames: JSON.decode("["+MooTools.lang.get('msg', 'datepicker.daynames')+"]"),
			monthNames: JSON.decode("["+MooTools.lang.get('msg', 'datepicker.monthnames')+"]")
		});
	});	

}

function getDocuments () {
	new Request({
		method: 'get', 
		url: 'dispatcher-table',
		data: niprvic,
		'onSuccess':function(result){
	}}).send();
}	


function reset () {
	$('selectStranka').value = -1;
	$('selectOdpadek').value = -1;
	$('selectNarocil').value = -1;
	$('naslov').value = '';
	$('kraj').value = '';
	$('kontakt').value = '';
	$('telefon').value = '';
	$('kupec').value = '';
	$('osnovno').value = '';
	$('datum').value = '';
	$('kolicina').value = '';
	$('opomba').value = '';
}


function confirm(){
	if ($('selectStranka').value == -1) {
		alert(MooTools.lang.get('msg','dispatcher.izberi_stranko_error'));
		return;
	}
	if ($('selectNarocil').value == -1) {
		alert(MooTools.lang.get('msg','dispatcher.izberi_uporabnika_error'));
		return;
	}
	if (isNaN($('kolicina').value)) {
		alert(MooTools.lang.get('msg','dispatcher.izberi_kolicino_error'));
		return;
	}
	if ($('datum').value == '') {
		alert(MooTools.lang.get('msg','dispatcher.izberi_datum_error'));
		return;
	}
	var f = $('datum').value.split(".");
	var fr = new Date(f[2],f[1]-1,f[0]);
	var frd = new Date(fr.getTime());
	var t = new Date();
	var tr = new Date(t.getFullYear(),t.getMonth(),t.getDate());
	var trd = new Date(tr.getTime());
	if (frd < trd) {
		alert(MooTools.lang.get('msg','dispatcher.izberi_datum_error_2'));
		return;
	}

	var podatkiJSON = {};
	podatkiJSON["stranka"] = $('selectStranka').value;
	podatkiJSON["material"] = $('selectOdpadek').value;
	podatkiJSON["narocil"] = $('selectNarocil').value;
	podatkiJSON["datum"] = $('datum').value;
	podatkiJSON["kolicina"] = $('kolicina').value;
	podatkiJSON["opomba"] = $('opomba').value;
	var j = {};
	j.confirmData = JSON.encode(podatkiJSON);		
	
	new Request({
		method: 'get', 
		url: 'confirm',
		data: j,
		'onSuccess':function(result){}
	}).send();
}


function tiskaj() {
	
	var stranka = "";
	if ($('selectStranka').selectedIndex != 0) 
		stranka = $('selectStranka').options[$('selectStranka').selectedIndex].text;
	var odpadek = "";
	if ($('selectOdpadek').selectedIndex != 0) 
		odpadek = $('selectOdpadek').options[$('selectOdpadek').selectedIndex].text;
	var narocil = "";
	if ($('selectNarocil').selectedIndex != 0) 
		narocil = $('selectNarocil').options[$('selectNarocil').selectedIndex].text;
	
	print_html = '<table>';
	print_html += 
	'<tr>' +
		'<td align="left">'+MooTools.lang.get('msg','dispatcher.stranka')+'</td>' +
		'<td align="left">'+stranka.substr(0,stranka.indexOf(";"))+'</td>' +
	'</tr>' +
	'<tr>' +
		'<td align="left">'+MooTools.lang.get('msg','dispatcher.naslov')+'</td>' +
		'<td align="left">'+$('naslov').value+'</td>' +
	'</tr>' +
	'<tr>' +
		'<td align="left">'+MooTools.lang.get('msg','dispatcher.kraj')+'</td>' +
		'<td align="left">'+$('kraj').value+'</td>' +
	'</tr>' +
	'<tr>' +
		'<td align="left">'+MooTools.lang.get('msg','dispatcher.kontakt')+'</td>' +
		'<td align="left">'+$('kontakt').value+'</td>' +
	'</tr>' +
	'<tr>' +
		'<td align="left">'+MooTools.lang.get('msg','dispatcher.telefon')+'</td>' +
		'<td align="left">'+$('telefon').value+'</td>' +
	'</tr>' +
	'<tr>' +
		'<td align="left">'+MooTools.lang.get('msg','dispatcher.kupec')+'</td>' +
		'<td align="left">'+$('kupec').value+'</td>' +
	'</tr>' +
	'<tr>' +
		'<td align="left">'+MooTools.lang.get('msg','dispatcher.datum')+'</td>' +
		'<td align="left">'+$('datum').value+'</td>' +
	'</tr>' +
	'<tr>' +
		'<td align="left">'+MooTools.lang.get('msg','dispatcher.odpadek')+'</td>' +
		'<td align="left">'+odpadek+'</td>' +
	'</tr>' +
	'<tr>' +
		'<td align="left">'+MooTools.lang.get('msg','dispatcher.kolicina')+'</td>' +
		'<td align="left">'+$('kolicina').value+'</td>' +
	'</tr>' +
	'<tr>' +
		'<td align="left">'+MooTools.lang.get('msg','dispatcher.osnovno')+'</td>' +
		'<td align="left">'+$('osnovno').value+'</td>' +
	'</tr>' +
	'<tr>' +
		'<td align="left">'+MooTools.lang.get('msg','dispatcher.opomba')+'</td>' +
		'<td align="left">'+$('opomba').value+'</td>' +
	'</tr>' +
	'<tr>' +
		'<td align="left">'+MooTools.lang.get('msg','dispatcher.narocil')+'</td>' +
		'<td align="left">'+narocil+'</td>' +
	'</tr>';
	print_html += '</table>';
    
	$('form_title').value=MooTools.lang.get('msg','welcome.title');
	$('form_html').value=print_html;
	$('listForms').action = Hydra.pageContext+"/pdf";
    $('listForms').submit();	
}



function addSubjects() {
	var subjectOptionData = '<option value=-1 selected>'+MooTools.lang.get('msg','dispatcher.izberi_stranko')+'</option>';
	for (var ii=0;ii<subjectOption.length;ii++) {
		subjectOptionData += '<option value='+subjectOption[ii]['sifra']+'>'+subjectOption[ii]['naziv']+"; "+subjectOption[ii]['naslov']+" "+subjectOption[ii]['posta']+" "+subjectOption[ii]['kraj']+"; "+subjectOption[ii]['osnovna']+'</option>';
	}
	$('selectStranka').set('html', subjectOptionData);
}

function changeSubject() {
	var selectStranka = $('selectStranka').value;
	for (var ii=0;ii<subjectOption.length;ii++) {
		if (subjectOption[ii]['sifra'] == selectStranka) {
			$('naslov').value = subjectOption[ii]['naslov'];
			$('kraj').value = subjectOption[ii]['posta']+" "+subjectOption[ii]['kraj'];
			$('kontakt').value = subjectOption[ii]['kont_os'];
			$('telefon').value = subjectOption[ii]['telefon'];
			$('kupec').value = subjectOption[ii]['kupec'];
			$('osnovno').value = subjectOption[ii]['osnovna'];
			$('opomba').value = subjectOption[ii]['opomba'];
		}
	}
}

function addMaterials() {
	var materialsOptionData = '<option value=-1 selected>'+MooTools.lang.get('msg','dispatcher.izberi_material')+'</option>';
	for (var ii=0;ii<materialsOption.length;ii++) {
		materialsOptionData += '<option value='+materialsOption[ii]['koda']+'>'+materialsOption[ii]['koda']+" "+materialsOption[ii]['material']+'</option>';
	}
	$('selectOdpadek').set('html', materialsOptionData);
}

function addUsers() {
	var usersOptionData = '<option value=-1 selected>'+MooTools.lang.get('msg','dispatcher.izberi_uporabnika')+'</option>';
	for (var ii=0;ii<usersOption.length;ii++) {
		usersOptionData += '<option value='+usersOption[ii]['id']+'>'+usersOption[ii]['name']+'</option>';
	}
	$('selectNarocil').set('html', usersOptionData);
}






var cmu = [
        {
           dataIndex: 'dobavitelj',
           dataType:'string',
           width:200
        },
        {
           dataIndex: 'naslov',
           dataType:'string',
           width:200
        },
        {
           dataIndex: 'narocilo',
           dataType:'string',
           width:80
        },
        {
            dataIndex: 'evl',
            dataType:'string',
            width:80
         },
        {
           dataIndex: 'datum_narocila',
           dataType:'string',
           width:100
        },
        {
           dataIndex: 'datum_izvedbe',
           dataType:'string',
           width:100
        },
        {
           dataIndex: 'vrsta_odpadka',
           dataType:'string',
           width:200
        },
        {
           dataIndex: 'klasifikacijska_stevilka',
           dataType:'string',
           width:100
        },
        {
           dataIndex: 'kolicina',
           dataType:'string',
           width:50
        },
        {
           dataIndex: 'placnik',
           dataType:'string',
           width:150
        },
        {
           dataIndex: 'kontakt',
           dataType:'string',
           width:100
        },
        {
           dataIndex: 'opomba',
           dataType:'string'
        },
        {
           dataIndex: 'vozilo',
           dataType:'string',
           width:200
        }];	

window.addEvent("domready", function(){
                
    datagrid = new omniGrid('myTableGrid', {
        columnModel: cmu,
        //url:"dispatcher-table",
        perPageOptions: [10,20,50,100,200],
        perPage:10,
        page:1,
        pagination:true,
        alternaterows: true,
        showHeader:true,
        sortHeader:true,
        resizeColumns:true,
        multipleSelection:true,
        serverSort:true,
        sortOn: 'Status_sort, DocumentID',
		sortBy: 'ASC',
		sortOrder: 0,
		height: 500
    });
    
    //datagrid.addEvent('click', onGridSelect);
    
    initComponents(getLanguage());
    		
 });
