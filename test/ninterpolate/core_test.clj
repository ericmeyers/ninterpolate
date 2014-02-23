(ns ninterpolate.core-test
  (:require [clojure.test :refer :all]
            [ninterpolate.core :refer :all]))


(def table1d   {1,2 3,4 5,6})
(def table1dus {5,6 1,2 3,4})

(deftest linear-test
    (is (= 3.0 (linear_interpolate table1d 2.0))   "sorted")
    (is (= 3.0 (linear_interpolate table1dus 2.0)) "unsorted")
    (is (= 0.0 (linear_interpolate table1dus -1.0)) "extrapolate")
)
