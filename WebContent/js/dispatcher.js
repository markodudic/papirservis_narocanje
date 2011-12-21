var subjectOption = '';
var vehicleOption = '';
var documents = '';
var stateOption = ['UA', 'AC', 'A', 'F'];
var stateOptionLabel = ['UA', 'AC', 'A', 'F'];
var print_html = '';
var refreshSubjects = false;
var refreshInterval = 30000;
var prvic = {};
prvic.prvic = true;
var niprvic = {};
niprvic.prvic = false;

function initComponents(lang) {
	MooTools.lang.setLanguage(lang);

	//getSubjects();
	
	new Request({method: 'get', url: 'subjects',
		'onSuccess':function(result){
			subjectOption = JSON.decode(result);
			addSubjects(subjectOption);
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
			documents = JSON.decode(result);
//			addToTable(true);
	}}).send();
}	

function search () {
	var sortorder = 0;
	if ($('order_1').checked) sortorder = 1;
	if ($('order_2').checked) sortorder = 2;
	var selText = $('searchText').value;
	var selVozilo = $('selectvozilo').value;
	var selStatus = $('selectstatus').value;
	var datumOd = $('datum_od').value;
	var datumDo = $('datum_do').value;
 
    datagrid.options.sortOrder = sortorder;
    datagrid.options.selText = escape(selText);
    datagrid.options.selVozilo = selVozilo;
    datagrid.options.selStatus = selStatus;
    datagrid.options.datumOd = datumOd;
    datagrid.options.datumDo = datumDo;
    
    datagrid.options.page = 1;
    datagrid.refresh();
}

function reset () {
	$('searchText').value = '';
	$('selectvozilo').value = -1;
	$('selectstatus').value = -1;
	$('datum_od').value = '';
	$('datum_do').value = '';
	
	datagrid.options.sortOrder = 1;
	datagrid.options.selText = '';
    datagrid.options.selVozilo = -1;
    datagrid.options.selStatus = -1;
    datagrid.options.datumOd = '';
    datagrid.options.datumDo = '';
    
    datagrid.options.page = 1;
    datagrid.refresh();
}


function potrdiVozilo(id, st){
	var podatkiJSON = {};
	podatkiJSON[st] = id;
	var j = {};
	j.confirmData = JSON.encode(podatkiJSON);		
	new Request({
		method: 'post', 
		url: 'confirm',
		data: j,
		'onSuccess':function(result){}
	}).send();
}

function confirm(){
	new Request({
		method: 'get', 
		url: 'confirmAll',
		'onSuccess':function(result){}
	}).send();
}


function tiskaj() {
	print_html = '<table>';
	addToTable(false);
	print_html += '</table>';
    
	$('form_html').value=print_html;
	$('listForms').action = Hydra.pageContext+"/pdf";
    $('listForms').submit();	
}

function refresh(id, evl) {
	var j = {};
	j.refreshData = id;
	j.evl = evl;
	
	new Request({
		method: 'post', 
		url: 'refresh',
		data: j,
		'onSuccess':function(result){
		}
	}).send();
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
			
		}
	}
}

function addVehicles() {
	var vehiclesOptionData = '<option value=-1 selected>'+MooTools.lang.get('msg','dispatcher.izberi_vozilo')+'</option>';
	for (var ii=0;ii<vehicleOption.length;ii++) {
		vehiclesOptionData += '<option value='+vehicleOption[ii]['sifra']+'>'+vehicleOption[ii]['naziv']+'</option>';
	}
	$('selectvozilo').set('html', vehiclesOptionData);
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
        url:"dispatcher-table",
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
