(ns jreber.boids.math.vector-test
  (:refer-clojure :exclude [+ - /])
  (:require [clojure.test :refer :all]
            [jreber.boids.math.vector :as v]
            [clojure.algo.generic.arithmetic :refer [+ - /]]
            [clojure.spec.alpha :as s]
            [clojure.spec.test.alpha :as stest]
            [clojure.test.check
             [generators :as gen]
             [clojure-test :refer [defspec]]
             [properties :as prop]]
            [plumula.mimolette.alpha :refer [defspec-test]]))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; test Vector implementation
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defspec magnitude
  (prop/for-all [[a b :as v] (s/gen ::v/vector)]
    (let [c (v/magnitude v)]
      (and (>= (+ c 1E-100) (max a b)) ;; handle stupid float rounding
           (not (neg? c))))))

(defspec angle
  (prop/for-all [[x y :as v] (s/gen ::v/vector)]
    (let [q (v/angle v)
          >=zero? #(or (zero? %) (pos? %))
          <=zero? #(or (zero? %) (neg? %))]
      (and (<= 0 q v/two-pi)
           (cond
             (and (>=zero? x) (>=zero? y)) (<= 0 q v/pi-half) ;; top right quadrant
             (and (<=zero? x) (>=zero? y)) (<= v/pi-half q v/pi) ;; top left
             (and (<=zero? x) (<=zero? y)) (<= v/pi q v/pi-three-halves) ;; bottom left
             (and (>=zero? x) (<=zero? y)) (or (<= v/pi-three-halves q v/two-pi)
                                               (zero? q)))))))

(defspec x
  (prop/for-all [[x y :as v] (s/gen ::v/vector)]
    (= x (v/x v))))

(defspec y
  (prop/for-all [[x y :as v] (s/gen ::v/vector)]
    (= y (v/y v))))

(defspec add
  (prop/for-all [[x1 y1 :as v1] (s/gen ::v/vector)
                 [x2 y2 :as v2] (s/gen ::v/vector)]
    (= (v/add v1 v2)
       [(+ x1 x2)
        (+ y1 y2)])))

(defspec scale
  (prop/for-all [[x y :as v] (s/gen ::v/vector)
                 s (s/gen ::v/nice-double)]
    (= (v/scale v s)
       [(* x s)
        (* y s)])))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; test basic vector operations with [+ - /] and avg
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defspec addition
  (prop/for-all [[x1 y1 :as v1] (s/gen ::v/vector)
                 [x2 y2 :as v2] (s/gen ::v/vector)]
    (= (+ v1 v2)
       [(+ x1 x2)
        (+ y1 y2)])))

(defspec subtraction
  (prop/for-all [[x1 y1 :as v1] (s/gen ::v/vector)
                 [x2 y2 :as v2] (s/gen ::v/vector)]
    (= (- v1 v2)
       [(- x1 x2)
        (- y1 y2)])))

(defspec scaler-division
  (prop/for-all [[x y :as v] (s/gen ::v/vector)
                 s (gen/such-that
                    (complement zero?)
                    (s/gen ::v/nice-double))]
    (= (/ v s)
       [(/ x s)
        (/ y s)])))

(defspec-test test-ns-specs
  (stest/enumerate-namespace 'jreber.boids.math.vector))
