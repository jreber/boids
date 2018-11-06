(ns jreber.boids.boid
  (:refer-clojure :exclude [update]))

(defprotocol boid
  "Basic data algebra for a boid; or, 'the set of all things you can do
  with a boid.'"

  (position [boid] "Returns the position of this boid in some undetermined
  coordinate system.")

  (velocity [boid] "Returns the velocity of this boid in some undetermined
  format.")

  (adjust [boid flock] "Returns an acceleration (in some undetermined format) to
  keep this boid aligned with its flock. A flock is a set/seq of boids."))

(defn update
  "Returns a new boid position and velocity that is moved one time step
  according to the values of (position ...), (velocity ...),
  and (adjust ...).

  The difference between this function and boid#adjust is that adjust
  returns the acceleration that the boid would do, while this function
  actually makes that change."
  [boid flock]
  (let [pos   (position boid)
        vel   (velocity boid)
        accel (adjust boid flock)]
    [(+ pos vel)
     (+ vel accel)]))
