var chars = "abcdefghijklmnopqrstuvwxyz1234567890_=+-()*&^%$#@<>/:;[]{}\|'!?,.";
var shift = false;

function setChar(char, slot) {
	$("#letter" + slot).text((shift) ? char.toUpperCase() : char);
}

function init() {
	setChar("!", 1);
	setChar("?", 2);
	setChar(",", 3);
	setChar(".", 4);
	setChar("a", 5);
	setChar("b", 6);
	setChar("c", 7);
	setChar("d", 8);
	setChar("e", 9);
}


init();