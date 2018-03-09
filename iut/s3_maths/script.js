//############## INITIALISATION ##############
//recois dans votes, logins
'use strict';

var subjects = {"acda":"ACDA", "eng":"Anglais", "apl":"APL", "art":"Art", "asr":"Asr", "ec":"EC", "egod":"EGOD", "maths":"Maths", "sgbd":"SGBD", "sport":"Sport"};
var subjects_ext_to_in = {"ACDA":"acda", "ANG":"eng", "APL":"apl", "ART":"art", "ASR":"asr", "EC":"ec", "EGOD":"egod", "MAT":"maths", "SGBD":"sgbd", "SPORT":"sport"};
var subjects_in_to_ext = {"acda":"ACDA", "eng":"ANG", "apl":"APL", "art":"ART", "asr":"ASR", "ec":"EC", "egod":"EGOD", "maths":"MAT", "sgbd":"SGBD", "sport":"SPORT"};

//obtenir le nombre d'eleves
function getStudentNumber() {
    var count = 0;
    for(var student in logins){
        count++;
    }
    return count;
}
var studentCount = getStudentNumber();


//############## EVENT LISTENERS #############
//appele a chaque fois qu'une des checkbox de matiere est changee
//si aucune checkbox est activee, le bouton "classer" est desactive, sinon il est active
function verifyCheckboxValid(){
    var submitButton = document.getElementById("sort-button");
    var subjectCheckboxes = document.getElementsByClassName("subject-checkbox");

    for(var checkbox in subjectCheckboxes){
        if (subjectCheckboxes[checkbox].checked) {
            submitButton.disabled = false;
            return;
        };
    }
    submitButton.disabled = true;
}
//appele quand on clic sur le bouton "classer"
//appele la fonction qui fait le classement
function getScoresCaller(){
    this.classer();
}


//################ AFFICHAGE #################
class View{
    constructor(){
        this.buildCheckboxes();
    }

    //construit les checkbox de matieres
    buildCheckboxes(){
        var subjectsForm = document.getElementById("subjects-checkboxes");
        for(var subject in subjects){
            var currLabel = document.createElement("label");
            currLabel.htmlFor = "checkbox-" + subject;
            currLabel.className = "checkbox-label";
            subjectsForm.appendChild(currLabel);

            var currCheckbox = document.createElement("input");
            currCheckbox.type = "checkbox";
            currCheckbox.name = "subjects[]";
            currCheckbox.value = subject;
            currCheckbox.className = "subject-checkbox";
            currCheckbox.id = "checkbox-" + subject;
            currCheckbox.addEventListener("change", verifyCheckboxValid);
            currLabel.appendChild(currCheckbox);

            var currText = document.createTextNode(subjects[subject]);
            currLabel.appendChild(currText);
        }
    }

    //affiche le classement envoye en parametre dans le tableau.
    displayClassment(subjectClassment){
        var table = document.getElementById("classment-table-body");
        table.innerHTML = "";
        for(var i = 0; i < subjectClassment.length; i++){
            var score = Math.round(subjectClassment[i][1]* 1000) / 10;
            if (score >= 0.1) {
                //afficher les resultats
                var currLine = document.createElement("tr");
                var cellRank = document.createElement("td");
                var cellName = document.createElement("td");
                var cellPoints = document.createElement("td");
                cellRank.innerHTML = i + 1;
                cellName.innerHTML = logins[subjectClassment[i][0]];
                cellPoints.innerHTML = score;
                currLine.appendChild(cellRank);
                currLine.appendChild(cellName);
                currLine.appendChild(cellPoints);
                table.appendChild(currLine);
            }
        }
    }
}


//################ TRAITEMENT ################
//Classe qui gere le classement des eleves dans une matiere
class SubjectClassment {
    constructor(subject_) {
        this.subject = subject_;
        this.subject_ext = subjects_in_to_ext[this.subject];

        this.createSuccessorMatrix();
        this.createPredecessorMatrix();
        this.pageRankInit();
        this.pageRank();
        this.createClassment();
    }

