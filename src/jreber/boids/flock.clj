(ns jreber.boids.flock
  (:refer-clojure :exclude [-])
  (:require [clojure.spec.alpha :as s]
            [jreber.boids.boid :as boid]
            [jreber.boids.math.vector :as v]
            [clojure.algo.generic.arithmetic :as generic :refer [-]]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; specs for functions dealing groups of boids (flocks)
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(def boids-center-fspec
  (s/fspec :args (s/cat :boids ::boid/boids)
           :ret ::v/vector))

(def boids-avg-velocity-fspec
 (s/fspec :args (s/cat :boids ::boid/boids)
          :ret ::v/vector))

(def boids-neighbors-fspec
  (s/fspec :args (s/cat :boids    ::boid/boids
                        :radius   number?
                        :pos      ::v/vector)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; implementations of the above specs
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn center
  "Returns a vector representing the center of the group of boids."
  [boids]
  (s/assert ::boid/boids boids)
  (v/avg
   (map ::boid/position boids)))

(s/def center boids-center-fspec)


(defn avg-velocity
  "Returns a vector representing the average velocity of the group of boids."
  [boids]
  (s/assert ::boid/boids boids)
  (v/avg
   (map ::boid/velocity boids)))

(s/def avg-velocity boids-avg-velocity-fspec)


(defn neighbors
  "Returns the set of boids within radius distance (exclusive) of position."
  [boids radius pos]
  (s/assert ::boid/boids boids)
  (letfn [(within-radius? [{:keys [::boid/position]}]
            (< (v/magnitude
                (- position pos))
               radius))]
    (filter within-radius? boids)))

(s/def neighbors boids-neighbors-fspec)
