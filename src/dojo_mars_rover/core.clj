(ns dojo-mars-rover.core
  (:require [clojure.java.io :as io])
  (:gen-class :main true))

(use '[clojure.string :only (split)])

(defn add-coordinates [c1 c2]
  (map + c1 c2))

(def displacements 
  {:north {"f" [0 1] "b" [0 -1]}
   :south {"f" [0 -1] "b" [0 1]}
   :east {"f" [1 0] "b" [-1 0]}
   :west {"f" [-1 0] "b" [1 0]}})

(defn get-new-coordinates [{:keys [coords orientation]} command]
  (if (or (= command "f")
          (= command "b"))
    (add-coordinates coords (get-in displacements [orientation command]))
    coords))

(defn turn-right [orientation]
  (case orientation
    :north :east
    :south :west
    :east :south
    :west :north))

(defn turn-left [orientation]
  (case orientation
    :north :west
    :south :east
    :east :north
    :west :south))

(defn calculate-new-orientation [command {:keys [orientation]}]
  (cond (= "r" command) (turn-right orientation)
        (= "l" command) (turn-left orientation)
        :default orientation))

(defn move [old-position command]
  {:coords (get-new-coordinates old-position command) 
    :orientation (calculate-new-orientation command old-position)})

(defn receive [position commands]
  (reduce move position (split  commands #"")))

(defn rover [x y orientation]
  {:coords [x y] :orientation orientation})