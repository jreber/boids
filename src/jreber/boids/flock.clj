(ns jreber.boids.flock)

(defprotocol flock
  "Represents the set of things you cna do with a flock."

  (center-of-mass [this] "Returns the position of the center of mass of the
  flock.")

  (nearyby-boids [flock radius position] "Returns the set of boids within radius
  distance of position.")

  (density-gradient [flock position] "Returns the density graident (in some
  format) of the flock at position.")

  (boids [flock] "Returns a seq of all the boids in this flock."))
