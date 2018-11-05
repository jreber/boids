(ns jreber.boids.math.vector
  (:refer-clojure :exclude [+])
  (:require [clojure.algo.generic.arithmetic :as generic]))

(defprotocol Vector
  "A representation of a simple vector. Up/down vector? Who cares!"
  (magnitude [vector] "Returns the magnitude of the vector")

  (angle [vector] "Returns the angle of the vector (in radians? from what
  zero?).")

  (x [vector] "Returns the x component of the vector, assuming 'x' means
  something useful.")

  (y [vector] "Returns the y component of the vector, assuming 'y' means
  something useful.")

  (add [vector other] "Returns a new vector that is the sum of vector and
  other."))

(defmethod generic/+ [Vector Vector]
  [v1 v2]
  (add v1 v2))
