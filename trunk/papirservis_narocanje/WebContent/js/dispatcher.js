var subjectOption = '';
var materialsOption = '';
var usersOption = '';
var fileAPIEnabled = true;


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

	
	
	
	//$('toolbar').set('id','_old_toolbar');
	//tlb = new Element('div').set('id','toolbar');
	//tlb.inject($('_old_toolbar'),'after');
	//$('_old_toolbar').destroy();

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
		image: Hydra.pageContext+'/img/fileicons/pdf.png',
		onClick:function(){tiskaj();}
	});
	pdfBtn.addTo('toolbar');
	if (fileAPIEnabled) {
		var xmlBtn = new Jx.Button({
			id: "ifile",
			label: MooTools.lang.get('msg','dispatcher.xml'),
			image: Hydra.pageContext+'/img/fileicons/xml.png',
			onClick:function(){xml();}
		});
		xmlBtn.addTo('toolbar');
	
		fileinputs = new Element('div').set('id','fileinputs');
		fileinputs.inject($('ifile'),'after');
	
		files = new Element('input').set('id','files');
		files.set('type','file');
		files.inject($('fileinputs'));
		
		fakefile  = new Element('div').set('id','fakefile');
		fakefile .inject($('fileinputs'));
	
		var xmlBtn2 = new Jx.Button({
			id: "xmlFile",
			label: MooTools.lang.get('msg','dispatcher.xml2'),
			image: Hydra.pageContext+'/img/fileicons/xml.png'
		});
		xmlBtn2.addTo('fakefile');
	
		
		ffile = new Element('input').set('id','ffile');
		ffile.set('type','text');
		ffile.inject($('xmlFile'),'after');
	}
	
	
	// texti
	$('vnos').set('html',MooTools.lang.get('msg','dispatcher.vnos'));
	$('odprta').set('html',MooTools.lang.get('msg','dispatcher.odprta'));
	$('l_stranka').set('html',MooTools.lang.get('msg','dispatcher.stranka'));
	$('l_naslov').set('html',MooTools.lang.get('msg','dispatcher.naslov'));
	$('l_kraj').set('html',MooTools.lang.get('msg','dispatcher.kraj'));
	$('l_kontakt').set('html',MooTools.lang.get('msg','dispatcher.kontakt'));
	$('l_telefon').set('html',MooTools.lang.get('msg','dispatcher.telefon'));
	$('l_kupec').set('html',MooTools.lang.get('msg','dispatcher.kupec'));
	$('l_potnik').set('html',MooTools.lang.get('msg','dispatcher.potnik'));

	$('l_datum').set('html',MooTools.lang.get('msg','dispatcher.datum'));
	$('l_material').set('html',MooTools.lang.get('msg','dispatcher.material'));
	$('l_kolicina').set('html',MooTools.lang.get('msg','dispatcher.kolicina'));
	$('l_osnovno').set('html',MooTools.lang.get('msg','dispatcher.osnovno'));
	$('l_opomba').set('html',MooTools.lang.get('msg','dispatcher.opomba'));
	$('l_narocil').set('html',MooTools.lang.get('msg','dispatcher.narocil'));

		
	var lfs = ['st_dob','datum','stranka','kupec','potnik','material','kolicina','opomba','narocil'];
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
	


function reset () {
	$('selectStranka').value = -1;
	$('selectMaterial').value = -1;
	$('selectNarocil').value = -1;
	$('i_naslov').value = '';
	$('i_kraj').value = '';
	$('i_kontakt').value = '';
	$('i_potnik').value = '';
	$('i_telefon').value = '';
	$('i_kupec').value = '';
	$('i_osnovno').value = '';
	$('i_datum').value = '';
	$('i_kolicina').value = '';
	$('i_opomba').value = '';
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
	if (isNaN($('i_kolicina').value)) {
		alert(MooTools.lang.get('msg','dispatcher.izberi_kolicino_error'));
		return;
	}
	if ($('i_datum').value == '') {
		alert(MooTools.lang.get('msg','dispatcher.izberi_datum_error'));
		return;
	}
	var f = $('i_datum').value.split(".");
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
	podatkiJSON["material"] = $('selectMaterial').value;
	podatkiJSON["narocil"] = $('selectNarocil').value;
	podatkiJSON["datum"] = $('i_datum').value;
	podatkiJSON["kolicina"] = $('i_kolicina').value;
	podatkiJSON["opomba"] = $('i_opomba').value;
	podatkiJSON["form_html"] = getHtmlForm();
	var j = {};
	j.confirmData = JSON.encode(podatkiJSON);		
	
	new Request({
		method: 'post', 
		url: 'confirm',
		data: j,
		'onSuccess':function(result){
			datagrid.refresh();
		}
	}).send();
}


