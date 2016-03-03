function canvi(sis) {
    document.getElementById('csslink').href = sis;
}


/*SOURCE from  http://www.quirksmode.org/js/cross_dhtml.html */
var DHTML = (document.getElementById || document.all || document.layers);

function getObj(name)
{
    if (document.getElementById)
    {
        this.obj = document.getElementById(name);
        if (this.obj != null)
            this.style = document.getElementById(name).style;
    }
    else if (document.all)
    {
        this.obj = document.all[name];
        if (this.obj != null)
            this.style = document.all[name].style;
    }
    else if (document.layers)
    {
        this.obj = document.layers[name];
        if (this.obj != null)
            this.style = document.layers[name];
    }
}

function invi(name, flag)
{
    if (!DHTML)
        return;
    var x = new getObj(name);
    x.style.visibility = flag;//(flag) ? 'hidden' : 'visible';
}

/*Own code.*/

var category;
var lastElement;
var lastType;
var pesType;
var lastnode;
var capaCat;
var lastObservation;
var lastContent;
var lastArea;
var lastLevel;

function init() {
    //	swapDisplay('formObservacions');
    //	swapDisplay('formReferencia');
    swapDisplay('formlevel');
    //	swapDisplay('formobjective');
    swapDisplay('formcontent');
    //	swapDisplay('formThesaure');
    /*
     if (document.NodeForm.operacio.value == "thesnav"){	
     swapDisplay('formThesaure');
     document.NodeForm.operacio.value = document.NodeForm.entornOperacio.value;
     if (document.NodeForm.operacio.value == "addnode")
     insertElem();
     if (document.NodeForm.operacio.value =="addnodefill")
     insertElemFill();
     }
     */
    if (document.NodeForm.operacio.value == "delnode") {
        document.NodeForm.operacio.value = "";
        document.NodeForm.idLevel.value = "";
    }

    lastLevel = new getObj('level' + document.NodeForm.idLevel.value);
    lastArea = new getObj('area' + document.NodeForm.idArea.value);
    lastContent = new getObj('content' + document.NodeForm.idContent.value);
    //	lastObjective = new getObj('objective'+document.NodeForm.idObjective.value);

    if (lastLevel.style == null) {
        disableOperations('level', "disabled", "disabled", "enabled", "disabled");
        var elem = new getObj("levelFill");
        elem.obj.className = "disabled";
    }
    if (lastArea.style == null) {
        document.NodeForm.idArea.value = 0;
        disableOperations('area', "disabled", "disabled", "enabled", "disabled");
    }
    if (lastContent.style == null) {
        disableOperations('content', "disabled", "disabled", "disabled", "disabled");
        var elem = new getObj("contentFill");
        elem.obj.className = "disabled";
    }
    /*	if (lastObjective.style == null){
     disableOperations('objective',"disabled","disabled","disabled","disabled");				
     }
     */
    if (document.NodeForm.operacio.value == "selec")
        document.NodeForm.operacio.value = "setnode";
    mostraForm(document.NodeForm.nodeType.value);
}


function disableOperations(type, amunt, avall, add, del) {
    var elem = new getObj(type + "Amunt");
    elem.obj.className = amunt;
    var elem = new getObj(type + "Avall");
    elem.obj.className = avall;
    var elem = new getObj(type + "Add");
    elem.obj.className = add;
    var elem = new getObj(type + "Del");
    elem.obj.className = del;

}


