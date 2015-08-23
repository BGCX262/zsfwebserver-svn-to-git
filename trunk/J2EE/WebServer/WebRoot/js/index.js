var interval, x, y;
$(function() {
	interval = setInterval(move, 500);
	$("#main").mousemove(function(e) {
		x = Math.min(e.pageX - $("#dialog").width() / 2, $("#main").width() - $("#dialog").width());
		y = Math.min(e.pageY - $("#dialog").height() / 2, $("#main").height() - $("#dialog").height());
		x = Math.max(0, x);
		y = Math.max(0, y);
	});
});

var move = function() {
	$("#dialog").animate({
		"left" : x,
		"top" : y
	}, 450);
}