function getHtmlForm() {
	
	var stranka = "";
	if ($('selectStranka').selectedIndex != 0) 
		stranka = $('selectStranka').options[$('selectStranka').selectedIndex].text;
	var material = "";
	if ($('selectMaterial').selectedIndex != 0) 
		material = $('selectMaterial').options[$('selectMaterial').selectedIndex].text;
	var narocil = "";
	if ($('selectNarocil').selectedIndex != 0) 
		narocil = $('selectNarocil').options[$('selectNarocil').selectedIndex].text;
	
	var print_html = '<table>';
	print_html += 
	'<tr>' +
		'<td align="left">'+MooTools.lang.get('msg','dispatcher.stranka')+'</td>' +
		'<td align="left">'+stranka.substr(0,stranka.indexOf(";"))+'</td>' +
	'</tr>' +
	'<tr>' +
		'<td align="left">'+MooTools.lang.get('msg','dispatcher.naslov')+'</td>' +
		'<td align="left">'+$('i_naslov').value+'</td>' +
	'</tr>' +
	'<tr>' +
		'<td align="left">'+MooTools.lang.get('msg','dispatcher.kraj')+'</td>' +
		'<td align="left">'+$('i_kraj').value+'</td>' +
	'</tr>' +
	'<tr>' +
		'<td align="left">'+MooTools.lang.get('msg','dispatcher.kontakt')+'</td>' +
		'<td align="left">'+$('i_kontakt').value+'</td>' +
	'</tr>' +
	'<tr>' +
		'<td align="left">'+MooTools.lang.get('msg','dispatcher.telefon')+'</td>' +
		'<td align="left">'+$('i_telefon').value+'</td>' +
	'</tr>' +
	'<tr>' +
		'<td align="left">'+MooTools.lang.get('msg','dispatcher.kupec')+'</td>' +
		'<td align="left">'+$('i_kupec').value+'</td>' +
	'</tr>' +
	'<tr>' +
		'<td align="left">'+MooTools.lang.get('msg','dispatcher.potnik')+'</td>' +
		'<td align="left">'+$('i_potnik').value+'</td>' +
	'</tr>' +
	'<tr>' +
		'<td align="left">'+MooTools.lang.get('msg','dispatcher.datum')+'</td>' +
		'<td align="left">'+$('i_datum').value+'</td>' +
	'</tr>' +
	'<tr>' +
		'<td align="left">'+MooTools.lang.get('msg','dispatcher.material')+'</td>' +
		'<td align="left">'+material+'</td>' +
	'</tr>' +
	'<tr>' +
		'<td align="left">'+MooTools.lang.get('msg','dispatcher.kolicina')+'</td>' +
		'<td align="left">'+$('i_kolicina').value+'</td>' +
	'</tr>' +
	'<tr>' +
		'<td align="left">'+MooTools.lang.get('msg','dispatcher.osnovno')+'</td>' +
		'<td align="left">'+$('i_osnovno').value+'</td>' +
	'</tr>' +
	'<tr>' +
		'<td align="left">'+MooTools.lang.get('msg','dispatcher.opomba')+'</td>' +
		'<td align="left">'+$('i_opomba').value+'</td>' +
	'</tr>' +
	'<tr>' +
		'<td align="left">'+MooTools.lang.get('msg','dispatcher.narocil')+'</td>' +
		'<td align="left">'+narocil+'</td>' +
	'</tr>';
	print_html += '</table>';
	
	return print_html;
}

function tiskaj() {
	$('form_title').value=MooTools.lang.get('msg','welcome.title');
	$('form_html').value=getHtmlForm();
	$('listForms').action = Hydra.pageContext+"/pdf";
    $('listForms').submit();	
}

function xml() {
	
	var stranka = "";
	if ($('selectStranka').selectedIndex != 0) 
		stranka = $('selectStranka').options[$('selectStranka').selectedIndex].text;
	var material = "";
	if ($('selectMaterial').selectedIndex != 0) 
		material = $('selectMaterial').options[$('selectMaterial').selectedIndex].text;
	var narocil = "";
	if ($('selectNarocil').selectedIndex != 0) 
		narocil = $('selectNarocil').options[$('selectNarocil').selectedIndex].text;
	
	var xml_vsebina = '<?xml version="1.0" encoding="UTF-8"?>\n' +
	'<narocilo>\n' +
	'	<stranka>\n' +
	'		<sif_str>'+$('selectStranka').value+'</sif_str>\n' +
	'		<naziv>'+stranka.substr(0,stranka.indexOf(";"))+'</naziv>\n' +
	'		<naslov>'+$('i_naslov').value+'</naslov>\n' +
	'		<kraj>'+$('i_kraj').value+'</kraj>\n' +
	'		<kontakt>'+$('i_kontakt').value+'</kontakt>\n' +
	'		<telefon>'+$('i_telefon').value+'</telefon>\n' +
	'		<kupec>'+$('i_kupec').value+'</kupec>\n' +
	'		<potnik>'+$('i_potnik').value+'</potnik>\n' +
	'	</stranka>\n' +
	'	<datum>'+$('i_datum').value+'</datum>\n' +
	'	<material>\n'+
	'		<koda>'+$('selectMaterial').value+'</koda>\n' +
	'		<naziv>'+material+'</naziv>\n' +
	'	</material>\n' +
	'	<kolicina>'+$('i_kolicina').value+'</kolicina>\n' +
	'	<osnovno>'+$('i_osnovno').value+'</osnovno>\n' +
	'	<opomba>'+$('i_opomba').value+'</opomba>\n' +
	'	<narocil>\n'+
	'		<sif_upor>'+$('selectNarocil').value+'</sif_upor>\n' +
	'		<ime_in_priimek>'+narocil+'</ime_in_priimek>\n' +
	'	</narocil>\n' +
	'</narocilo>';
    
	$('form_html').value=xml_vsebina;
	$('listForms').action = Hydra.pageContext+"/xml";
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
			$('i_naslov').value = subjectOption[ii]['naslov'];
			$('i_kraj').value = subjectOption[ii]['posta']+" "+subjectOption[ii]['kraj'];
			$('i_kontakt').value = subjectOption[ii]['kont_os'];
			$('i_telefon').value = subjectOption[ii]['telefon'];
			$('i_kupec').value = subjectOption[ii]['kupec'];
			$('i_osnovno').value = subjectOption[ii]['osnovna'];
			$('i_opomba').value = subjectOption[ii]['opomba'];
			$('i_potnik').value = subjectOption[ii]['potnik'];
		}
	}
}

