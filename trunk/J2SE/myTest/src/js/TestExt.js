/*
function putmsg(msg){
	msg = "Hello : " + msg;
	alert(msg);
	
}
*/

function f() {
	var doc = window.open("1.txt", "", "height=200,width=200,scrollbars=no");

	doc.document.writer("123321");
	doc.close();
}
