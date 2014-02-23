(ns ninterpolate.core-test
  (:require [clojure.test :refer :all]
            [ninterpolate.core :refer :all]))


(def table1d {1,2 3,4 5,6})

(deftest simple-test
  (testing "test a simple 1-d table"
    (is (= 3.0 (linear_interpolate table1d 2.0)))
  )
)
