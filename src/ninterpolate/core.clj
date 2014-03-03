(ns ninterpolate.core)

(defn- bracket2 
    "returns a list containing 2 key value pairs that bracket the x value. the first 2 pairs or last 2 pairs are returned if x is 
     outside of the range of keys. note a key is always a number but a value can be a number or another list"
    [x table]
    {:pre
       [(map? table)
        (>= (count table) 2)
       ]
    }
    (let [
          m (into (sorted-map) table)
          lows  (subseq m <= x)
          highs (subseq m > x)
         ]
         (cond
           (empty? lows)  [(first m) (first (rest m))]
           (empty? highs) [(first (rest (reverse m))) (last m)] 
           :else [(last lows) (first highs)]
         )
    )
)

(defn- bracket3 
    "returns a list containing 3 key value pairs that bracket the x value."
    [x table]
    {:pre [
      (map? table)
      (>= (count table) 3)
    ]}
    (let [
          m (into (sorted-map) table)
          lows  (subseq m <= x)
          highs (subseq m > x)
         ]
         (cond
           (empty? lows)  [(first m) (first (rest m)) (first (rest (rest m)))]
           (empty? highs) [(first (rest (rest (reverse m)))) (first (rest (reverse m))) (last m)] 
           (< (count highs) 2) [(first (rest (rest (reverse m)))) (first (rest (reverse m))) (last m)] 
           :else [(last lows) (first highs) (second highs)]
         )
    )
)

(declare interpolate)

(defn- linear_interpolate
     ([{extrap :extrap} table xr]
      {:pre [
             (isa? (class xr) Number)     
            ]
       }
       (let [
             [[xl yl] [xh yh]] (bracket2 xr table)
             slope (/ (- yh yl) (- xh xl))
             diff (- xr xl)
            ]
            (cond
             (and (not extrap) (< xr xl)) yl
             (and (not extrap) (> xr xh)) yh
             :else (+ yl (* diff slope))
            ) 
       )
     )

     ([opts table xr1 & xrs]
       (let [
             [[xl tablel] [xh tableh]] (bracket2 xr1 table)
             yl (apply interpolate tablel xrs)
             yh (apply interpolate tableh xrs)
            ]
            (linear_interpolate opts {xl yl xh yh} xr1)
      )
    )
)


(defn- lagrange2_interpolate
     ([{extrap :extrap} table xr]
      {:pre [
             (isa? (class xr) Number)     
            ]
       }
       (let [
             [[xl yl] [xh1 yh1] [xh2 yh2]] (bracket3 xr table)
             c12 (- xl xh1)
             c13 (- xl xh2)
             c23 (- xh1 xh2)
             q1 (/ yl  (* c12 c13))
             q2 (/ yh1 (* c12 c23))
             q3 (/ yh2 (* c13 c23))
             xx1 (- xr xl)
             xx2 (- xr xh1)
             xx3 (- xr xh2)
            ]
            (cond
              (and (not extrap) (< xr xl)) yl
              (and (not extrap) (> xr xh2)) yh2
              :else (+ (* xx3 (- (* q1 xx2) (* q2 xx1))) (* q3 xx1 xx2))
           )
       )
     )

     ([opts table xr1 & xrs]
       (let [
             [[xl tablel] [xh1 tableh1] [xh2 tableh2]] (bracket3 xr1 table)
             yl (apply interpolate tablel xrs)
             yh1 (apply interpolate tableh1 xrs)
             yh2 (apply interpolate tableh2 xrs)
            ]
            (lagrange2_interpolate opts {xl yl xh1 yh1 xh2 yh2} xr1)
      )
    )
)


(defmulti interpolate 
 "Interpolates a table (map) using one or more supplied values. Tables
  can be nested. The number of arguments should be consistent with the
  number of table dimensions."
  (fn [table xr & xrs] (map? xr)))

;
; no options supplied with the independent argument so just call linear interpolation without extrapolation
;
(defmethod interpolate false [table xr & xrs] 
   (apply linear_interpolate {:extrap false} table xr xrs)
)

;
; xr is a hash meaning some options were passed with the independent argument (interpolation order, allow extrapolation, etc)
;
(defmethod interpolate true [table xr & xrs] 
   {:pre [
           (not (nil? (:value xr)))     
         ]
       }
   (let [
         xv (:value xr)
         extrap (:extrap xr)
         order (or (xr :order) 1)
        ]
        (cond
         (= 1 order) (apply linear_interpolate {:extrap extrap} table xv xrs)
         (= 2 order) (apply lagrange2_interpolate {:extrap extrap} table xv xrs)
        )
   )
)