    //cree la matrice qui associe les voteurs aux votes
    createSuccessorMatrix() {
        this.successormatrix = {};
        for (var student in votes){
            this.successormatrix[student] = {};
            for(var vote in votes[student][this.subject_ext]){
                var voteLogin = votes[student][this.subject_ext][vote];
                this.successormatrix[student][voteLogin] = 1;
            }
        }
    }
    //cree la matrice qui associe les votes aux voteurs
    createPredecessorMatrix() {
        this.predecessorMatrix = {};
        for (var student in votes){
            for(var vote in votes[student][this.subject_ext]){
                var voteLogin = votes[student][this.subject_ext][vote];
                if (!(voteLogin in this.predecessorMatrix)) {
                    this.predecessorMatrix[voteLogin] = {};
                }
                this.predecessorMatrix[voteLogin][student] = 1;
            }
        }
    }
    //initialise le classement
    //scoreMap est un dictionaire de la forme: {login: score, login2: score}
    pageRankInit(){
        this.scoreMap = {}
        var startScore = 1 / studentCount;
        for(var student in this.successormatrix){
            this.scoreMap[student] = startScore;
        }
        for(var student in this.predecessorMatrix){
            this.scoreMap[student] = startScore;
        }
    }
    //Algorithme de classement, base sur PageRank de Google
    pageRank() {
        var finished = false;
        while (!finished) {
            finished = true;

            var newClassment = Object.assign({}, this.scoreMap);
            for(var student in this.scoreMap){
                //score de base de l'eleve
                newClassment[student] = (1 - SubjectClassment.dampingFactor)/studentCount;
                //ajouter le score obtenu a partir des gens qui ont vote pour lui
                var stepScore = 0;
                for(var predecessor in this.predecessorMatrix[student]){
                    var predecessorVotes = 0;
                    for(var successor in this.successormatrix[predecessor]){
                        predecessorVotes++;
                    }
                    stepScore += this.scoreMap[predecessor]/predecessorVotes;
                }
                newClassment[student] += SubjectClassment.dampingFactor * stepScore;

                //si la nouvelle valeur est trop differente de l'ancienne, continuer la boucle. pas besoin de trop de precision.
                if (Math.round(newClassment[student] *1000)/1000 != Math.round(this.scoreMap[student] *1000)/1000) {
                    finished = false;
                }
            }
            this.scoreMap = newClassment;
        }
    }
    //prends le dictionaire fait par pageRank() et cree un tableau de la forme: [[login, score], [login2, score], ...] trie sur le score
    createClassment(){
        this.sortedClassment = []
        for (var student in this.scoreMap) {
            if (this.scoreMap.hasOwnProperty(student)) {
                this.sortedClassment.push([student, this.scoreMap[student]]);
            }
        }

        this.sortedClassment.sort(function(a,b) {
            return b[1]-a[1];
        });
    }
}
//la seule maniere de creer une variable statique en javascript proprement...
SubjectClassment.dampingFactor = 0.85;

//Classe qui gere le classement des eleves dans plusieurs matieres
class MultipleSubjectClassment {
    constructor(subjects) {
        this.subjects = subjects;

        this.createScores();
        this.createClassment();
    }

    //apelle la classe SubjectClassment pour toutes les matieres selectionnees, et fait la moyenne des scores.
    //scoreMap est un dictionaire de la forme: {login: score, login2: score}
    createScores(){
        //faire la somme de tout les scores d'un eleve
        this.scoreMap = {}
        for(var subject in this.subjects){
            var currSubjectClassment = new SubjectClassment(this.subjects[subject]);
            var currClassment = currSubjectClassment.scoreMap;
            for(student in currClassment){
                if (!this.scoreMap.hasOwnProperty(student)) {
                    this.scoreMap[student] = 0;
                }
                this.scoreMap[student] += currClassment[student];
            }
        }
        //faire la moyenne
        for(var student in this.scoreMap){
            this.scoreMap[student] /= this.subjects.length;
        }
    }

    //prends le dictionaire fait par createScores() et cree un tableau de la forme: [[login, score], [login2, score], ...] trie sur le score
    createClassment(){
        this.sortedClassment = []
        for (var student in this.scoreMap) {
            if (this.scoreMap.hasOwnProperty(student)) {
                this.sortedClassment.push([student, this.scoreMap[student]]);
            }
        }

        this.sortedClassment.sort(function(a,b) {
            return b[1]-a[1];
        });
    }
}
//Classe principale
class Main {
    constructor() {
        this.view = new View();

        var submitButton = document.getElementById("sort-button");
        submitButton.addEventListener("click", getScoresCaller.bind(this));
    }

    //appele par l'event listener sur le bouton "Classer"
    classer(){
        this.getChecked();
        this.makeClassment();
    }
    //met les checkbox selectionnees dans this.checked
    getChecked(){
        var checkboxes = document.getElementsByClassName("subject-checkbox");
        this.checked = []
        for(var checkbox in checkboxes){
            if (checkboxes[checkbox].checked) {
                this.checked.push(checkboxes[checkbox].value);
            }
        }
    }
    //fait le classement, et l'envoie a l'affichage
    makeClassment(){
        if (this.checked.length < 1) {
            //ca ne devrais pas arriver mais on sais jamais
            alert("Erreur! Choisissez au moins une matière. Ceci ne devrais pas arriver.");
        }
        else{
            var classment;
            if(this.checked.length == 1){
                classment = new SubjectClassment(this.checked[0]);
            }
            else {
                classment = new MultipleSubjectClassment(this.checked);
            }
            this.view.displayClassment(classment.sortedClassment);
        }
    }
}


var main = new Main();
