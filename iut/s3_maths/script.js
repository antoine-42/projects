//############## EVENT LISTENERS #############
function switchTable () {
    var subject = this.id.split('-')[0];
    var icon = document.getElementById(subject + "-switch");
    var table = document.getElementById(subject + "-table");
    var tableWrapper = document.getElementById(subject + "-table-wrapper");

    if(this.title != "Cacher"){
        var tableStyle = window.getComputedStyle(table);
        var height = tableStyle.getPropertyValue('height');
        var width = tableStyle.getPropertyValue('width');

        tableWrapper.style.height = height; //todo: get that from the table
        tableWrapper.style.width = width;
        tableWrapper.style.paddingBottom = "14px";

        this.title = "Cacher";
        icon.style.transform = "rotate(360deg)";
    }
    else {
        tableWrapper.style.height = "0";
        tableWrapper.style.width = "0";
        tableWrapper.style.paddingBottom = "0";

        this.title = "Étendre";
        icon.style.transform = "rotate(270deg)";
    }
}

var tableSwitches = document.getElementsByClassName("table-switch");
var tableSwitchLabels = document.getElementsByClassName("table-switch-label");
for(var i = 0; i < tableSwitches.length; i++){
    tableSwitchLabels[i].addEventListener('click', switchTable);
}


//############## INITIALISATION ##############
//votes, logins
var subjects = {"acda":"ACDA", "eng":"Anglais", "apl":"APL", "art":"Art", "asr":"Asr", "ec":"EC", "egod":"EGOD", "maths":"Maths", "sgbd":"SGBD", "sport":"Sport"};
var subjects_ext_to_in = {"ACDA":"acda", "ANG":"eng", "APL":"apl", "ART":"art", "ASR":"asr", "EC":"ec", "EGOD":"egod", "MAT":"maths", "SGBD":"sgbd", "SPORT":"sport"};

//Dictionaire qui assigne a chaque matiere un dictionaire. ces dictionaires assignent a chaque eleve leur resultat final dans une matiere.
var results = {};
for(var subject in subjects){
    //ajoute les matieres
    results[subject] = [];

    for(var student in logins){
        //ajoute les eleves
        results[subject].push([student, 0]);
    }
}


//################ TRAITEMENT ################
for (var student in votes){
    for(var subject_ext in votes[student]){
        //distribuer le poids entre les votes pour chaque matiere
        var weight = 1/ votes[student][subject_ext].length;

        for(var vote in votes[student][subject_ext]){
            //pas compter les votes pour soi-meme
            if (votes[student][subject_ext][vote] != student) {
                var position = -1
                for(var i = 0; i < results[subjects_ext_to_in[subject_ext]].length; i++){
                    if(results[subjects_ext_to_in[subject_ext]][i][0] == votes[student][subject_ext][vote]){
                        results[subjects_ext_to_in[subject_ext]][i][1] += weight;
                        break;
                    }
                }
            }
        }
    }
}


//################ AFFICHAGE #################
for(var subject in subjects){
    //ordonner les resultats dans l'ordre decroissant
    results[subject].sort(function(a,b) {
        return b[1]-a[1];
    });

    var subjectBox = document.getElementById(subject + "-table");

    for(var i = 0; i < results[subject].length; i++){
        //afficher les resultats
        if (results[subject][i][1] >= 0.05) {
            var currLine = document.createElement("tr");
            var cellName = document.createElement("td");
            var cellPoints = document.createElement("td");
            cellName.innerHTML = logins[results[subject][i][0]];
            cellPoints.innerHTML = Math.round(results[subject][i][1]* 10) / 10;
            currLine.appendChild(cellName);
            currLine.appendChild(cellPoints);
            subjectBox.appendChild(currLine);
        };
    }
}
