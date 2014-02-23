(ns ninterpolate.core
   (:gen-class)
)
(use 'clojure.tools.trace)

(defn- bracket2 
    "returns a list containing 2 key value pairs that bracket the x value. the first 2 pairs or last 2 pairs are returned if x is 
     outside of the range of keys. note a key is always a number but a value can be a number or another list"
    [x table]
    {:pre [(>= (count table) 2)]}
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
    {:pre [(>= (count table) 3)]}
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


(defn linear_interpolate
     ([table xr]
      {:pre [
             (isa? (class xr) Number)     
            ]
       }
       (let [
             [[xl yl] [xh yh]] (bracket2 xr table)
             slope (/ (- yh yl) (- xh xl))
             diff (- xr xl)
            ]
            (+ yl (* diff slope)) 
       )
     )

     ([table xr1 & xrs]
       (let [
             [[xl tablel] [xh tableh]] (bracket2 xr1 table)
             yl (apply linear_interpolate tablel xrs)
             yh (apply linear_interpolate tableh xrs)
            ]
            (linear_interpolate {xl yl xh yh} xr1)
      )
    )
)

(declare lagrange2_interpolate)
;
; this function can only be called if table has at least 3 items
;
(defn- lagrange2_interpolate_ok
     ([table xr]
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
            (+ (* xx3 (- (* q1 xx2) (* q2 xx1))) (* q3 xx1 xx2))
       )
     )

     ([table xr1 & xrs]
       (let [
             [[xl tablel] [xh1 tableh1] [xh2 tableh2]] (bracket3 xr1 table)
             yl (apply lagrange2_interpolate tablel xrs)
             yh1 (apply lagrange2_interpolate tableh1 xrs)
             yh2 (apply lagrange2_interpolate tableh2 xrs)
            ]
            (lagrange2_interpolate {xl yl xh1 yh1 xh2 yh2} xr1)
      )
    )
)


;
; let's default to linear if table isn't long enough
;
(defn lagrange2_interpolate [table & args]
    (cond
     (>= (count table) 3) (apply lagrange2_interpolate_ok table args)
     :else (apply linear_interpolate table args)
    )
)
