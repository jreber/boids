(ns jreber.boids.boid-test
  (:require [clojure.spec.test.alpha :as stest]
            [plumula.mimolette.alpha :refer [defspec-test]]))

(defspec-test test-ns-specs
  (stest/enumerate-namespace 'jreber.boids.boid))