/*Fa visibles les capes de contingut i de objectius donades.*/
function setCapes(cont, obj) {
    lastType = document.NodeForm.nodeType.value;
    capaCat = cont;
    if (lastType != 'area')
        swapDisplay('form' + lastType);
    if (cont != "CA" && cont != "CC" && cont != "CP")
        cont = "CC";
    elem = document.getElementById('cont' + cont);
    if (elem == null) {
        invi("contCA", 'visible'); //'hidden');
        invi("contCC", 'visible'); //'hidden');
        invi("contCP", 'visible'); //'hidden');
    }
    else {
        elem.style.visibility = 'visible';
    }
    /*	
     if (obj != "OP" && obj != "OT") cont = "OP";
     elem = document.getElementById('obj'+obj);
     if (elem == null){
     invi("objOT",'hidden');
     invi("objOP",'hidden');
     }
     else{elem.style.visibility = 'visible';}
     */
    capaCat = 'null';
    //	if (zone == "objective") capaCat = obj;
    if (zone == "category")
        capaCat = cont;

}
/*
 function pestanya(vis, hid1, hid2){
 pesType = vis;
 invi(vis,'visible');
 invi(hid1,'hidden');
 if (hid2 != null) invi(hid2,'hidden');
 }
 */
function selec(type, category, id, obj) {
    if (document.NodeForm.idNode.value != id || lastType != type) {
        lastType = type;
        if (type == 'level') {
            if (id != document.NodeForm.idLevel.value) {
                document.NodeForm.idLevel.value = id;
                document.NodeForm.idObjective.value = 0;
                document.NodeForm.idContent.value = 0;
            }
        }
        if (type == 'area') {
            if (id != document.NodeForm.idArea.value) {
                document.NodeForm.idArea.value = id;
                document.NodeForm.idObjective.value = 0;
                document.NodeForm.idContent.value = 0;
            }
        }
        if (type == 'objective') {//objectiu = id; 
            document.NodeForm.idObjective.value = id;
        }
        if (type == 'content') { //contingut = id; 
            document.NodeForm.idContent.value = id;
        }
        document.NodeForm.nodeType.value = type;
        document.NodeForm.idNode.value = id;
        setCategory(category);
        document.NodeForm.operacio.value = "selec";
        document.NodeForm.submit();
    }
}


function insert(elem, zone, nonreset) {
    if (elem.className == "enabled" || elem.className == "") {
        if (document.NodeForm.idNode.value <= 0) {
            lastType = "";
        } else {
            test = new getObj(zone + document.NodeForm.idNode.value);
            if (test == null || test.obj == null || test.style == null) {
                document.NodeForm.idNode.value = 0;
                lastType = "";
            }
        }
        if (lastType == zone) {
            id = document.NodeForm.idNode.value;
        } else {
            if (zone == "level")
                id = document.NodeForm.idLevel.value;
            if (zone == "area")
                id = document.NodeForm.idArea.value;
            if (zone == "content")
                id = document.NodeForm.idContent.value;
            if (zone == "objective")
                id = document.NodeForm.idObjective.value;
            lastElement = new getObj(document.NodeForm.nodeType.value + document.NodeForm.idNode.value);
            if (lastElement.obj != null)
                lastElement.obj.className = "selected2";
            lastElement = null;
            test = new getObj(zone + id);
            if (test == null || test.obj == null || test.style == null) {
                document.NodeForm.idNode.value = 0;
                lastType = "";
            }
            if (id <= 0) {
                lastElement = new getObj('div' + zone);
                i = 0;
                trobat = false;
                while (lastElement.obj.childNodes[i] != null && !trobat) {
                    if (lastElement.obj.childNodes[i].className == "content") {
                        trobat = true;
                    } else
                        i++;
                }
                lastElement = lastElement.obj.childNodes[i];
                i = 0;
                trobat = false;
                while (lastElement.childNodes[i] != null && !trobat) {
                    if (lastElement.childNodes[i].nodeName == "OL") {
                        trobat = true;
                    } else
                        i++;
                }
                lastElement = lastElement.childNodes[i].lastChild;
            }
        }
        newLI = document.createElement("li");
        newSPAN = document.createElement("a");
        newSPAN.setAttribute("id", zone + "0");
        newSPAN.setAttribute("class", "selected");
        newSPAN.setAttribute("href", "#");
        newSPAN.setAttribute("onclick", "selec('" + zone + "','" + document.NodeForm.category[0].value + "' ,0,this)");
        newSPAN.setAttribute("align", "center");
        newText = document.createTextNode("nou node " + zone);
        newSPAN.appendChild(newText);
        newLI.appendChild(newSPAN);


        if (lastElement != null && id <= 0 && lastType != zone) {
            lastType == zone
            lastElement.parentNode.appendChild(newLI);
            lastnode = newLI;
            mostraForm(zone);
            document.NodeForm.operacio.value = "addnode";
            document.NodeForm.idNode.value = 0;
            if (nonreset != true) {
                cleanForm();
                document.NodeForm.nodeType.value = zone;
                document.NodeForm.term.value = "nou node";
            }
        } else {
            lastElement = new getObj(zone + id);
            lastElement = lastElement.obj;
            if (lastElement != null) {
                lastElement.id.value = "";
                lastElement.className = "selected2";
                lastElement.parentNode.parentNode.insertBefore(newLI, lastElement.parentNode);
                lastnode = newLI;
                mostraForm(zone);
                document.NodeForm.operacio.value = "addnode";
                document.NodeForm.idNode.value = 0;
                if (nonreset != true) {
                    cleanForm();
                    document.NodeForm.nodeType.value = zone;
                    document.NodeForm.term.value = "nou node";
                }
            }
        }
    } else {
        alert("Clica primer el node sobre el que voldras inserir un nou node.");
    }

}

