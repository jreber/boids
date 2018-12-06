(ns jreber.boids.flock-test
  (:require [clojure.spec.test.alpha :as stest]
            [plumula.mimolette.alpha :refer [defspec-test]]))


(defspec-test test-ns-specs
  (stest/enumerate-namespace 'jreber.boids.flock))


(comment
  "Trying to make better spec-testing macro."

  (defmacro test-specs [ns]
   (let [vars (stest/enumerate-namespace ns)
         deftest (fn [var]
                   (list 'defspec-test
                         (symbol (name var))
                         (symbol (str ns) (name var))))]
     (cons 'do
           (map deftest vars))))


  (macroexpand-1 '(test-specs jreber.boids.flock))

  (do
    (defspec-test boids-neighbors-fspec `jreber.boids.flock/boids-neighbors-fspec)
    (defspec-test boids-avg-velocity-fspec jreber.boids.flock/boids-avg-velocity-fspec)
    (defspec-test center jreber.boids.flock/center)
    (defspec-test avg-velocity jreber.boids.flock/avg-velocity)
    (defspec-test neighbors jreber.boids.flock/neighbors)
    (defspec-test boids-center-fspec jreber.boids.flock/boids-center-fspec)))