function addMaterials() {
	var materialsOptionData = '<option value=-1 selected>'+MooTools.lang.get('msg','dispatcher.izberi_material')+'</option>';
	for (var ii=0;ii<materialsOption.length;ii++) {
		materialsOptionData += '<option value='+materialsOption[ii]['koda']+'>'+materialsOption[ii]['koda']+" "+materialsOption[ii]['material']+'</option>';
	}
	$('selectMaterial').set('html', materialsOptionData);
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
           dataIndex: 'st_dob',
           dataType:'string',
           width:60
        },
        {
           dataIndex: 'datum',
           dataType:'string',
           width:100
        },
        {
           dataIndex: 'stranka',
           dataType:'string',
           width:250
        },
        {
            dataIndex: 'kupec',
            dataType:'string',
            width:200
         },
         {
            dataIndex: 'potnik',
            dataType:'string',
            width:150
        },
        {
           dataIndex: 'material',
           dataType:'string',
           width:200
        },
        {
           dataIndex: 'kolicina',
           dataType:'string',
           width:60
        },
        {
           dataIndex: 'opomba',
           dataType:'string',
           width:300
        },
        {
           dataIndex: 'narocil',
           dataType:'string',
           width:150
        }];	


window.addEvent("domready", function(){
                
    datagrid = new omniGrid('myTableGrid', {
        columnModel: cmu,
        url:"orders_table",
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
        sortOn: 'datum desc, st_dob',
		sortBy: 'ASC',
		sortOrder: 0,
		height: 400
    });
    
    //datagrid.addEvent('click', onGridSelect);
    
    if (window.File && window.FileReader && window.FileList && window.Blob) {
    	//Great success! All the File APIs are supported.
    } else {
    	  alert('The File APIs are not fully supported in this browser.');
    	  fileAPIEnabled = false;
    }

    initComponents(getLanguage());
    
    if (fileAPIEnabled)
    	document.getElementById('files').addEventListener('change', handleFileSelect, false);
 });



function handleFileSelect(evt) {
	var files = evt.target.files; // FileList object

	var f = files[0];
    if (!f.type.match('xml.*')) {
   	  alert(MooTools.lang.get('msg','error.file_format'));
   	  return;
    }

    
    var reader = new FileReader();
    // Closure to capture the file information.
    reader.onload = (function(theFile) {
      return function(e) {
    	$('ffile').value = theFile.name;
    	var xml = e.target.result;
        // Render xml
      	var parser=new DOMParser();
        var xmlDoc=parser.parseFromString(xml,"text/xml");
        
        $('selectStranka').value = xmlDoc.getElementsByTagName("sif_str")[0].childNodes[0].nodeValue; 	
    	if ($('selectStranka').value == -1) {
    		alert(MooTools.lang.get('msg','dispatcher.napacna_stranka'));
    		return;
    	}
        changeSubject();
    	$('i_kontakt').value = xmlDoc.getElementsByTagName("kontakt")[0].childNodes[0].nodeValue;
    	$('i_telefon').value = xmlDoc.getElementsByTagName("telefon")[0].childNodes[0].nodeValue;
        $('selectMaterial').value = xmlDoc.getElementsByTagName("koda")[0].childNodes[0].nodeValue; 	
        $('selectNarocil').value = xmlDoc.getElementsByTagName("sif_upor")[0].childNodes[0].nodeValue; 	
    	$('i_datum').value = xmlDoc.getElementsByTagName("datum")[0].childNodes[0].nodeValue;
    	$('i_kolicina').value = xmlDoc.getElementsByTagName("kolicina")[0].childNodes[0].nodeValue;
    	$('i_potnik').value = xmlDoc.getElementsByTagName("potnik")[0].childNodes[0].nodeValue;
    	$('i_opomba').value = xmlDoc.getElementsByTagName("opomba")[0].childNodes[0].nodeValue;
        
        
      };
    })(f);

    var xml = reader.readAsText(f);
  }

  