function insertFill(elem, zone, noreset) {
    if (elem.className == "enabled" || elem.className == "") {
        if (lastType == zone) {
            newLI = document.createElement("li");
            newSPAN = document.createElement("a");
            newSPAN.setAttribute("href", "#");
            newSPAN.setAttribute("id", "selected");
            newSPAN.setAttribute("class", "selected");
            newSPAN.setAttribute("onclick", "selec('" + zone + "','" + document.NodeForm.category[0].value + "' ,0,this)");
            newSPAN.setAttribute("align", "center");
            newText = document.createTextNode("nou node " + zone);
            newSPAN.appendChild(newText);
            newLI.appendChild(newSPAN);

            lastElement = new getObj(zone + document.NodeForm.idNode.value);
            lastElement = lastElement.obj;
            if (lastElement != null) {
                lastElement.id.value = "";
                lastElement.style.color = "black";
                lastElement.style.fontWeight = "normal";
                lastElement.style.background = "";
                lastElement.className = "";
                lastElement.parentNode.lastChild.appendChild(newLI);
                lastnode = newLI;
                document.NodeForm.operacio.value = "addnodefill";
                document.NodeForm.idNode.value = 0;
                if (noreset != true) {
                    cleanForm();
                    document.NodeForm.term.value = "nou node";
                    if (category == "cicle")
                        category = "curs";
                    if (category == "etapa")
                        category = "cicle";
                    setCategory(category);
                }
            }
        } else {
            alert("Clica primer el node sobre el que voldr? inserir un nou node.");
        }
    }
}

function amunt(elem, zone) {
    var mvElem;
    if (elem.className == "enabled" || elem.className == "") {
        if (lastType != zone) {
            if (zone == "level")
                id = document.NodeForm.idLevel.value;
            if (zone == "area")
                id = document.NodeForm.idArea.value;
            if (zone == "content")
                id = document.NodeForm.idContent.value;
            if (zone == "objective")
                id = document.NodeForm.idObjective.value;
        } else {
            id = document.NodeForm.idNode.value;
        }
        mvElem = new getObj(zone + id);
        mvElem = mvElem.obj;
        if (mvElem.parentNode.previousSibling != null) {
            mvElem.parentNode.parentNode.insertBefore(mvElem.parentNode, mvElem.parentNode.previousSibling);
            document.NodeForm.operacio.value = "swapup";
            document.NodeForm.idNode.value = id;
            document.NodeForm.nodeType.value = zone;
            document.NodeForm.submit();
        }
    }
}

