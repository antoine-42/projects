const Interval = require('./interval');

describe('constructor', function () {
    test.each([
        ["a", 5],
        [1, "5"],
        ["1", "a"],
    ])(
        'Test de constructor avec debut=%s et fin=%s',
        (a, b) => {
            expect(()=> {new Interval(a, b)}).toThrow(/number/);
        }
    );

    test.each([
        [10, 5],
        [1, -2],
        [-1, -2],
        [Number.MAX_SAFE_INTEGER, Number.MIN_SAFE_INTEGER],
        [Number.MAX_SAFE_INTEGER, 0],
    ])(
        'Test de constructor avec debut=%s et fin=%s',
        (a, b) => {
            expect(()=> {new Interval(a, b)}).toThrow(/</);
        }
    );

    test.each([
        [10, 10],
        [Number.MAX_SAFE_INTEGER, Number.MAX_SAFE_INTEGER],
        [-42, -42],
        [Number.MIN_SAFE_INTEGER, Number.MIN_SAFE_INTEGER],
        [0, 0],
    ])(
        'Test de constructor avec debut=%s et fin=%s',
        (a, b) => {
            expect(()=> {new Interval(a, b)}).toThrow(/!==/);
        }
    );

    test.each([
        [1, 5, "[1,5]"],
        [1, Number.MAX_SAFE_INTEGER, "[1," + Number.MAX_SAFE_INTEGER + "]"],
        [Number.MIN_SAFE_INTEGER, 5, "[" + Number.MIN_SAFE_INTEGER + ",5]"],
        [-42, -5, "[-42,-5]"],
        [-1, 0, "[-1,0]"],
    ])(
        'Test de constructor avec debut=%s et fin=%s',
        (a, b, expected) => {
            expect(new Interval(a, b).toString()).toBe(expected);
        }
    );
});

describe('equals', function () {
    test.each([
        [new Interval(1, 5), new Interval(2, 3), false],
        [new Interval(1, 5), new Interval(1, 3), false],
        [new Interval(1, 3), new Interval(2, 3), false],
        [new Interval(1, 2), new Interval(1, 2), true],
        [new Interval(1, Number.MAX_SAFE_INTEGER), new Interval(1, Number.MAX_SAFE_INTEGER), true],
        [new Interval(0, 5), new Interval(0, 5), true],
        [new Interval(Number.MIN_SAFE_INTEGER, 5), new Interval(Number.MIN_SAFE_INTEGER, 5), true],
    ])(
        'Test d\'egalite de %s et %s',
        (a, b, expected) => {
            expect(a.isEqual(b)).toBe(expected);
        }
    );
});

describe('overlaps', function () {

    test.each([
        [new Interval(1, 5), new Interval(2, 3), true],
        [new Interval(1, 2), new Interval(5, 9), false],
        [new Interval(1, 2), new Interval(2, 3), false],
        [new Interval(1, 2), new Interval(0, 1), false],
        [new Interval(1, 5), new Interval(1, 5), true],
        [new Interval(1, 5), new Interval(0, 3), true],
        [new Interval(1, 5), new Interval(2, 5), true],
        [new Interval(-1, 5), new Interval(2, 5), true],
        [new Interval(1, 5), new Interval(-2, 5), true],
        [new Interval(-3, 5), new Interval(-2, 3), true],
    ])(
        'Test de couverture de %s par %s',
        (a, b, expected) => {
            expect(a.overlaps(b)).toBe(expected);
        }
    );
});

describe('includes', function () {
    test.each([
        [new Interval(1, 5), new Interval(2, 3), true],
        [new Interval(1, 2), new Interval(5, 9), false],
        [new Interval(1, 5), new Interval(1, 5), true],
        [new Interval(1, 5), new Interval(0, 3), false],
        [new Interval(1, 5), new Interval(2, 5), true],
        [new Interval(-1, 5), new Interval(2, 5), true],
        [new Interval(1, 5), new Interval(-2, 5), false],
        [new Interval(-3, 5), new Interval(-2, 3), true],
    ])(
        'Test d\'inclusion de %s par %s',
        (a, b, expected) => {
            expect(a.includes(b)).toBe(expected);
        }
    );
});

describe('union', function () {
    test.each([
        [new Interval(1, 5), new Interval(2, 3), [new Interval(1, 5)]],
        [new Interval(1, 2), new Interval(5, 9), [new Interval(1, 2), new Interval(5, 9)]],
        [new Interval(1, 5), new Interval(1, 5), [new Interval(1, 5)]],
        [new Interval(1, 5), new Interval(0, 3), [new Interval(0, 5)]],
        [new Interval(-1, 5), new Interval(2, 5), [new Interval(-1, 5)]],
        [new Interval(1, 5), new Interval(-2, 5), [new Interval(-2, 5)]],
        [new Interval(-3, 5), new Interval(-2, 3), [new Interval(-3, 5)]],
    ])(
        'Test d\'union de %s et %s',
        (a, b, expected) => {
            expect(a.union(b)).toStrictEqual(expected);
        }
    );
});

describe('intersection', function () {
    test.each([
        [new Interval(1, 5), new Interval(2, 3), new Interval(2, 3)],
        [new Interval(2, 5), new Interval(1, 3), new Interval(2, 3)],
        [new Interval(1, 3), new Interval(2, 5), new Interval(2, 3)],
        [new Interval(-1, 5), new Interval(2, 3), new Interval(2, 3)],
        [new Interval(1, 5), new Interval(-2, 3), new Interval(1, 3)],
        [new Interval(-1, 5), new Interval(-2, 3), new Interval(-1, 3)],
        [new Interval(-5, 0), new Interval(2, 3), undefined],
        [new Interval(1, 2), new Interval(2, 3), undefined],
    ])(
        'Test d\'union de %s et %s',
        (a, b, expected) => {
            expect(a.intersection(b)).toStrictEqual(expected);
        }
    );
});

describe('exclusion', function () {
    test.each([
        [new Interval(1, 2), new Interval(5, 9), [new Interval(1, 2), new Interval(5, 9)]],
        [new Interval(1, 5), new Interval(5, 9), [new Interval(1, 9)]],
        [new Interval(1, 7), new Interval(5, 9), [new Interval(1, 5), new Interval(7, 9)]],
    ])(
        'Test d\'union de %s et %s',
        (a, b, expected) => {
            expect(a.exclusion(b)).toStrictEqual(expected);
        }
    );
});