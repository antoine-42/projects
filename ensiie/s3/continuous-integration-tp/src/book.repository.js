class BookRepository {

    /**
     * @param db
     */
    constructor(db) {
        this.db = db;
    }

    save (book) {
        this.db.get('books').push(book).write();
    }

    /**
     * Nombre total de livre
     */
    getTotalCount() {
        let books = this.db.get('books').value();
        return books.length;
    }

    /**
     * Somme du prix de tous les livre
     */
    getTotalPrice() {
        let books = this.db.get('books').value();
        let cost = 0;
        for (let i = 0; i < books.length; i++) {
            cost += books[i]["price"]
        }
        return Math.round(cost * 10000) / 10000;
    }


    /**
     * Retourne un livre
     */
    getBookByName(bookName) {
        return this.db.get('books').find({ name: bookName }).value();
    }

    /**
     * Nombre de livre ajouté par mois
     *
     *  [
     *      {
     *          year: 2017,
     *          month, 2,
     *          count, 129,
     *          count_cumulative: 129
     *      },
     *      {
     *          year: 2017,
     *          month, 3,
     *          count, 200,
     *          count_cumulative: 329
     *      },
     *      ....
     *  ]
     */
    getCountBookAddedByMont() {
        let result = [];
        let books = this.db.get('books').sortBy("added_at").value();

        let oldYear = -1;
        let oldMonth = -1;
        for (let i = 0; i < books.length; i++) {
            let currYear = parseInt(books[i]["added_at"].split("-")[0]);
            let currMonth = parseInt(books[i]["added_at"].split("-")[1]);

            if (oldMonth !== currMonth || oldYear !== currYear){
                let lastCount = 0;
                if(result.length > 0){
                    lastCount = result[result.length - 1]["count_cumulative"];
                }
                result.push({
                    year: currYear,
                    month: currMonth,
                    count: 1,
                    count_cumulative: lastCount + 1
                })
            }
            else {
                result[result.length - 1]["count"]++;
                result[result.length - 1]["count_cumulative"]++;
            }

            oldMonth = currMonth;
            oldYear = currYear;
        }

        return result;
    }

}


module.exports = BookRepository;