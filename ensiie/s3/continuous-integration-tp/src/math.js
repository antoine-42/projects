let Util = {};
Util.factorial = (n) => {
    if (n === 0) {
        return 1;
    }

    if (n >= 3000) {
        throw 'n too large'
    }

    if (n < 0) {
        throw 'n is negative'
    }

    return n * Util.factorial(n - 1);
};

/**
 * Détermine si n est un nombre premier.
 * Util.isPrime(5) => false
 * Util.isPrime(6) => true
 *
 * @param {number} n
 * @returns {boolean}
 */
Util.isPrime = function (n) {
    if (n === 1 || n === 0) {
        return false;
    }
    if (n < 0) {
        throw 'Unable to compute prime for n < 0'
    }
    for (var i = 2; i < n; i++)
        if (n % i === 0) return false;
    return true;

};


/**
 * Additionne l'ensemble des nombres premiers de 2 à n
 *
 * Util.sumPrime(6) = 2 + 3 + 5 = 10
 * Util.sumPrime(8) = 2 + 3 + 5 + 7 = 17
 *
 * @param {number} n
 * @returns {number}
 */
Util.sumPrime = function(n) {
    if (n < 0) {
        throw 'Unable to compute prime for negative numbers.'
    }
    let sum = 0;
    for (i = 0; i <= n; i++){
        if (Util.isPrime(i)){
            sum += i;
        }
    }
    return sum
};

/**
 * Cette méthode doit retourner un tableau de 1 à n tel que:
 * - Pour les nombres multiples de 3, les remplacer par "Fizz"
 * - Pour les nombres multiples de 5, les remplacer par "Buzz"
 * - Pour les nombres multiples de 3 et 5, les remplacer par "FizzBuzz"
 *
 * Exp :
 * Util.fizzBuzz(15) => [1, 2, "Fizz", 4, "Buzz", "Fizz", 7, 8, "Fizz", "Buzz", 11, "Fizz", 13, 14, "FizzBuzz"]
 *
 * @param {number} n
 * @returns {array}
 */
Util.fizzBuzz = function(n) {
    if (n <= 0) {
        throw 'Unable to compute fizzBuzz for negative numbers or zero.'
    }
    let array = []
    for (i = 1; i <= n; i++){
        let index = i - 1
        array[index] = i
        if (i % 3 === 0){
            array[index] = "Fizz"
            if (i % 5 === 0){
                array[index] += "Buzz"
            }
        }
        else if (i % 5 === 0){
            array[index] = "Buzz"
        }
    }
    return array
};

/**
 * Chiffre une phrase selon la règle suivante : Les A deviennent des B, les B des C, etc.
 * Les Z deviennent des A
 *
 * Exp :
 * Util.cipher("Test Unitaire") => "Uftu Tojubjsf"
 *
 * @param phrase
 * @returns {string}
 */
Util.cipherNum = 1;
Util.cipher = function (phrase) {
    if (typeof phrase != "string"){
        throw 'Please input a string.'
    }
    let result = '';
    for (var i = 0; i < phrase.length; i++) {
        let currChar = phrase[i];
        if (currChar.match(/[a-z]/i)){
            let charcode = (currChar.charCodeAt()) + Util.cipherNum;
            let newChar = String.fromCharCode(charcode);
            if (!newChar.match(/[a-z]/i)){
                newChar = String.fromCharCode(charcode - 26);
            }
            result += newChar
        }
        else {
            result += currChar
        }
    }
    return result;
};

/**
 * Retourne le nombre de paires dans un tableau
 *
 * Exp :
 *
 * Util.pairs([3,3]) => 1
 * Util.pairs([3,3,5,]) => 1
 * Util.pairs([3,3,5,5,5]) => 4
 *
 * @param array
 * @return int
 */
Util.pairs = function(array) {
    if (array.constructor !== Array){
        throw 'Please input an array.'
    }
    let numPairs = 0;
    for (let i = 0; i < array.length; i++) {
        if (typeof array[i] !== "number"){
            throw 'Please input a number array.'
        }
        for (let j = i+1; j < array.length; j++) {
            if (array[i] === array[j]){
                numPairs++;
            }
        }
    }
    return numPairs
};


module.exports = Util;
