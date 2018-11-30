(ns jreber.boids.boid
  (:refer-clojure :exclude [+])
  (:require [clojure.spec.alpha :as s]
            [jreber.boids.math.vector :as v]
            [clojure.algo.generic.arithmetic :as generic :refer [+]]))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; boid definitions
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(s/def ::position ::v/vector)
(s/def ::velocity ::v/vector)
(s/def ::boid (s/keys :req [::position
                            ::velocity]))
(s/def ::boids (s/coll-of ::boid :min-count 1))

(def boid-accelerate-fspec
  (s/fspec
   :args (s/cat :boid         ::boid
                :acceleration ::v/vector)
   :ret ::boid
   :fn (s/and #(= (-> % :ret ::velocity)
                  (+ (-> % :args :boid ::velocity)
                     (-> % :args :acceleration)))
              #(= (-> % :ret ::position)
                  (-> % :args :boid ::position)))))

(def boid-move-fspec
  (s/fspec
   :args (s/cat :boid ::boid)
   :ret ::boid
   :fn (s/and #(= (-> % :ret ::position)
                  (+ (-> % :args :boid ::position)
                     (-> % :args :boid ::velocity)))
              #(= (-> % :ret ::velocity)
                  (-> % :args :boid ::velocity)))))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; basic boid operations
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn accelerate
  [boid acceleration]
  (s/assert ::boid boid)
  (update boid ::velocity + acceleration))

(s/def accelerate boid-accelerate-fspec)

(defn move
  "Moves a boid forward in time one time step, corresponding to adding
  the velocity vector to the position vector."
  [{:keys [::velocity] :as boid}]
  (s/assert ::boid boid)
  (update boid ::position + velocity))

(s/def move boid-move-fspec)
