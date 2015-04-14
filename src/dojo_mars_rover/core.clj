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

(defn get-direction [orientation command]  
  (get-in displacements [orientation command]))

(defn get-new-coordinates [{:keys [coords orientation]} command]
  {:coords (if (or (= command "f")
                   (= command "b"))
             (add-coordinates coords (get-direction orientation command))
             coords)})

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
  {:orientation 
   (cond (= "r" command) (turn-right orientation)
         (= "l" command) (turn-left orientation)
         :default orientation)})

(defn move [old-position command]
  (merge (get-new-coordinates old-position command) 
         (calculate-new-orientation command old-position)))

(defn receive [position commands]
  (reduce move position (split  commands #"")))

(defn rover [x y orientation]
  {:coords [x y] :orientation orientation})