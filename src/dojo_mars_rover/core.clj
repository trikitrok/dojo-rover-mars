(ns dojo-mars-rover.core
  (:require [clojure.java.io :as io])
  (:gen-class :main true))

(use '[clojure.string :only (split)])

(defn add-coordinates [c1 c2]
  (map + c1 c2))

(defn get-direction [orientation command]
  (cond (and (= :north orientation) (= "f" command)) [0 1]
        (and (= :north orientation) (= "b" command)) [0 -1]
        (and (= :north orientation) (= "l" command)) [-1 0]
        (and (= :north orientation) (= "r" command)) [1 0]
        (and (= :south orientation) (= "f" command)) [0 -1]
        (and (= :south orientation) (= "b" command)) [0 1]
        (and (= :south orientation) (= "l" command)) [1 0]
        (and (= :south orientation) (= "r" command)) [-1 0]
        
        (and (= :east orientation) (= "f" command)) [1 0]
        (and (= :east orientation) (= "b" command)) [-1 0]
        (and (= :east orientation) (= "l" command)) [0 1]
        (and (= :east orientation) (= "r" command)) [0 -1]
        (and (= :west orientation) (= "f" command)) [-1 0]
        (and (= :west orientation) (= "b" command)) [1 0]
        (and (= :west orientation) (= "l" command)) [0 -1]
        (and (= :west orientation) (= "r" command)) [0 1]
        :default [0 0]))

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

(defn calculate-new-position [old-position command]
  (merge (get-new-coordinates old-position command) 
         (calculate-new-orientation command old-position)))

(defn receive [init-position commands]
  (reduce calculate-new-position init-position (split  commands #"")))

(defn rover [x y orientation]
  {:coords [x y] :orientation orientation})