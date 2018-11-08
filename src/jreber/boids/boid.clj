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
(s/def ::boid (s/keys ::position
                      ::velocity))
(s/def ::boids (s/coll-of ::boid))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; basic boid operations
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defmethod + [clojure.lang.IPersistentMap v/Vector] ;; add acceleration to a boid
  [m acceleration]
  (let [{:keys [::velocity] :as boid} (s/conform ::boid m)]
    (if (= boid ::s/invalid)
      (throw (ex-info "Invalid boid provided"
                      (s/explain-data ::boid m)))
      (update boid ::velocity + acceleration))))

(defn move
  "Moves a boid forward in time one time step, corresponding to adding
  the velocity vector to the position vector."
  [m]
  (let [{:keys [::position ::velocity] :as boid} (s/conform ::boid m)]
    (if (= boid ::s/invalid)
      (throw (ex-info "Invalid boid provided"
                      (s/explain-data ::boid m)))
      (update boid ::position + velocity))))

(s/fdef move
  :args (s/cat :boid ::boid)
  :ret ::boid
  :fn (s/and #(= (-> % :ret ::position)
                 (+ (-> % :args :boid ::position)
                    (-> % :args :boid ::velocity)))
             #(= (-> % :ret ::velocity)
                 (-> % :args :boid ::velocity))))
