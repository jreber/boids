(ns jreber.boids.boid)

(defprotocol boid
  "Basic data algebra for a boid; or, 'the set of all things you can do
  with a boid.'"

  (position [boid] "Returns the position of this boid in some undetermined
  coordinate system.")

  (velocity [boid] "Returns the velocity of this boid in some undetermined
  format.")

  (adjust [boid flock] "Returns an acceleration (in some undetermined format) to
  keep this boid aligned with its flock. A flock is a set/seq of boids."))
