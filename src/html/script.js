var chars = "abcdefghijklmnopqrstuvwxyz1234567890_=+-()*&^%$#@<>/:;[]{}\|'!?,.";
var shift = false;
var index = 0;
var animationSpeed = 100;

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
		$(".letter > span").animate({marginLeft: "-=70"}, animationSpeed, "swing", function() {done++; if(done == 11) {callback()}});
	} else if(direction === "right") {
		$(".letter > span").animate({marginLeft: "+=70"}, animationSpeed, "swing", function() {done++; if(done == 11) {callback()}});
	}
}

function moveLetters(direction, callback) {
	if(direction === "left") {
		$(".letter > span").css({marginLeft: "-=70"}, animationSpeed, callback);
	} else if(direction === "right") {
		$(".letter > span").css({marginLeft: "+=70"}, animationSpeed, callback);
	}
}

function moveLeft() {
	translateLetters("right", function() {
		index--;
		init();
		moveLetters("left");
	});
}

function moveRight() {
	translateLetters("left", function() {
		index++;
		init();
		moveLetters("right");
	});
}

init();