if (!window.console || !console.firebug)
{
	var names = ["log", "debug", "info", "warn", "error", "assert", "dir", "dirxml",
    "group", "groupEnd", "time", "timeEnd", "count", "trace", "profile", "profileEnd"];
    window.console = {};
    for (var i = 0; i < names.length; ++i)
    window.console[names[i]] = function() {}
}
var glbIdPref = "login";
function login()
{
    var username = document.getElementById(glbIdPref + "-username").value;
    var password = document.getElementById(glbIdPref + "-password").value;
    loginReq(CHALLENGE,username,password);
    //return false;
}

function avtoLogin()
{
    loginReq();
    return false;
}

function getHTTPObject() {
	var xmlhttp = false;
	if (typeof XMLHttpRequest != 'undefined') {
		try {
			xmlhttp = new XMLHttpRequest();
		} catch (e) {
			xmlhttp = false;
		}
	} else {
        /*@cc_on
        @if (@_jscript_version >= 5)
            try {
                xmlhttp = new ActiveXObject("Msxml2.XMLHTTP");
            } catch (e) {
                try {
                    xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
                } catch (E) {
                    xmlhttp = false;
                }
            }
        @end @*/
    }
	return xmlhttp;
}
function fixUrl(url) {
	return url.replace(/\+/g,'.');
}

var CHALLENGE = 0;
var AUTH = 1;
var LOGOUT = 2;
var glbNc=0;
var glbDigest=null;
function loginReq(type,username,password,ar) {
	var http = getHTTPObject();
	var url;
	if (AUTH==type) { //* auth
		calculateResponse(glbDigest,username,password);
		url = glbAppContext+"/authenticate?Authorization="+fixUrl("username:"+username+",realm:"+ar.realm+",nonce:"+ar.nonce+",nc:"+ar.nc+",cnonce:"+ar.cnonce+",response:"+ar.response+",algorithm:"+ar.algorithm+",opaque:"+ar.opaque);
		glbNc++;
		var nc = ("00000000"+glbNc);
		nc = nc.substring(nc.length-8);
		glbDigest.nc=nc;
		var rnd = ""+Math.random();
		glbDigest.cnonce=rnd.substring(rnd.indexOf(".")+1);
	} else if (CHALLENGE==type) {
		//* challenge
		url = glbAppContext+"/authenticate?rnd="+Math.random();
	} else if (LOGOUT==type) {
		//* logout
		//alert("lo");
		url = glbAppContext+"/authenticate?Logout=true&rnd="+Math.random();
	}
    http.open("get", url, false);
    http.send("");
	if (http.status == 200) {
	//console.debug(http.responseText);
		if (CHALLENGE==type) {
			if (glbDigest==null) {
				var tokens = http.responseText.split(",");
				var digest=new Object();
				for (var i=0;i<tokens.length;i++)
	            {
	                  var tok = tokens[i].split(":");
	                  if (tok!=null)
	                  {
	                      if ("algorithm"==tok[0]) {
	                          digest.algorithm=tok[1];
	                      } else if ("realm"==tok[0]) {
	                          digest.realm=tok[1];
	                      } else if ("nonce"==tok[0]) {
	                          digest.nonce=tok[1];
	                      } else if ("opaque"==tok[0]) {
	                          digest.opaque=tok[1];
	                      }
	                  }
	            }    
	            digest.nc='00000001';
	            var rnd = ""+Math.random();
	            digest.cnonce=rnd.substring(rnd.indexOf(".")+1);
	            //console.debug(digest);
	            //console.debug("NC:"+digest.nc);
	            //console.debug("cnonce:"+digest.cnonce);
	            //console.debug("nonce:"+digest.nonce);
	            
	            calculateResponse(digest,username,password);
	            glbNc=1;
	            glbDigest=digest;
	            // kličem avtorizacijo
            }
            loginReq(AUTH,username,password,glbDigest);
            return;
		}
		if ( http.responseText == "Error" ) {
			alert("Nepravilna prijava ali nimate pravice za uporabo portala");
			glbLoggedIn= false;
		} else if ( http.responseText == "Logged-out" ) {
			//alert("odlogiran");
			CHALLENGE = 0;
			AUTH = 1;
			LOGOUT = 2;
			glbNc=0;
			glbDigest=null;
			glbLoggedIn= false;
		} else {
			glbLoggedIn= http.responseText;
		}
    } else {
    	glbLoggedIn= false;
    }
    //createForm();
    //alert("refresh");
    window.location=glbAppRedirect;
    return false;
}
/*function createForm()
{
	if (!glbLoggedIn) {
	    document.getElementById("login-forma").style.display="block";
	    document.getElementById("logout-forma").style.display="none";
	} else {
		document.getElementById("login-forma").style.display="none";
		document.getElementById("logged-user").innerHTML=glbLoggedIn+" ";
		document.getElementById("logout-forma").style.display="block";
	}
}*/
function logout()
{
	loginReq(LOGOUT);
}
function calculateResponse(digest,username,password) {
	var hasher;
    if (digest.algorithm=="MD5") {
    	hasher = hex_md5;
    } else if (digest.algorithm=="SHA-1") {
    	hasher = hex_sha1;
    }
    // calc A1 digest
    var s = username+":"+digest.realm+":"+password;
    //console.debug("s:"+s);
    var a1 = hasher(s);
    //console.debug("A1:"+a1);
    var a2 = hasher("GET");
    s = a1+":"+digest.nonce+":"+digest.nc+":"+digest.cnonce+":"+digest.opaque+":"+a2;
    var a3 = hasher(s);
    //console.debug("A3:"+a3);
    digest.response=a3;
}