(defproject boids "0.1.0-SNAPSHOT"
  :description "Boids simulation"
  :url "https://github.com/jreber/boids"
  :license {:name "GPL v3.0"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [clojure-future-spec "1.9.0-beta4"]
                 [org.clojure/algo.generic "0.1.3"]]
  :main ^:skip-aot boids.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
