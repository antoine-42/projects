const Util = require('../../src/math');


describe('sumPrime', function () {

    test('Test somme des premiers jusqu\'a -42', () => {
        expect(()=> {Util.sumPrime(-42)}).toThrow(/negative/);
    });

    test.each([
        [0, 0],
        [5, 10],
        [7, 17],
        [42, 238],
    ])(
        'Test somme des premiers jusqu\'a %i',
        (n, expected) => {
            expect(Util.sumPrime(n)).toBe(expected);
        }
    );
});

describe('fizzBuzz', function () {
    test('Test fizzBuzz jusqu\'a -42', () => {
        expect(()=> {Util.fizzBuzz(-42)}).toThrow(/negative/);
    });
    test('Test fizzBuzz jusqu\'a 0', () => {
        expect(()=> {Util.fizzBuzz(0)}).toThrow(/zero/);
    });
    test('Test somme des premiers jusqu\'a 15', () => {
        expect(Util.fizzBuzz(15)).toEqual([1, 2, "Fizz", 4, "Buzz", "Fizz", 7, 8, "Fizz", "Buzz", 11, "Fizz", 13, 14, "FizzBuzz"]);
    });
});

describe('sumPrime', function () {

    test('Test somme de chiffrage de -42', () => {
        expect(()=> {Util.cipher(-42)}).toThrow(/string/);
    });

    test.each([
        ["Test Unitaire", "Uftu Vojubjsf"],
        ["qwertyuiopasdfghjklzxcvbnm", "rxfsuzvjpqbteghiklmaydwcon"],
        ["QWERTYUIOPASDFGHJKLZXCVBNM", "RXFSUZVJPQBTEGHIKLMAYDWCON"],
        ["test😋日本語42;!~ \n", "uftu😋日本語42;!~ \n"],
        ["", ""],
    ])(
        'Test chiffrage de %i',
        (n, expected) => {
            expect(Util.cipher(n)).toBe(expected);
        }
    );
});

describe('pairs', function () {

    test('Test somme de pairs de', () => {
        expect(()=> {Util.pairs(-42)}).toThrow(/array/);
    });
    test('Test somme de pairs de', () => {
        expect(()=> {Util.pairs(["test"])}).toThrow(/number array/);
    });

    test.each([
        [[3,3], 1],
        [[3,3,5,], 1],
        [[3,3,5,5,5], 4],
    ])(
        'Test comptage de pairs de %n',
        (n, expected) => {
            expect(Util.pairs(n)).toBe(expected);
        }
    );
});
