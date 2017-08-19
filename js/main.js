//called in login.jsp, file_chooser.jsp, and sign_up.jsp
//parameters:
//servletName -- servlet that the ajax call will go to
//jspName -- window location to navigate to if there was no error
//paramArgs -- array of ids of all the parameters that need to be included in ajax request
//numArgs -- number of elements in paramArgs
//errorDivName -- id of the error div
//function errorCheck (servletName, jspName, paramArgs, numArgs, errorDivName){
//	
//	var xhttp = new XMLHttpRequest();
//	//gets the path
//	var path = "/"+window.location.pathname.split("/")[1];
//	//create url with first parameter from paramArgs
//	var url = path + servletName+"?"+paramArgs[0]+"="+document.getElementById(paramArgs[0]).value;
//	//append the rest of the elements in paramArgs
//	for (let i = 1; i<numArgs; i++){
//		url += "&"+paramArgs[i]+"="+document.getElementById(paramArgs[i]).value;
//	}
//	//send synchronous ajax call to servelt
//	xhttp.open("GET", url, false);
//	xhttp.send();
//	//if we got a response text, there must have been an error
//	if (xhttp.responseText.trim().length > 0) {
//		//set the repsonse text as the innerHTML of the error div
//		document.getElementById(errorDivName).innerHTML = xhttp.responseText;
//	}
//	else{
//		//otherwise navigate to the destination jsp
//		window.location.href = path + "/jsp/"+jspName;
//	}
//	
//	return false;
//	
//}
function errorCheck (servletname, jspname, param, numparam, errormessage){
    //The XMLHttpRequest object is used to exchange data with a server behind the scenes.
    //This means that it is possible to update parts of a web page, without reloading the whole page.
    var xhttp = new XMLHttpRequest();
    var port = window.location.port;
    var hostname = window.location.hostname;
    var protocal = window.location.protocol;
    //return the path of the current page
    var path = document.location.pathname;
    //url with first param
    var url = protocal+'//'+ hostname + ':' + port  + '/haopengs_csci201l_assignment5' + servletname + '?' +param[0] + '=' + document.getElementById(param[0]).value;
 
    //append the rest
    for(i=1;i<numparam;i++){
        url+='&'+param[i]+'='+document.getElementById(param[i]).value;
    }
   //s document.write(url);
    //send synchronous ajax call to servelt
    console.log(protocal+'//' + hostname + ':'+ path);
    xhttp.open("GET",url,false);
    xhttp.send();
    //if we got a response text, there must have been an error
    if(xhttp.responseText.trim().length > 0){
        //set response text as the innerHtml of the error div
        document.getElementById(errormessage).innerHTML = xhttp.responseText;
    }
    else {
        //otherwise navigate to the destination jsp
        window.location.href = '/haopengs_csci201l_assignment5/jsp/' + jspname;
    }
}