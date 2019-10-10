const BookRepository = require('./book.repository');
const db = require('./db');

const repository = new BookRepository(db);

repository.save({
    'id' : 1,
    "name" :"test",
    'price' :6.1,
    "added_at" : '2019-01-01'
});
repository.save({
    "id": 2,
    "name": "Leviathan Wakes",
    "price": 10,
    "added_at": "2015-04-01"
});
repository.save({
    "id": 3,
    "name": "The Hitchhiker's Guide to the Galaxy",
    "price": 5.42,
    "added_at": "2001-09-11"
});
repository.getCountBookAddedByMont();
