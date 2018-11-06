(ns jreber.boids.main
  (:refer-clojure :exclude [update])
  (:require [jreber.boids.boid  :as boid]
            [jreber.boids.flock :as flock]))

(defn flock-movements
  "Returns an infinite, lazy sequence of successive states of the flock."
  [flock create-boid]
  (let [boids (flock/boids flock)
        f (fn [boid]
            (create-boid
             (boid/update boid flock)))]
    (map f boids)))
