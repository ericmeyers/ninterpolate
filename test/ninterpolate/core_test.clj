(ns ninterpolate.core-test
  (:require [clojure.test :refer :all]
            [ninterpolate.core :refer :all]))


(def table1d   {1.0,2.0 3.0,4.0 7.0,6.0})
(def table1dus {5.0,6.0 1.0,2.0 3.0,4.0})

(def table2d
    {
     1.0 {1,2 3,4 5,6 7,10.0}
     2.0 {7,8 9,10.0, 11,12.0}
    }
)

(def table3d
  {
   0.0
    {
      1.0 {1,2 3,4 5,6 7,10.0}
      2.0 {7,8 9,10.0, 11,12.0}
    }
  
   4.0
    {
      1.5 {11,2 13,4 15,6 7,10.0}
      2.5 {17,8 19,10.0, 11,12.0}
    }
  }
 
)


(deftest linear-test
 (testing "univariant"
    (is (= 3.0 (interpolate table1d 2.0))   "sorted")
    (is (= 3.0 (interpolate table1dus 2.0)) "unsorted")
    (is (= 2.0 (interpolate table1dus -1.0)) "np extrapolate")
    (is (= 0.0 (interpolate table1dus {:extrap true :value -1.0})) "extrapolate")
    (is (= 3.083333333333333 (interpolate table1d {:value 2.0 :order 2}))   "2nd order")

 )
 (testing "bivariant"
    (is (= 2.0 (interpolate table2d 1.0 1.0)) "bivariant linear no extrap")
    (is (= 9.0 (interpolate table2d 1.5 7.0)) "bivariant linear no extrap")
    (is (= 9.5 (interpolate table2d 1.5 8.0)) "bivariant linear no extrap")
    (is (= 10.5 (interpolate table2d 1.5 {:extrap true :value 8.0})) "bivariant linear extrap")
    (is (= 15.0 (interpolate table2d {:extrap true :value 0.0} {:extrap true :value 8.0})) "bivariant linear extrap")
 )
 (testing "trivariant"
    (is (= 4.0 (interpolate table3d 0.0 1.0 3.0)) "3d")
    (is (= 10.0 (interpolate table3d 4.0 1.5 7.0)) "3d")
    (is (= 12.0 (interpolate table3d 4.0 2.5 7.0)) "3d")
    (is (= 11.0 (interpolate table3d 4.0 2.0 7.0)) "3d")
  )
)

(deftest lagrange-second-order-test
 (testing "univariant"
    (is (= 3.083333333333333 (interpolate table1d {:order 2 :value 2.0})))
    (is (= 16.5 (interpolate table2d {:extrap true :value 0.0} {:order 2 :extrap true :value 8.0})) "bivariant linear extrap")
 
 )

)