function avall(elem, zone) {
    var mvElem;
    if (elem.className == "enabled" || elem.className == "") {
        if (lastType != zone) {
            if (zone == "level")
                id = document.NodeForm.idLevel.value;
            if (zone == "area")
                id = document.NodeForm.idArea.value;
            if (zone == "content")
                id = document.NodeForm.idContent.value;
            if (zone == "objective")
                id = document.NodeForm.idObjective.value;
            mvElem = new getObj(zone + id);
            mvElem = mvElem.obj;
        } else {
            id = document.NodeForm.idNode.value;
        }
        mvElem = new getObj(zone + id);
        mvElem = mvElem.obj;
        if (mvElem.parentNode.nextSibling != null) {//lastElement.parentNode.parentNode.lastChild){
            mvElem.parentNode.parentNode.insertBefore(mvElem.parentNode.nextSibling, mvElem.parentNode);
            document.NodeForm.operacio.value = "swapdown";
            document.NodeForm.idNode.value = id;
            document.NodeForm.nodeType.value = zone;
            document.NodeForm.submit();
        }
    }
}

function swapDisplay(name) {
    var e = new getObj(name);
    if (e.style != null) {
        if (e.style.display == 'none' || e.style.display == '') {
            e.style.display = 'block';
        } else {
            e.style.display = 'none';
        }
    }
}

function chtype(elem) {
    term = document.NodeForm.term.value;
    desc = document.NodeForm.description.value;
    mostraForm(elem.value);
    document.NodeForm.term.value = term;
    document.NodeForm.description.value = desc;
    document.NodeForm.operacio.value = "addnode";
    cleanForm();
}


function mostraForm(form) {
    var e = new getObj('formlevel');
    e.style.display = 'none';
    //var e = new getObj('formobjective');
    //e.style.display = 'none';
    var e = new getObj('formcontent');
    e.style.display = 'none';
    if (form != "area") {
        var e = new getObj('form' + form);
        e.style.display = 'block';
    }
}

function cleanForm() {
    document.NodeForm.idNode.value = 0;
    document.NodeForm.term.value = "";
    document.NodeForm.termOc.value = "";
    document.NodeForm.termEs.value = "";
    document.NodeForm.termEn.value = "";
    document.NodeForm.description.value = "";
    document.NodeForm.category.value = "";
    document.NodeForm.note.value = "";
    document.NodeForm.references.value = "";

    lk = "#" + document.NodeForm.selecPath.value;
    while (lk.length > 1) {
        key = lk.substring(lk.indexOf("#") + 1, lk.indexOf("#", lk.indexOf("#") + 1));
        lk = lk.substring(lk.indexOf("#", lk.indexOf("#") + 1));

        delKey(key);
    }

}


function addNodeAlone(elem, type, category) {
    cleanForm();
    if (lastnode != null) {
        lastnode.firstChild.nodeValue = 'Node nou';//'<bean:message key="application.clicktonode"/>';
        lastnode.setAttribute("class", "");
        lastnode.setAttribute("id", "");
    }
    if (type != "level" && !(document.NodeForm.idLevel.value > 0)) {
        alert("Cal seleccionar un Nivell previament.");
    } else {
        zone = type;
        nact = new getObj("selected");
        if (nact != null && nact.style != null) {
            nact.style.fontWeight = "normal";
            nact.style.color = "black";
        }
        elem.setAttribute("class", "selected");
        elem.setAttribute("id", "selected");
        elem.setAttribute("onclick", "addNodeAlone(this,'" + type + "','" + category + "')");
        elem.setAttribute("align", "center");
        elem.firstChild.nodeValue = 'Nou node ' + type;//<bean:message key="application.newnode"/>
        document.NodeForm.idNode.value = 0;
        document.NodeForm.term.value = 'Nou node ' + type;//<bean:message key="application.newnode"/>
        document.NodeForm.description.value = "";
        document.NodeForm.category.value = category;
        setCategory(category);
        document.NodeForm.note.value = "";
        document.NodeForm.references.value = "";
        document.NodeForm.operacio.value = "addnode";
        document.NodeForm.nodeType.value = type;
        mostraForm(type);
        lastnode = elem;
    }
}

