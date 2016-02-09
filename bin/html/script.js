var chars = "abcdefghijklmnopqrstuvwxyz1234567890_=+-()*&^%$#@<>\\:;[]{}\"|'/!?,.";
var shift = false;
var index = 0;
var animationSpeed = 200;

function setChar(char, slot) {
	$("#letter" + slot + " > span").text((shift) ? char.toUpperCase() : char);
}

function wrap(num) {
	result = num % chars.length;
	if(result < 0) {
		return chars.length + result;
	}
	return result;
}

function init() {
	setChar(chars[wrap(index - 5)], 0);
	setChar(chars[wrap(index - 4)], 1);
	setChar(chars[wrap(index - 3)], 2);
	setChar(chars[wrap(index - 2)], 3);
	setChar(chars[wrap(index - 1)], 4);
	setChar(chars[wrap(index)], 5);
	setChar(chars[wrap(index + 1)], 6);
	setChar(chars[wrap(index + 2)], 7);
	setChar(chars[wrap(index + 3)], 8);
	setChar(chars[wrap(index + 4)], 9);
	setChar(chars[wrap(index + 5)], 10);
}

function translateLetters(direction, callback) {
	var done = 0;
	if(direction === "left") {
		$(".letter > span").animate({left: "-=70"}, animationSpeed, "swing", function() {done++; if(done == 11) {callback()}});
		// $("#letter6").animate({color: jQuery.color("#534B52")}, animationSpeed);	
	} else if(direction === "right") {
		$(".letter > span").animate({left: "+=70"}, animationSpeed, "swing", function() {done++; if(done == 11) {callback()}});
		// $("#letter4").animate({color: jQuery.color("#534B52")}, animationSpeed);	
	}
	// $("#letter5").animate({color: jQuery.color("#E0DDCF")}, animationSpeed);

}

function finishAnimations() {
	$(".letter > span").finish();
	$(".letter").finish();
}


function setIndex(value) {
	index = value;
	init();
}

function moveLetters(direction, callback) {
	if(direction === "left") {
		$(".letter > span").css({left: "-=70"}, animationSpeed, callback);
	} else if(direction === "right") {
		$(".letter > span").css({left: "+=70"}, animationSpeed, callback);
	}
}

function moveLeft(numTimes) {
	finishAnimations();
	translateLetters("right", function() {
		setIndex(index - 1);
		moveLetters("left");
	});
}

function moveRight(numTimes) {
	finishAnimations();
	translateLetters("left", function() {
		setIndex(index + 1);
		moveLetters("right");
	});
}


function getSelectedLetter() {
	return $("#letter5 > span").text();
}

function setShift(value) {
	shift = value;
	init();
}

function getDimensions() {
	width = parseInt($("#mainContainer").css("width").substring(0, 3));
	height = parseInt($("#mainContainer").css("height").substring(0, 3));

	return {height: height, width: width};
}

//init();