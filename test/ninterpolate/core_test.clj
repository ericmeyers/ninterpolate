(ns ninterpolate.core-test
  (:require [clojure.test :refer :all]
            [ninterpolate.core :refer :all]))


(def table1d   {1,2 3,4 5,6})
(def table1dus {5,6 1,2 3,4})

(def table2d
    {
     1.0 {1,2 3,4 5,6 7,10.0}
     2.0 {7,8 9,10.0, 11,12.0}
    }
)

(deftest linear-test
 (testing "univariant"
    (is (= 3.0 (linear_interpolate table1d 2.0))   "sorted")
    (is (= 3.0 (linear_interpolate table1dus 2.0)) "unsorted")
    (is (= 0.0 (linear_interpolate table1dus -1.0)) "extrapolate")
 )
 (testing "bivariant"
    (is (= 2.0 (linear_interpolate table2d 1.0 1.0)) )
    (is (= 9.0 (linear_interpolate table2d 1.5 7.0)) )
    (is (= 10.5 (linear_interpolate table2d 1.5 8.0)) )
 )
)
