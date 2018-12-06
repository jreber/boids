(ns jreber.boids.flock
  (:refer-clojure :exclude [-])
  (:require [clojure.spec.alpha :as s]
            [clojure.spec.test.alpha :as stest]
            [jreber.boids.boid :as boid]
            [jreber.boids.math.vector :as v :refer [x y]]
            [clojure.algo.generic.arithmetic :as generic :refer [-]]))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; specs for functions dealing groups of boids (flocks)
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(def boids-center-fspec
  (s/fspec :args (s/cat :boids ::boid/boids)
           :ret ::v/vector
           :fn (let [find (fn [reducer vector-component invocation]
                            (->> (:boids
                                  (:args invocation))
                                 (map ::boid/position)
                                 (map vector-component)
                                 (reduce reducer)))]
                 (s/and #(<= (find min x %) (x (:ret %)))
                        #(<= (x (:ret %)) (find max x %))
                        #(<= (find min v/y %) (v/y (:ret %)))
                        #(<= (v/y (:ret %)) (find max v/y %))))))

(def boids-avg-velocity-fspec
  (s/fspec :args (s/cat :boids ::boid/boids)
           :ret ::v/vector
           :fn (let [find (fn [reducer vector-component invocation]
                            (->> (:boids
                                  (:args invocation))
                                 (map ::boid/velocity)
                                 (map vector-component)
                                 (reduce reducer)))]
                 (s/and #(<= (find min x %) (x (:ret %)))
                        #(<= (x (:ret %)) (find max x %))
                        #(<= (find min y %) (y (:ret %)))
                        #(<= (y (:ret %)) (find max y %))))))

(def boids-neighbors-fspec
  (s/fspec :args (s/cat :boids    ::boid/boids
                        :radius   number?
                        :pos      ::v/vector)
           :ret (s/nilable ::boid/boids)
           :fn #(let [{:keys [radius pos boids]} (:args %)
                      nearby? (fn [boid] (< (v/magnitude
                                             (- (::boid/position boid)
                                                pos))
                                            radius))]
                  (->> boids
                       (filter nearby?)
                       set
                       (= (set (:ret %)))))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; implementations of the above specs
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn center
  "Returns a vector representing the center of the group of boids."
  [boids]
  (v/avg
   (map ::boid/position boids)))

(s/def center boids-center-fspec)


(defn avg-velocity
  "Returns a vector representing the average velocity of the group of boids."
  [boids]
  (v/avg
   (map ::boid/velocity boids)))

(s/def avg-velocity boids-avg-velocity-fspec)


(defn neighbors
  "Returns the set of boids within radius distance (exclusive) of position."
  [boids radius pos]
  (letfn [(within-radius? [boid]
            (< (v/magnitude
                (- (::boid/position boid)
                   pos))
               radius))]
    (not-empty
     (filter within-radius? boids))))

(s/def neighbors boids-neighbors-fspec)
