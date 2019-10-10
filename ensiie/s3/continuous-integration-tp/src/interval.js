class Interval {
    constructor(start, end) {
        if (typeof start !== "number" || typeof end !== "number"){
            throw 'Please input a number';
        }
        if (start === end){
            throw 'start !== end';
        }
        if (start > end){
            throw 'start < end';
        }
        this.start = start;
        this.end = end
    }

    toString() {
        return "[" + this.start + "," + this.end + "]";
    }

    /**
     * @param {Interval} other
     * @returns {Interval}
     */
    isEqual(other){
        return this.start === other.start && this.end === other.end
    }

    /**
     * Exemple 1 :
     *      interval1 = [1,5]                          ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓
     *      interval2 = [2,3]                                  ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓
     *      interval1.overlaps(interval2) => true
     *
     * Exemple 2 :
     *      interval1 = [1,2]                          ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓
     *      interval2 = [5,9]                                                       ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓
     *      interval1.overlaps(interval2) => false
     *
     * @param {Interval} interval
     * @returns {boolean}
     */
    overlaps(interval) {
        return this.end > interval.start && this.start < interval.end;
    }

    /**
     * Retourne true si cet interval contient le paramètre interval
     *
     * Exemple 1 :
     *      interval1 = [1,5]                          ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓
     *      interval2 = [1,3]                                  ▓▓▓▓▓▓▓▓▓▓▓▓▓
     *      interval1.includes(interval2) => true
     *
     * Exemple 2 :
     *      interval1 = [1,2]                          ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓
     *      interval2 = [4,9]                              ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓
     *      interval1.includes(interval2) => false
     *
     * @param {Interval} interval
     * @returns {boolean}
     */
    includes(interval) {
        return this.end >= interval.end && this.start <= interval.start;
    };

    /**
     * Retourne l'union de deux intervals
     *
     * Exemple 1 :
     *      interval1 = [1,5]                          ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓
     *      interval2 = [4,6]                                              ▓▓▓▓▓▓▓▓▓▓▓▓▓
     *      interval1.union(interval2) => [1,6]        ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒
     *
     * Exemple 2 :
     *      interval1 = [[1,5]]                            ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓
     *      interval2 = [[6,10]]                                                       ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓
     *      interval1.union(interval2) => [[1,5], [6,10]]  ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒   ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒
     *
     * @param {Interval} interval
     * @returns {Interval[]}
     */
    union(interval) {
        if(this.overlaps(interval)){
            return [new Interval(Math.min(this.start, interval.start), Math.max(this.end, interval.end))];
        }
        return [this, interval];
    };

    /**
     * Retourne l'intersection de deux intervals
     *
     * Exemple 1 :
     *      interval1 =                          ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓
     *      interval2 =                                              ▓▓▓▓▓▓▓▓▓▓▓▓▓
     *      interval1.intersection(interval2) =>                     ▒▒▒▒▒
     *
     * Exemple 2 :
     *      interval1 =                          ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓
     *      interval2 =                                                      ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓
     *      interval1.intersection(interval2) => <tableau vide>
     *
     * @param {Interval} interval
     * @returns {Interval|null}
     */
    intersection(interval) {
        if(this.overlaps(interval)){
            return new Interval(Math.max(this.start, interval.start), Math.min(this.end, interval.end));
        }
    };

    /**
     * Retourne l'exclusion de deux intervals
     *
     * Exemple 1 :
     *      interval1 =                          ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓
     *      interval2 =                                              ▓▓▓▓▓▓▓▓▓▓▓▓▓
     *      interval1.exclusion(interval2) =>    ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒     ▒▒▒▒▒▒▒▒
     *
     * Exemple 2 :
     *      interval1 =                          ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓
     *      interval2 =                                                      ▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓
     *      interval1.exclusion(interval2) =>    ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒   ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒
     *
     * @param {Interval} interval
     * @returns {Interval[]}
     */
    exclusion(interval) {
        let points = [this.start, this.end, interval.start, interval.end];
        points.sort();
        if (points[1] === points[2]){
            return [new Interval(points[0], points[3])];
        }
        return [new Interval(points[0], points[1]), new Interval(points[2], points[3])];
    };
}

module.exports = Interval;
