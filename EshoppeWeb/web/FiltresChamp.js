// FiltresChamp.js
// Fait par : Nicolas Chourot 
//      Adapté par : Simon Bouchard

function GetEventTarget(event) {
    return event.target;
}
// Retourne vrai si le caractÃ¨re "c" est alphanumÃ©rique
function CharIsAlpha(c) {
    c = c.toLowerCase();
    var alphas = "abcdefghijklmnopqrstuvwxyzÃ Ã¢Ã¤Ã§Ã©Ã¨ÃªÃ«Ã¬Ã®Ã¯Ã²Ã´Ã¶Ã¹Ã»Ã¼Ã¿Ã±-";
    var position = alphas.indexOf(c,0);
    return ( position != -1);
}
// Filter les caractÃ¨res d'un textbox afin d'accepter uniquement les alphanumÃ©riques
// utilisez lâ€™Ã©vÃ©nement onkeyup
function ConstrainToAlpha(event) {
    var textBox = event.target; ///GetEventTarget(event);
    var car = "";
    var code = 0;
    var valide = "";
    var constrain = false;
    for (var c = 0; c < textBox.value.length; c++) {
        car = textBox.value.substr(c,1);
        if (!CharIsAlpha(car)) {
            //if (!constrain)
            // alert("Lettres de 'a' a 'z' seulement!");
            constrain = true;
        }
        else {
            valide = valide + car;
        }
    }
    if (constrain)
        textBox.value = valide;
    return true;
}

// Retourne vrai si le caractÃ¨re "c" est un chiffre
function CharIsDigit(c) {
    var digits = "0123456789";
    return (digits.indexOf(c,0) != -1);
}
// Filter les caractÃ¨res d'un textbox afin d'accepter uniquement les chiffres
// utilisez lâ€™Ã©vÃ©nement onkeyup
function ConstrainToDigit(event) {
    var textBox = GetEventTarget(event);
    var car = "";
    var code = 0;
    var valide = "";
    var constrain = false;
    for (var c = 0; c < textBox.value.length; c++) {
        car = textBox.value.substr(c,1);
        if (!CharIsDigit(car)) {
            //if (!constrain)
            // alert("Chiffre de '0' a '9' seulement!");
            constrain = true;
        }
        else {
            valide = valide + car;
        }
    }
    if (constrain)
        textBox.value = valide;
}

// Retourne vrai si le code ascii "key" correspond Ã  un chiffre
function KeyIsDigit(key) {
    return ((48 <= key) && (key <= 57));
}

// Retourne vrai si le code ascii "key" correspond Ã  une lettre
function KeyIsLetter(key) {
    return ((65 <= key) && (key <= 90));
}
// Retourne vrai si le code ascii "key" correspond Ã  un control valide
function KeyIsLegalControl(key) {
    return ((key==8) || /*back space*/
    (key==9) || /*tab */
    (key==13) || /*return */
    (key==16) || /*delete */
    (key==27)); /*escape */
}
// Filtrer les caractÃ¨res d'un textbox en utilisant un masque mÃ©morisÃ© dans
// son attribut "alt",
// exemple de masque pour un code postal :<input type= "text" alt="C#C #C#" />
// utilisez lâ€™Ã©vÃ©nement onkeydown
function ConstrainToFilter(event) {
    var key = (window.event ? window.event.keyCode : event.keyCode || event.which);
    var textBox = event.target;
    var masque = textBox.getAttribute("alt");
    mp = textBox.value.length;
    charTyped = String.fromCharCode(key) ;
    if (KeyIsLegalControl(key)) {
        if ((mp > 0) && (key == 8)) {// backspace 
            mp --;
            if ((masque[mp] != "C") && (masque[mp] != "#"))
                mp --;
            textBox.value = textBox.value.substr(0,mp);
            return false;
        }
        else {
            textBox.value = textBox.value.toUpperCase();	
            return true;
        }
    }
    while ((mp < masque.length) && (masque[mp] != "C") && (masque[mp] != "#")) {
        textBox.value += masque[mp];
        mp ++;
    }
    current_pos = mp;
    if (current_pos < masque.length) {
        if ((masque[current_pos] == "C") && (!KeyIsLetter(key)))
            return false;
        if ((masque[current_pos] == "#") && (!KeyIsDigit(key)))
            return false;
    }
    else
        return false;
    textBox.value = textBox.value.toUpperCase();
    return true;
}

function MakeFilteredTextBoxControl(id, caption, mask) {
    var panel = document.createElement("div");
    var label = document.createElement("label");
    label.htmlFor = id;
    label.innerHTML = caption;
    var textbox = document.createElement("input");
    textbox.type = "text";
    textbox.id = id;
    textbox.alt = mask;
    textbox.onkeydown = ConstrainToFilter;
    //textbox.setAttribute("onkeydown", "return ConstrainToFilter(event);");
    panel.appendChild(label);
    panel.appendChild(textbox);
    return panel;
}



