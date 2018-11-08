(ns jreber.boids.behaviors
  (:require [jreber.boids
             [boid  :as boid]
             [flock :as flock]]
            [jreber.boids.math.vector :as v]
            [clojure.algo.generic.arithmetic :as generic :refer [-]]))

(defn align-by-pos-vel
  "Returns an acceleration vector to attempt to align the given boid to
  the others."
  [boids radius {:keys [::boid/position ::boid/velocity]}]
  (let [neighbors (flock/neighbors boids radius position)
        avg-p     (flock/center neighbors)
        avg-v     (flock/avg-velocity neighbors)
        desired-v (v/avg [avg-p avg-v])]
    (- desired-v velocity)))