function setCategory(valor) {
    category = valor;
    var lelems = document.getElementsByName("category");
    var elf = new getObj("levelFill");
    if (valor == "etapa" || valor == "cicle") {
        elf.style.visibility = 'visible';
    } else {
        elf.style.visibility = 'hidden';
    }
    i = 0;
    while (lelems[i] != null) {
        if (lelems[i].type == "radio") {
            if (lelems[i].value == valor) {
                lelems[i].checked = true;
            }
        }
        i++;
    }
}

function delNode(elem, zone) {
    if (elem.className == "enabled" || elem.className == "") {
        if (lastType != zone) {
            document.NodeForm.nodeType.value = zone;
        }
        document.NodeForm.operacio.value = "delnode";
        if (confirm("?s a punt d'esborrar el node seleccionat del típus:'" + zone + "'. N'est? segur?")) {
            document.NodeForm.submit();
        }
    }
}

function old_navigateTo(idKey) {
    document.NodeForm.idKey.value = idKey;
    document.NodeForm.entornOperacio.value = document.NodeForm.operacio.value;
    document.NodeForm.operacio.value = "thesnav";
    document.NodeForm.submit();
}

function addKey(idKey) {
    document.NodeForm.idKey.value = idKey;
    pcm = "#" + document.NodeForm.selecPath.value;
    elemKey = new getObj("thesaurus" + idKey);
    text = elemKey.obj.text;
    if (pcm.indexOf("#" + idKey + "#") < 0) {
        document.NodeForm.selecPath.value += idKey + "#";
        key = new getObj('keys');
        aDel = document.createElement("a");
        aDel.setAttribute("href", "#");
        aDel.setAttribute("id", "delkey" + idKey);
        aDel.setAttribute("onclick", "delKey(" + idKey + ")");
        textDel = document.createTextNode("-");
        aDel.appendChild(textDel);

        aNav = document.createElement("a");
        aNav.setAttribute("href", "#");
        aNav.setAttribute("id", "key" + idKey);
        aNav.setAttribute("onclick", "navigateTo(" + idKey + ")");
        textNav = document.createTextNode(text);
        aNav.appendChild(textNav);
        key.obj.appendChild(aDel);
        key.obj.appendChild(aNav);
    }
}

function delKey(idKey) {
    pcm = "#" + document.NodeForm.selecPath.value;
    if (pcm.indexOf("#" + idKey + "#") >= 0) {
        //pcm = document.NodeForm.selecPath.value;// += idKey+"#";     
        aux = "#" + idKey + "#";
        pcd1 = pcm.substring(1, pcm.indexOf(aux) + 1);
        pcd1 += pcm.substring(pcm.indexOf(aux) + aux.length);
        document.NodeForm.selecPath.value = pcd1;
        key = new getObj("key" + idKey);
        key.obj.parentNode.removeChild(key.obj);
        key = new getObj("delkey" + idKey);
        key.obj.parentNode.removeChild(key.obj);
    }
}

function insertElem() {
    elem = new getObj(zone + "Add");
    if (document.NodeForm.idNode.value > 0)
        insert(elem.obj, zone, true);
    else {
        elem = new getObj(zone + "-1");
        if (zone == "content" || zone == "objective") {
            i = 0;
        }
        addNodeAlone(elem.obj, zone, capaCat);
    }
}

function insertElemFill() {
    elem = new getObj(zone + "Fill");
    document.NodeForm.idNode.value = document.NodeForm.idLevel.value;
    insertFill(elem.obj, zone, true);
}