(ns dojo-mars-rover.core
  (:use [clojure.string :only [split]]))

(defn- add-coords [c1 c2]
  (map + c1 c2))

(def ^:private displacements 
  {:north {"f" [0 1] "b" [0 -1]}
   :south {"f" [0 -1] "b" [0 1]}
   :east {"f" [1 0] "b" [-1 0]}
   :west {"f" [-1 0] "b" [1 0]}})

(defn- displacement [orientation command]
  (get-in displacements [orientation command]))

(defn- calc-coords [command {:keys [coords orientation]}]
  (if (or (= command "f") (= command "b"))
    (add-coords coords (displacement orientation command))
    coords))

(defn- turn-right [orientation]
  (case orientation
    :north :east
    :south :west
    :east :south
    :west :north))

(defn- turn-left [orientation]
  (case orientation
    :north :west
    :south :east
    :east :north
    :west :south))

(defn- calc-orientation [command {:keys [orientation]}]
  (case command 
    "r" (turn-right orientation)
    "l" (turn-left orientation)
    orientation))

(defn- move [position command]
  {:coords (calc-coords command position) 
   :orientation (calc-orientation command position)})

(defn receive [position commands]
  (reduce move position (split commands #"")))

(defn rover [x y orientation]
  {:coords [x y] :orientation orientation})