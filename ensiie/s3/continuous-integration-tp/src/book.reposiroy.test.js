const BookRepository = require('./book.repository');
let book = {
    "id": 1,
    "name": "test",
    "price": 6.1,
    "added_at": "2019-01-01"
};
let books = [
    {
        "id": 3,
        "name": "The Hitchhiker's Guide to the Galaxy",
        "price": 5.42,
        "added_at": "2001-09-11"
    },
    {
        "id": 2,
        "name": "Leviathan Wakes",
        "price": 10,
        "added_at": "2015-04-01"
    },
    book,
];

describe('Book repository Save', function () {
    const dbMock = {
        get : jest.fn().mockReturnThis(),
        push : jest.fn().mockReturnThis(),
        write : jest.fn().mockReturnThis()
    };
    const repository = new BookRepository(dbMock);
    repository.save(book);

    test('Save book push call', () => {
        expect(dbMock.push.mock.calls[0]).toEqual([book]);
    });
    test('Save book get parameter', () => {
        expect(dbMock.get.mock.calls[0]).toEqual(['books']);
    });
    test('Save book get call number', () => {
        expect(dbMock.get.mock.calls.length).toBe(1);
    });
    test('Save book push call number', () => {
        expect(dbMock.push.mock.calls.length).toBe(1);
    });
    test('Save book write call number', () => {
        expect(dbMock.write.mock.calls.length).toBe(1);
    });
});

describe('Book repository get total count', function () {
    const dbMock = {
        get : jest.fn().mockReturnThis(),
        value : jest.fn().mockReturnValue(books)
    };
    const repository = new BookRepository(dbMock);

    test('get book total count', () => {
        expect(repository.getTotalCount()).toBe(3);
    });
    test('get book total count value result', () => {
        expect(dbMock.value.mock.results[0].value).toBe(books);
    });
    test('get book total count get parameter', () => {
        expect(dbMock.get.mock.calls[0]).toEqual(['books']);
    });
    test('get book total count get call number', () => {
        expect(dbMock.get.mock.calls.length).toBe(1);
    });
    test('get book total count value call number', () => {
        expect(dbMock.value.mock.calls.length).toBe(1);
    });
});

describe('Book repository get total price', function () {
    const dbMock = {
        get : jest.fn().mockReturnThis(),
        value : jest.fn().mockReturnValue(books)
    };
    const repository = new BookRepository(dbMock);

    test('get book total price', () => {
        expect(repository.getTotalPrice()).toBe(21.52);
    });
    test('get book total price value result', () => {
        expect(dbMock.value.mock.results[0].value).toBe(books);
    });
    test('get book total price get parameter', () => {
        expect(dbMock.get.mock.calls[0]).toEqual(['books']);
    });
    test('get book total price get call number', () => {
        expect(dbMock.get.mock.calls.length).toBe(1);
    });
    test('get book total price value call number', () => {
        expect(dbMock.value.mock.calls.length).toBe(1);
    });
});

describe('Book repository get by name', function () {
    const dbMock = {
        get : jest.fn().mockReturnThis(),
        find : jest.fn().mockReturnThis(),
        value : jest.fn()
            .mockReturnValueOnce(books[1])
            .mockReturnValueOnce(books[2])
            .mockReturnValue(books[0])
    };
    const repository = new BookRepository(dbMock);

    test('get book by name 1', () => {
        expect(repository.getBookByName(books[1]["name"])).toBe(books[1]);
    });
    test('get book by name 2', () => {
        expect(repository.getBookByName(books[2]["name"])).toBe(books[2]);
    });
    test('get book by name 0', () => {
        expect(repository.getBookByName(books[0]["name"])).toBe(books[0]);
    });
    test('get book by name value result', () => {
        expect(dbMock.value.mock.results[0].value).toBe(books[1]);
    });
    test('get book by name get parameter', () => {
        expect(dbMock.get.mock.calls[0]).toEqual(['books']);
    });
    test('get book by name find parameter', () => {
        expect(dbMock.find.mock.calls[0]).toEqual([{ name: books[1]["name"] }]);
    });
    test('get book by name get call number', () => {
        expect(dbMock.get.mock.calls.length).toBe(3);
    });
    test('get book by name find call number', () => {
        expect(dbMock.find.mock.calls.length).toBe(3);
    });
    test('get book by name value call number', () => {
        expect(dbMock.value.mock.calls.length).toBe(3);
    });
});

describe('Book repository get book count by month', function () {
    const dbMock = {
        get : jest.fn().mockReturnThis(),
        sortBy : jest.fn().mockReturnThis(),
        value : jest.fn().mockReturnValue(books)
    };
    const repository = new BookRepository(dbMock);

    test('get book count by month', () => {
        expect(repository.getCountBookAddedByMont()).toStrictEqual([
            {
                "year": 2001,
                "month": 9,
                "count": 1,
                "count_cumulative": 1
            },
            {
                "year": 2015,
                "month": 4,
                "count": 1,
                "count_cumulative": 2
            },
            {
                "year": 2019,
                "month": 1,
                "count": 1,
                "count_cumulative": 3
            }
        ]);
    });
    test('get book count by month value result', () => {
        expect(dbMock.value.mock.results[0].value).toBe(books);
    });
    test('get book count by month get parameter', () => {
        expect(dbMock.get.mock.calls[0]).toEqual(['books']);
    });
    test('get book count by month get call number', () => {
        expect(dbMock.get.mock.calls.length).toBe(1);
    });
    test('get book count by month sortBy call number', () => {
        expect(dbMock.sortBy.mock.calls.length).toBe(1);
    });
    test('get book count by month value call number', () => {
        expect(dbMock.value.mock.calls.length).toBe(1);
    });
});