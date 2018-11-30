(ns jreber.boids.math.vector
  (:refer-clojure :exclude [+ - /])
  (:require [clojure.algo.generic
             [arithmetic :as generic :refer [+ - /]]
             [math-functions :as m]]
            [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as gen]))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; vector definitions
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defprotocol Vector
  "A representation of a simple vector. Double values of
  infinity (either one) and NaN are unsupported and lead to arbitrary
  behavior."
  (magnitude [v] "Returns the magnitude of the vector.")
  (angle     [v] "Returns the angle of the vector in radians.")
  (x         [v] "Returns the x component of v, assuming 'x' means something useful.")
  (y         [v] "Returns the y component of v, assuming 'y' means something useful.")
  (add       [v other] "Returns a new vector that is the sum of v and other.")
  (scale     [v scaler] "Returns a new vector that is a scaled version of v."))

(s/def ::vector #(satisfies? Vector %))
(s/def ::nice-double (s/and float?
                            #(not (Double/isNaN %))
                            #(not (Double/isInfinite %))))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; vector implementations
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(def square #(Math/pow % 2))
(def sqrt   #(Math/sqrt %))
(def pi     Math/PI)
(def pi-half (/ pi 2))
(def pi-three-halves (* (/ pi 2) 3))
(def two-pi (* 2 pi))
(defn arctan [x y]
  (Math/atan2 y x)) ;; yes, y before x; see javadoc


(extend-protocol Vector
  clojure.lang.PersistentVector
  (magnitude [[x y]] (sqrt
                      (+ (square x) (square y))))
  (angle     [[x y]] (mod
                      (arctan x y)
                      two-pi))
  (x         [[x y]] x)
  (y         [[x y]] y)
  (add       [v other] (mapv + v
                             ((juxt x y) other)))
  (scale [v scaler] (mapv #(* scaler %) v)))

(s/def ::vector
  (s/with-gen ::vector
    #(gen/vector (s/gen ::nice-double)
                 2 2))) ;; because vectors are now Vectors


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; basic vector operations
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defmethod + [clojure.lang.PersistentVector clojure.lang.PersistentVector]
  [v1 v2]
  (add v1 v2))

(defmethod - [clojure.lang.PersistentVector clojure.lang.PersistentVector]
  [v1 v2]
  (let [negate #(scale % -1)]
    (add v1 (negate v2))))

(defmethod / [clojure.lang.PersistentVector java.lang.Number]
  [v s]
  (scale v (/ s)))

(defn avg
  "Returns a vector that is the average of the input vectors."
  [vs]
  (/ (reduce + vs)
     (count vs)))

(s/fdef avg
  :args (s/cat :vs (s/coll-of ::vector :min-count 1))
  :ret ::vector
  :fn (let [find (fn [reducer pos invocation]
                   (reduce reducer
                           (map pos
                                (-> invocation :args :vs))))]
        (s/and #(<= (find min x %) (x (:ret %)))
               #(<= (x (:ret %)) (find max x %))
               #(<= (find min y %) (y (:ret %)))
               #(<= (y (:ret %)) (find max y %)))))
